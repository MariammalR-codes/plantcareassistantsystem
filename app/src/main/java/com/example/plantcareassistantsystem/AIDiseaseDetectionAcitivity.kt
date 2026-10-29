package com.example.plantcareassistantsystem

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.google.android.material.button.MaterialButton
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class AIDiseaseDetectionActivity : AppCompatActivity() {

    // ================= UI =================
    private lateinit var backButton: TextView
    private lateinit var takePhotoButton: MaterialButton
    private lateinit var galleryButton: MaterialButton
    private lateinit var plantPreview: ImageView
    private lateinit var photoTitle: TextView
    private lateinit var analyseButton: MaterialButton
    private lateinit var cropSelectLayout: LinearLayout
    private lateinit var cropSpinner: Spinner

    // ================= IMAGE =================
    private var selectedBitmap: Bitmap? = null
    private var cameraPhotoFile: File? = null

    private data class ResultData(
        val status: String,      // DISEASE / HEALTHY / INVALID
        val plant: String,
        val disease: String,
        val symptoms: String,
        val severity: String
    )

    private var lastImageKey: Int? = null
    private var lastResult: ResultData? = null

    // ================= AI MODEL =================
    private var interpreter: Interpreter? = null
    private var featureExtractor: Interpreter? = null
    private var labels: List<String> = emptyList()

    private var selectedCrop: String = "Paddy"
    private var loadedCrop: String? = null

    // ---- Crop classifier (auto crop detection) ----
    private var cropClassifier: Interpreter? = null
    private var cropLabels: List<String> = emptyList()

    // Disease models cache (oru thadava load pannina, thirumba load aagaadhu)
    private val diseaseModelCache = mutableMapOf<String, Interpreter>()
    private val diseaseLabelCache = mutableMapOf<String, List<String>>()

    private val cropModels = linkedMapOf(
        "Paddy" to Pair("paddy_disease_model.tflite", "paddy_labels.txt"),
        "Banana" to Pair("banana_disease_head.tflite", "banana_labels.txt"),
        "Mango" to Pair("mango_disease_head.tflite", "mango_labels.txt"),
        "Coconut" to Pair("coconut_disease_head.tflite", "coconut_labels.txt"),
        "Groundnut" to Pair("groundnut_disease_head.tflite", "groundnut_labels.txt"),
        "Cotton" to Pair("cotton_disease_head.tflite", "cotton_labels.txt"),
        "Sugarcane" to Pair("sugarcane_disease_head.tflite", "sugarcane_labels.txt")
    )

    companion object {
        private const val TAG = "PLANT_DEBUG"
        private const val AUTO_DETECT = "Auto detect"

        private const val GALLERY_REQUEST = 2001
        private const val CAMERA_REQUEST = 2002

        private const val EXTRACTOR_FILE = "mobilenetv2_feature_extractor.tflite"
        private const val CROP_CLASSIFIER_FILE = "crop_classifier_head.tflite"
        private const val CROP_LABELS_FILE = "crop_labels.txt"
        private const val FEATURE_SIZE = 1280

        // Crop classifier idhukku mela sure na mattum crop-a accept pannum
        private const val CROP_MIN_CONFIDENCE = 0.60f

        // Not_Plant probability idhukku mela irundha mattum "not a plant" nu solum
        private const val NOT_PLANT_REJECT = 0.40f

        // Photo-la idhukku mela green pixels irundha => plant. Not_Plant nu vandhaalum plant nu eduthukkum
        private const val GREEN_PLANT_RATIO = 0.40f

        // Idhukku keezha green irundha => plant illa (face, wall, table...)
        private const val MIN_GREEN_RATIO = 0.10f

        // Skin pixels idhukku mela irundhu, green kammi na => human / face
        private const val MAX_SKIN_RATIO = 0.30f

        // PlantVillage model (tomato, apple, potato...) - Other_Plant ku use aagum
        private const val PV_KEY = "Other Plants"
        private const val PV_MODEL_FILE = "plant_disease_model.tflite"
        private const val PV_LABELS_FILE = "class_names.json"

        // PlantVillage model input scaling: 0 = raw 0-255, 1 = 0..1 (x/255), 2 = -1..1 (x/127.5-1)
        private const val PV_INPUT_MODE = 0

        private const val LABEL_NOT_PLANT = "Not_Plant"
        private const val LABEL_OTHER_PLANT = "Other"

        private const val IMAGE_SIZE = 224
        private const val NUM_CHANNELS = 3
        private const val MAX_IMAGE_SIDE = 1024

        // Model top score idhukku keezha irundha => "not a plant image"
        private const val MIN_CONFIDENCE = 0.45f

        // Disease idhukku mela sure na => disease nu confirm
        private const val DISEASE_CONFIRM_THRESHOLD = 0.70f

        // Disease sure illa + healthy score = (disease score x idhu) allathu athukku mela => healthy
        private const val HEALTHY_OVERRIDE_RATIO = 0.50f

        // "Healthy" class illatha crop (Coconut) la, top score idhukku keezha na => healthy
        private const val NO_HEALTHY_CLASS_THRESHOLD = 0.60f

        // Leaf pixels-la idhukku keezha brown/yellow spots irundha => healthy
        private const val LESION_HEALTHY_MAX = 0.04f

        // Healthy nu solla minimum green
        private const val HEALTHY_MIN_GREEN = 0.50f
    }

    // =========================================================
    // ON CREATE
    // =========================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_disease_detection)

        backButton = findViewById(R.id.backButton)
        takePhotoButton = findViewById(R.id.takePhotoButton)
        galleryButton = findViewById(R.id.galleryButton)
        plantPreview = findViewById(R.id.plantPreview)
        photoTitle = findViewById(R.id.photoTitle)
        analyseButton = findViewById(R.id.analyseButton)
        cropSelectLayout = findViewById(R.id.cropSelectLayout)
        cropSpinner = findViewById(R.id.cropSpinner)

        // "Auto detect" first, then every crop
        val cropOptions = listOf(AUTO_DETECT) + cropModels.keys + PV_KEY
        cropSpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, cropOptions
        )

        loadCropClassifier()

        backButton.setOnClickListener { finish() }
        takePhotoButton.setOnClickListener { openCamera() }
        galleryButton.setOnClickListener { openGallery() }
        analyseButton.setOnClickListener { analysePlant() }
    }

    // =========================================================
    // CROP CLASSIFIER LOAD
    // =========================================================

    private fun loadCropClassifier() {
        try {
            featureExtractor = Interpreter(loadModelFile(EXTRACTOR_FILE))
        } catch (e: Exception) {
            Log.e(TAG, "Feature extractor load failed", e)
            return
        }

        try {
            val classifier = Interpreter(loadModelFile(CROP_CLASSIFIER_FILE))
            val classes = loadLabels(CROP_LABELS_FILE)

            val outSize = classifier.getOutputTensor(0).shape().last()
            if (outSize != classes.size) {
                classifier.close()
                throw Exception("Crop classifier gives $outSize outputs but crop_labels.txt has ${classes.size} labels")
            }

            cropClassifier = classifier
            cropLabels = classes
            Log.d(TAG, "Crop classifier loaded: $classes")
        } catch (e: Exception) {
            cropClassifier = null
            Log.w(TAG, "Crop classifier not found, manual crop select mode", e)
        }
    }

    // =========================================================
    // LOAD MODEL + LABELS
    // =========================================================

    // Crop-ku sariyaana disease model-a ready pannum (cache-la irundha thirumba load aagaadhu)
    private fun useDiseaseModel(crop: String, silent: Boolean = false): Boolean {
        return try {
            val modelInfo = if (crop == PV_KEY) {
                Pair(PV_MODEL_FILE, PV_LABELS_FILE)
            } else {
                cropModels[crop] ?: throw Exception("Unsupported crop: $crop")
            }

            val cachedModel = diseaseModelCache[crop]
            val cachedLabels = diseaseLabelCache[crop]

            if (cachedModel != null && cachedLabels != null) {
                interpreter = cachedModel
                labels = cachedLabels
            } else {
                val newLabels = if (crop == PV_KEY) loadJsonLabels(modelInfo.second) else loadLabels(modelInfo.second)

                if (crop != PV_KEY) {
                    val expectedLabels = when (crop) {
                        "Paddy" -> 10
                        "Banana" -> 3
                        "Mango" -> 8
                        "Coconut" -> 5
                        "Groundnut" -> 5
                        "Cotton" -> 5
                        "Sugarcane" -> 11
                        else -> throw Exception("Unsupported crop: $crop")
                    }
                    if (newLabels.size != expectedLabels) {
                        throw Exception("$crop requires $expectedLabels labels; found ${newLabels.size}")
                    }
                }

                val model = Interpreter(loadModelFile(modelInfo.first))

                // Output size = labels count irukkanum
                val outSize = model.getOutputTensor(0).shape().last()
                if (outSize != newLabels.size) {
                    model.close()
                    throw Exception("$crop: model gives $outSize outputs but labels file has ${newLabels.size}")
                }

                diseaseModelCache[crop] = model
                diseaseLabelCache[crop] = newLabels
                interpreter = model
                labels = newLabels
            }

            selectedCrop = crop
            loadedCrop = crop
            Log.d(TAG, "$crop model ready, labels = $labels")
            true

        } catch (e: Exception) {
            Log.e(TAG, "Failed to load $crop model", e)
            if (!silent) {
                Toast.makeText(this, "$crop model loading failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
            false
        }
    }

    private fun loadModelFile(modelName: String): ByteBuffer {
        val fd = assets.openFd(modelName)

        return FileInputStream(fd.fileDescriptor).channel.map(
            FileChannel.MapMode.READ_ONLY,
            fd.startOffset,
            fd.declaredLength
        )
    }

    // class_names.json => ["Apple___Apple_scab", ...]
    private fun loadJsonLabels(fileName: String): List<String> {
        val text = assets.open(fileName).bufferedReader().use { it.readText() }
        val arr = org.json.JSONArray(text)
        return List(arr.length()) { arr.getString(it).trim() }
    }

    private fun loadLabels(labelsFileName: String): List<String> {
        val list = mutableListOf<String>()

        BufferedReader(
            InputStreamReader(assets.open(labelsFileName))
        ).useLines { lines ->
            lines.forEach {
                if (it.isNotBlank()) {
                    list.add(it.trim())
                }
            }
        }

        return list
    }

    // =========================================================
    // GALLERY / CAMERA
    // =========================================================

    private fun openGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    // Full-size camera photo saved to cache via FileProvider
    private fun openCamera() {
        try {
            val file = File(cacheDir, "camera_photo.jpg")
            cameraPhotoFile = file

            val uri: Uri = FileProvider.getUriForFile(
                this,
                "$packageName.fileprovider",
                file
            )

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivityForResult(intent, CAMERA_REQUEST)

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Camera error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            GALLERY_REQUEST -> {
                val uri = data?.data
                if (uri != null) {
                    try {
                        val bitmap = loadBitmapFromUri(uri)
                        if (bitmap != null) setSelectedImage(bitmap)
                        else Toast.makeText(this, "Unable to load image", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Unable to load image", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }
            }

            CAMERA_REQUEST -> {
                val file = cameraPhotoFile
                if (file != null && file.exists()) {
                    val bitmap = loadBitmapFromFile(file)
                    if (bitmap != null) {
                        setSelectedImage(bitmap)
                        return
                    }
                }
                // Fallback: thumbnail
                val thumb = data?.extras?.get("data") as? Bitmap
                if (thumb != null) setSelectedImage(thumb)
            }
        }
    }

    // ---------- Load + fix EXIF rotation (same photo => same result) ----------

    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        // Pass 1: read size only
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }

        val opts = BitmapFactory.Options().apply {
            inSampleSize = calcSampleSize(bounds.outWidth, bounds.outHeight)
        }
        val bitmap = contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, opts)
        } ?: return null

        val rotation = contentResolver.openInputStream(uri)?.use { readExifRotation(it) } ?: 0f
        return rotateIfNeeded(bitmap, rotation)
    }

    private fun loadBitmapFromFile(file: File): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.absolutePath, bounds)

        val opts = BitmapFactory.Options().apply {
            inSampleSize = calcSampleSize(bounds.outWidth, bounds.outHeight)
        }
        val bitmap = BitmapFactory.decodeFile(file.absolutePath, opts) ?: return null

        val rotation = FileInputStream(file).use { readExifRotation(it) }
        return rotateIfNeeded(bitmap, rotation)
    }

    private fun calcSampleSize(w: Int, h: Int): Int {
        var sample = 1
        var longest = maxOf(w, h)
        while (longest / 2 >= MAX_IMAGE_SIDE) {
            longest /= 2
            sample *= 2
        }
        return sample
    }

    private fun readExifRotation(stream: java.io.InputStream): Float {
        return try {
            val exif = ExifInterface(stream)
            when (exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        } catch (e: Exception) {
            0f
        }
    }

    private fun rotateIfNeeded(bitmap: Bitmap, degrees: Float): Bitmap {
        if (degrees == 0f) return bitmap
        val m = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    private fun setSelectedImage(original: Bitmap) {
        val scaled = scaleDown(original)
        val safeBitmap = scaled.copy(Bitmap.Config.ARGB_8888, false)

        selectedBitmap = safeBitmap

        // New image => clear previous cache
        lastImageKey = null
        lastResult = null

        plantPreview.setImageBitmap(safeBitmap)
        plantPreview.visibility = ImageView.VISIBLE
        photoTitle.visibility = TextView.VISIBLE
        cropSelectLayout.visibility = View.VISIBLE
        analyseButton.visibility = MaterialButton.VISIBLE
        photoTitle.text = "Your Plant Photo"
        analyseButton.isEnabled = true
    }

    private fun scaleDown(bitmap: Bitmap): Bitmap {
        val longest = maxOf(bitmap.width, bitmap.height)
        if (longest <= MAX_IMAGE_SIDE) return bitmap

        val ratio = MAX_IMAGE_SIDE.toFloat() / longest
        return Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * ratio).toInt(),
            (bitmap.height * ratio).toInt(),
            true
        )
    }

    // Center crop to a square, then resize to 224x224 (no squashing)
    private fun centerCropAndResize(bitmap: Bitmap): Bitmap {
        val size = minOf(bitmap.width, bitmap.height)
        val x = (bitmap.width - size) / 2
        val y = (bitmap.height - size) / 2
        val square = Bitmap.createBitmap(bitmap, x, y, size, size)
        return Bitmap.createScaledBitmap(square, IMAGE_SIZE, IMAGE_SIZE, true)
    }

    // =========================================================
    // MODEL RUN
    // =========================================================

    // Models already have preprocessing inside => raw RGB values 0-255
    private fun bitmapToInput(bmp: Bitmap, mode: Int = 0): ByteBuffer {
        val inputBuffer = ByteBuffer.allocateDirect(
            4 * IMAGE_SIZE * IMAGE_SIZE * NUM_CHANNELS
        ).order(ByteOrder.nativeOrder())

        val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE)
        bmp.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE)

        fun scale(v: Int): Float = when (mode) {
            1 -> v / 255f
            2 -> v / 127.5f - 1f
            else -> v.toFloat()
        }

        for (pixel in pixels) {
            inputBuffer.putFloat(scale((pixel shr 16) and 0xFF))
            inputBuffer.putFloat(scale((pixel shr 8) and 0xFF))
            inputBuffer.putFloat(scale(pixel and 0xFF))
        }
        inputBuffer.rewind()
        return inputBuffer
    }

    private fun extractFeatures(bmp: Bitmap): FloatArray {
        val extractor = featureExtractor
            ?: throw IllegalStateException("Feature extractor is not loaded")

        val features = Array(1) { FloatArray(FEATURE_SIZE) }
        extractor.run(bitmapToInput(bmp), features)
        return features[0]
    }

    private fun runHead(head: Interpreter, features: FloatArray, numLabels: Int): FloatArray {
        val output = Array(1) { FloatArray(numLabels) }
        head.run(arrayOf(features), output)
        return output[0]
    }

    // Disease model run (selectedCrop-ku sariyaana model)
    // Input shape rank 4 => full image model (Paddy / PlantVillage), rank 2 => feature head
    private fun runModel(bmp: Bitmap): FloatArray {
        val model = interpreter
            ?: throw IllegalStateException("Crop model is not loaded")

        val inputRank = model.getInputTensor(0).shape().size

        if (inputRank == 4) {
            val mode = if (selectedCrop == PV_KEY) PV_INPUT_MODE else 0
            val output = Array(1) { FloatArray(labels.size) }
            model.run(bitmapToInput(bmp, mode), output)
            return output[0]
        }

        return runHead(model, extractFeatures(bmp), labels.size)
    }

    // Crop classifier run (original + horizontal flip average)
    // Input rank 2 => feature head (1280), rank 4 => full image model
    private fun predictCrop(bmp: Bitmap): FloatArray {
        val classifier = cropClassifier
            ?: throw IllegalStateException("Crop classifier is not loaded")

        val isImageModel = classifier.getInputTensor(0).shape().size == 4
        val views = listOf(bmp, transform(bmp, -1f, 1f, 0f))
        val avg = FloatArray(cropLabels.size)

        for (v in views) {
            val p = if (isImageModel) {
                val output = Array(1) { FloatArray(cropLabels.size) }
                classifier.run(bitmapToInput(v), output)
                output[0]
            } else {
                runHead(classifier, extractFeatures(v), cropLabels.size)
            }
            for (i in p.indices) avg[i] += p[i] / views.size
        }
        return avg
    }

    private fun transform(bmp: Bitmap, sx: Float, sy: Float, degrees: Float): Bitmap {
        val m = Matrix()
        m.postRotate(degrees)
        m.postScale(sx, sy)
        return Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, m, true)
    }

    // Original + horizontal flip only (vertical flip / rotate accuracy kammi pannum)
    private fun predictWithTTA(bmp: Bitmap): FloatArray {
        val views = listOf(
            bmp,
            transform(bmp, -1f, 1f, 0f)   // horizontal flip
        )

        val avg = FloatArray(labels.size)
        for (v in views) {
            val p = runModel(v)
            for (i in p.indices) avg[i] += p[i] / views.size
        }
        return avg
    }

    // =========================================================
    // HEALTHY / DISEASE DECISION HELPERS
    // =========================================================

    private fun isHealthyLabel(label: String): Boolean {
        return label.contains("healthy", ignoreCase = true) ||
                label.contains("normal", ignoreCase = true)
    }

    // "BrownRust" => "Brown Rust", "bacterial_leaf_blight" => "Bacterial Leaf Blight"
    private fun prettyName(raw: String): String {
        val lower = raw.lowercase()
        if (lower == "xamthomonas" || lower == "xanthomonas") return "Xanthomonas (Bacterial Wilt)"
        if (lower == "segatoka" || lower == "sigatoka") return "Sigatoka Leaf Spot"

        return raw
            .replace(Regex("([a-z])([A-Z])"), "$1 $2")
            .replace("_", " ")
            .trim()
            .lowercase()
            .split(" ")
            .filter { it.isNotBlank() }
            .joinToString(" ") { w -> w.replaceFirstChar { c -> c.uppercase() } }
    }

    // =========================================================
    // AI ANALYSIS
    // =========================================================

    private fun analysePlant() {
        val bitmap = selectedBitmap

        if (bitmap == null) {
            Toast.makeText(this, "Please select a plant image first", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val resized = centerCropAndResize(bitmap)

            // ---------- STEP -1: Plant-ah illaya nu mudhalaye check ----------
            val greenAll = greenRatio(resized)
            val skinAll = skinRatio(resized)
            Log.d(TAG, "GATE green=$greenAll skin=$skinAll")

            if (greenAll < MIN_GREEN_RATIO ||
                (skinAll > MAX_SKIN_RATIO && greenAll < GREEN_PLANT_RATIO)
            ) {
                showNotPlantDialog()
                return
            }

            // ---------- STEP 0: Crop kandupidikkirom ----------
            val cropToUse: String
            val chosen = cropSpinner.selectedItem as String
            val manualCrop = if (chosen == AUTO_DETECT) null else chosen

            if (manualCrop != null) {
                // User selected the crop => trust it, skip crop classifier
                cropToUse = manualCrop
            } else if (cropClassifier == null) {
                // Crop classifier illa => ella crop model-aiyum run panni best match edukkum
                cropToUse = autoDetectCrop(resized) ?: run {
                    showNotPlantDialog()
                    return
                }
            } else {
                val cropProbs = predictCrop(resized)
                var cropIndex = cropProbs.indices.maxByOrNull { cropProbs[it] } ?: 0

                cropProbs.indices
                    .sortedByDescending { cropProbs[it] }
                    .take(3)
                    .forEach { Log.d(TAG, "CropPrediction: ${cropLabels[it]} = ${cropProbs[it]}") }

                val notPlantIndex = cropLabels.indexOfFirst { it.equals(LABEL_NOT_PLANT, ignoreCase = true) }
                val notPlantProb = if (notPlantIndex >= 0) cropProbs[notPlantIndex] else 0f
                val green = greenAll
                Log.d(TAG, "notPlantProb = $notPlantProb, greenRatio = $green")

                // Classifier "Not_Plant" nu sonna mattum reject. Confidence kammi na reject pannaadhu.
                val classifierSaysNotPlant = cropIndex == notPlantIndex || notPlantProb >= NOT_PLANT_REJECT

                if (classifierSaysNotPlant) {
                    if (green >= GREEN_PLANT_RATIO) {
                        // Green leaf photo => next best plant class
                        val alt = cropProbs.indices
                            .filter { it != notPlantIndex }
                            .maxByOrNull { cropProbs[it] }
                        if (alt == null) {
                            showNotPlantDialog()
                            return
                        }
                        cropIndex = alt
                        Log.d(TAG, "Not_Plant overridden by green ratio => ${cropLabels[alt]}")
                    } else {
                        showNotPlantDialog()
                        return
                    }
                }

                // Crop classifier sure illa + green kammi => plant illa
                if (cropProbs[cropIndex] < CROP_MIN_CONFIDENCE && green < GREEN_PLANT_RATIO) {
                    showNotPlantDialog()
                    return
                }

                val cropName = cropLabels[cropIndex]

                cropToUse = if (cropName.startsWith(LABEL_OTHER_PLANT, ignoreCase = true)) {
                    PV_KEY
                } else {
                    cropModels.keys.firstOrNull { it.equals(cropName, ignoreCase = true) }
                        ?: run {
                            showNotPlantDialog()
                            return
                        }
                }
            }

            if (!useDiseaseModel(cropToUse)) return

            if (interpreter == null || labels.isEmpty()) {
                Toast.makeText(this, "AI model is not loaded", Toast.LENGTH_LONG).show()
                return
            }

            val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE)
            resized.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE)

            // Same image + same crop => same result
            val imageKey = pixels.contentHashCode() * 31 + selectedCrop.hashCode()
            val cached = lastResult
            if (imageKey == lastImageKey && cached != null) {
                openResultPage(cached, bitmap)
                return
            }

            // ---------- STEP 1: Predict ----------
            val probabilities = predictWithTTA(resized)

            var bestIndex = 0
            for (i in 1 until probabilities.size) {
                if (probabilities[i] > probabilities[bestIndex]) bestIndex = i
            }
            val topConfidence = probabilities[bestIndex]

            Log.d(TAG, "crop = $selectedCrop")
            probabilities.indices
                .sortedByDescending { probabilities[it] }
                .take(3)
                .forEach { Log.d(TAG, "Prediction: ${labels[it]} = ${probabilities[it]}") }

            // ---------- STEP 2: Model not sure => not a plant image ----------
            if (topConfidence < MIN_CONFIDENCE) {
                showNotPlantDialog()
                return
            }

            // PlantVillage "Background_without_leaves" => leaf illa => not a plant image
            if (selectedCrop == PV_KEY && labels[bestIndex].contains("background", ignoreCase = true)) {
                showNotPlantDialog()
                return
            }

            // ---------- STEP 3: Disease or Healthy ----------
            val isPV = selectedCrop == PV_KEY

            // PlantVillage: predict aana plant-oda healthy class-a mattum paarkanum (Tomato___healthy)
            val healthyIndex = if (isPV) {
                val topPlant = parsePlantVillageLabel(labels[bestIndex]).first
                labels.indices.firstOrNull {
                    isHealthyLabel(labels[it]) &&
                            parsePlantVillageLabel(labels[it]).first == topPlant
                } ?: -1
            } else {
                labels.indexOfFirst { isHealthyLabel(it) }
            }

            val modelHealthy: Boolean = if (healthyIndex != -1) {
                if (bestIndex == healthyIndex) {
                    true
                } else {
                    val healthyScore = probabilities[healthyIndex]
                    val diseaseNotSure = topConfidence < DISEASE_CONFIRM_THRESHOLD
                    val healthyCloseEnough = healthyScore >= topConfidence * HEALTHY_OVERRIDE_RATIO
                    diseaseNotSure && healthyCloseEnough
                }
            } else {
                // Coconut: healthy class illa => disease clear ah theriyala na healthy
                topConfidence < NO_HEALTHY_CLASS_THRESHOLD
            }

            // Colour check: green leaf-la brown/yellow spots illa na => healthy
            val lesion = lesionRatio(resized)
            val colorHealthy = greenAll >= HEALTHY_MIN_GREEN && lesion < LESION_HEALTHY_MAX
            val isHealthy = modelHealthy || colorHealthy
            Log.d(TAG, "LESION=$lesion colorHealthy=$colorHealthy modelHealthy=$modelHealthy")

            // ---------- STEP 4: Plant + disease + symptoms + severity ----------
            val pv = if (isPV) parsePlantVillageLabel(labels[bestIndex]) else null
            val plantName = pv?.first ?: selectedCrop

            val result = if (isHealthy) {
                ResultData(
                    "HEALTHY",
                    plantName,
                    "Healthy",
                    "No symptoms found. The plant looks healthy.",
                    "None"
                )
            } else {
                val diseaseName = pv?.second ?: prettyName(labels[bestIndex])
                ResultData(
                    "DISEASE",
                    plantName,
                    diseaseName,
                    if (isPV) getSymptomsPV(diseaseName) else getSymptoms(diseaseName),
                    if (isPV) getSeverityPV(diseaseName) else getSeverity(diseaseName)
                )
            }

            finishAnalysis(imageKey, result, bitmap)

        } catch (e: Exception) {
            Toast.makeText(this, "AI analysis failed: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    // Crop classifier illa na: ella disease model-aiyum run panni, highest confidence irukkura crop-a edukkum
    private fun autoDetectCrop(bmp: Bitmap): String? {
        var bestCrop: String? = null
        var bestScore = 0f

        for (crop in (cropModels.keys + PV_KEY)) {
            if (!useDiseaseModel(crop, silent = true)) continue
            try {
                val probs = predictWithTTA(bmp)
                val top = probs.maxOrNull() ?: 0f
                Log.d(TAG, "AutoCrop: $crop top=$top")
                if (top > bestScore) {
                    bestScore = top
                    bestCrop = crop
                }
            } catch (e: Exception) {
                Log.w(TAG, "AutoCrop failed for $crop", e)
            }
        }

        Log.d(TAG, "AutoCrop result: $bestCrop ($bestScore)")
        return if (bestScore >= MIN_CONFIDENCE) bestCrop else null
    }

    // Image-la green pixels evlo % nu kanakku pannum
    private fun greenRatio(bmp: Bitmap): Float {
        val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE)
        bmp.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE)
        var green = 0
        for (p in pixels) {
            val r = (p shr 16) and 0xFF
            val g = (p shr 8) and 0xFF
            val b = p and 0xFF
            if (g > 50 && g > r * 1.08f && g > b * 1.08f) green++
        }
        return green.toFloat() / pixels.size
    }

    // Leaf-la brown/yellow/orange (spot) pixels, leaf pixels-la evlo % nu kanakku pannum
    private fun lesionRatio(bmp: Bitmap): Float {
        val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE)
        bmp.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE)
        val hsv = FloatArray(3)
        var leaf = 0
        var lesion = 0

        for (p in pixels) {
            android.graphics.Color.colorToHSV(p, hsv)
            val h = hsv[0]
            val s = hsv[1]
            val v = hsv[2]

            if (v < 0.15f) {
                continue
            }

            // Pale / beige dead tissue, holes (low saturation, bright, yellow-brown hue)
            if (s >= 0.08f && s < 0.35f && v > 0.55f && h >= 20f && h <= 55f) {
                leaf++
                lesion++
                continue
            }

            if (s < 0.25f) {
                continue
            }

            if (h >= 60f && h <= 170f) {
                leaf++
            } else if (h >= 8f && h <= 55f && s > 0.30f) {
                leaf++
                lesion++
            }
        }

        return if (leaf == 0) 1f else lesion.toFloat() / leaf
    }

    // Image-la skin-colour pixels evlo % nu kanakku pannum (face / hand detect)
    private fun skinRatio(bmp: Bitmap): Float {
        val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE)
        bmp.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE)
        var skin = 0
        for (p in pixels) {
            val r = (p shr 16) and 0xFF
            val g = (p shr 8) and 0xFF
            val b = p and 0xFF
            val mx = maxOf(r, g, b)
            val mn = minOf(r, g, b)
            if (r > 95 && g > 40 && b > 20 &&
                (mx - mn) > 15 &&
                Math.abs(r - g) > 15 &&
                r > g && r > b
            ) skin++
        }
        return skin.toFloat() / pixels.size
    }

    private fun showNotPlantDialog() {
        AlertDialog.Builder(this)
            .setTitle("Not a plant image")
            .setMessage("Could not find a supported crop in this photo. Please give a clear close-up photo of the plant leaf.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun finishAnalysis(imageKey: Int, result: ResultData, bitmap: Bitmap) {

        lastImageKey = imageKey
        lastResult = result

        // "My Uploaded Photos" list-ku save pannum (fail aanaalum app crash aagaadhu)
        try {
            LocalPlantPhotoStorage(this).savePhoto(
                bitmap,
                result.plant,
                result.disease
            )
        } catch (e: Exception) {
            Log.w(TAG, "Photo save failed", e)
        }

        openResultPage(result, bitmap)
    }

    // =========================================================
    // OPEN RESULT PAGE (AiAnalysisResultActivity)
    // =========================================================

    private fun openResultPage(result: ResultData, bitmap: Bitmap) {
        val imagePath = saveImageToCache(bitmap)

        val intent = Intent(this, AiAnalysisResultActivity::class.java).apply {
            putExtra("result_status", result.status)
            putExtra("plant_name", result.plant)
            putExtra("disease_name", result.disease)
            putExtra("symptoms", result.symptoms)
            putExtra("severity", result.severity)
            putExtra("image_path", imagePath)
        }
        startActivity(intent)
    }

    private fun saveImageToCache(bitmap: Bitmap): String {
        val file = File(cacheDir, "analysis_image.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file.absolutePath
    }

    // =========================================================
    // SEVERITY
    // =========================================================

    private fun getSeverity(disease: String): String {
        val d = disease.lowercase()

        return when {
            d.contains("bud rot") ||
                    d.contains("bud root dropping") ||
                    d.contains("stem bleeding") ||
                    d.contains("fusarium") ||
                    d.contains("verticillium") ||
                    d.contains("rosette") ||
                    d.contains("tungro") ||
                    d.contains("dead heart") ||
                    d.contains("panicle blight") ||
                    d.contains("blast") ||
                    d.contains("grassy shoot") ||
                    d.contains("viral") ||
                    d.contains("smut") ||
                    d.contains("sett rot") ||
                    d.contains("die back") ||
                    d.contains("xanthomonas") ||
                    d.contains("bacterial canker") ||
                    d.contains("bacterial leaf blight") ||
                    d.contains("bacterial blight") -> "High"

            d.contains("bacterial leaf streak") ||
                    d.contains("brown spot") ||
                    d.contains("downy mildew") ||
                    d.contains("hispa") ||
                    d.contains("sigatoka") ||
                    d.contains("leaf rot") ||
                    d.contains("gray leaf spot") ||
                    d.contains("alternaria") ||
                    d.contains("leaf spot") ||
                    d.contains("rust") ||
                    d.contains("anthracnose") ||
                    d.contains("cutting weevil") ||
                    d.contains("pokkah boeng") ||
                    d.contains("yellow leaf") ||
                    d.contains("dried") -> "Moderate"

            d.contains("powdery mildew") ||
                    d.contains("sooty mould") ||
                    d.contains("gall midge") ||
                    d.contains("banded chlorosis") -> "Low"

            else -> "Moderate"
        }
    }

    // =========================================================
    // SYMPTOMS
    // =========================================================

    private fun getSymptoms(disease: String): String {
        val d = disease.lowercase()

        return when {
            // ---------------- PADDY ----------------
            d.contains("bacterial leaf blight") ->
                "• Water-soaked yellow-green stripes on leaf edges\n" +
                        "• Stripes turn yellow-white and the leaf dries\n" +
                        "• Leaves look scorched, mostly from the tip"

            d.contains("bacterial leaf streak") ->
                "• Thin, dark-green water-soaked streaks between leaf veins\n" +
                        "• Streaks turn brown and join together\n" +
                        "• Leaves dry up in bad cases"

            d.contains("bacterial panicle blight") ->
                "• Grains turn pale, discoloured and do not fill\n" +
                        "• Panicles stay upright and look empty\n" +
                        "• Bottom of the grain looks brown/grey"

            d.contains("blast") ->
                "• Diamond/spindle-shaped spots with grey centre and brown edge\n" +
                        "• Spots join and the whole leaf dries\n" +
                        "• Neck of the panicle may turn black and break"

            d.contains("dead heart") ->
                "• Central shoot turns brown and dries (can be pulled out easily)\n" +
                        "• Young plants wilt and die\n" +
                        "• Caused by stem borer attack"

            d.contains("downy mildew") ->
                "• Pale yellow spots on the upper side of leaves\n" +
                        "• White fluffy growth under the leaf\n" +
                        "• Plant growth is stunted"

            d.contains("hispa") ->
                "• White parallel streaks scraped on leaves\n" +
                        "• Leaves look papery and dry\n" +
                        "• Tiny beetles may be seen on leaves"

            d.contains("tungro") ->
                "• Leaves turn yellow-orange from the tip\n" +
                        "• Plants are short with fewer tillers\n" +
                        "• Spread by green leafhoppers"

            // ---------------- BANANA ----------------
            d.contains("sigatoka") ->
                "• Small yellow streaks on leaves that turn brown\n" +
                        "• Spots grow with grey centre and yellow border\n" +
                        "• Leaves dry early and fruit size reduces"

            d.contains("xanthomonas") ->
                "• Leaves turn yellow and wilt from the top\n" +
                        "• Yellow bacterial ooze when the stem is cut\n" +
                        "• Fruits ripen unevenly and rot inside"

            // ---------------- COCONUT ----------------
            d.contains("bud root dropping") ->
                "• Leaves at the crown turn yellow and droop down\n" +
                        "• Young buds and nuts drop early\n" +
                        "• Roots are weak and the tree looks stressed"

            d.contains("bud rot") ->
                "• Youngest spear leaf turns pale then brown and rots\n" +
                        "• Bad smell from the crown\n" +
                        "• Spear leaf can be pulled out easily"

            d.contains("leaf rot") ->
                "• Brown/black rotting patches on young leaves\n" +
                        "• Leaflets dry and break off\n" +
                        "• Crown looks ragged"

            d.contains("stem bleeding") ->
                "• Reddish-brown liquid oozes from cracks in the trunk\n" +
                        "• Liquid dries to a black crust\n" +
                        "• Leaves turn yellow and nut yield falls"

            // ---------------- COTTON ----------------
            d.contains("alternaria leaf spot") || d == "alternaria leaf" ->
                "• Small round brown spots with rings on leaves\n" +
                        "• Spots join and the leaf dries and falls\n" +
                        "• Lower leaves are affected first"

            d.contains("bacterial blight") ->
                "• Small angular water-soaked spots on leaves\n" +
                        "• Spots turn brown/black and limited by veins\n" +
                        "• Black lesions on stem and bolls may appear"

            d.contains("fusarium") ->
                "• Leaves turn yellow and wilt, one side first\n" +
                        "• Brown streaks inside the stem\n" +
                        "• Whole plant slowly dies"

            d.contains("verticillium") ->
                "• Yellow patches between veins, then brown\n" +
                        "• Leaves wilt and fall early\n" +
                        "• Brown colour inside the stem"

            // ---------------- GROUNDNUT ----------------
            d.contains("rosette") ->
                "• Leaves become small, curled and dark/yellow mottled\n" +
                        "• Plants are very short (dwarf)\n" +
                        "• Very few or no pods"

            d.contains("leaf spot") ->
                "• Small dark brown/black round spots on leaves\n" +
                        "• Yellow ring around spots\n" +
                        "• Leaves fall early and yield reduces"

            // ---------------- MANGO ----------------
            d.contains("anthracnose") ->
                "• Dark brown/black spots on leaves, flowers and fruits\n" +
                        "• Flowers dry and drop\n" +
                        "• Sunken black patches on fruit"

            d.contains("bacterial canker") ->
                "• Small raised water-soaked spots on leaves\n" +
                        "• Spots turn black with yellow border\n" +
                        "• Cracks and oozing on fruit and branches"

            d.contains("cutting weevil") ->
                "• Young leaves look cut or chewed at the base\n" +
                        "• Shoots and leaves fall off\n" +
                        "• Small beetles may be seen on new growth"

            d.contains("die back") ->
                "• Twigs and branches dry from the tip downwards\n" +
                        "• Leaves turn brown and stay on the dry branch\n" +
                        "• Dark gum oozes from the bark"

            d.contains("gall midge") ->
                "• Small raised bumps (galls) on leaves and flowers\n" +
                        "• Leaves curl and become deformed\n" +
                        "• Flowers and young fruit drop"

            d.contains("powdery mildew") ->
                "• White powder-like coating on leaves and flowers\n" +
                        "• Flowers and small fruits fall\n" +
                        "• Leaves curl and dry"

            d.contains("sooty mould") ->
                "• Black soot-like coating on leaves\n" +
                        "• Sticky honeydew from insects below the coating\n" +
                        "• Leaves get less sunlight and growth slows"

            // ---------------- SUGARCANE ----------------
            d.contains("banded chlorosis") ->
                "• Pale yellow/white bands across the leaf\n" +
                        "• Bands are across the leaf width\n" +
                        "• Usually caused by nutrient problems"

            d.contains("brown rust") ->
                "• Small orange-brown pustules on both sides of leaf\n" +
                        "• Pustules join and leaf dries\n" +
                        "• Growth and sugar content reduce"

            d.contains("brown spot") ->
                "• Oval brown spots with yellow ring on leaves\n" +
                        "• Spots increase and leaf dries from the tip\n" +
                        "• Common in humid weather"

            d.contains("dried") ->
                "• Leaves and stalks look dry and straw-coloured\n" +
                        "• Plant is stunted and weak\n" +
                        "• Check water supply, roots and borers"

            d.contains("grassy shoot") ->
                "• Many thin, grass-like shoots from the base\n" +
                        "• Leaves are pale white-yellow\n" +
                        "• No proper cane growth"

            d.contains("pokkah boeng") ->
                "• Twisted, crinkled young leaves at the top\n" +
                        "• Pale patches and red-brown streaks on leaves\n" +
                        "• Top of the cane may rot"

            d.contains("sett rot") ->
                "• Cut sett turns red then black and smells sour\n" +
                        "• Buds do not sprout\n" +
                        "• Young shoots wilt and die"

            d.contains("smut") ->
                "• Long black whip-like shoot from the top\n" +
                        "• Black powder (spores) on the whip\n" +
                        "• Thin canes with many tillers"

            d.contains("viral") ->
                "• Yellow-green mosaic or stripes on leaves\n" +
                        "• Leaves are twisted and plant is stunted\n" +
                        "• Spreads by insects"

            d.contains("yellow leaf") ->
                "• Midrib of the leaf turns yellow\n" +
                        "• Yellowing spreads to the whole leaf\n" +
                        "• Leaf tips dry and cane growth reduces"

            // ---------------- DEFAULT ----------------
            d.contains("rust") ->
                "• Small orange-brown pustules on leaves\n" +
                        "• Yellow spots on the leaf surface\n" +
                        "• Leaves turn yellow and dry"

            else ->
                "• Spots, colour change or drying on leaves\n" +
                        "• Please check the plant closely or consult an agriculture expert"
        }
    }


    // =========================================================
    // PLANTVILLAGE LABEL PARSER
    // "Tomato___Early_blight" => ("Tomato", "Early blight")
    // =========================================================

    private fun parsePlantVillageLabel(raw: String): Pair<String, String> {
        val plantRaw: String
        val diseaseRaw: String

        val parts = raw.split("___")
        if (parts.size >= 2) {
            plantRaw = parts[0]
            diseaseRaw = parts.drop(1).joinToString(" ")
        } else {
            val i = raw.indexOf('_')
            if (i > 0) {
                plantRaw = raw.substring(0, i)
                diseaseRaw = raw.substring(i + 1)
            } else {
                plantRaw = raw
                diseaseRaw = raw
            }
        }

        fun tidy(s: String): String =
            s.replace("_", " ")
                .replace(Regex("\\s+"), " ")
                .trim()
                .replaceFirstChar { it.uppercase() }

        return Pair(tidy(plantRaw), tidy(diseaseRaw))
    }

    // =========================================================
    // SEVERITY (PlantVillage diseases)
    // =========================================================

    private fun getSeverityPV(disease: String): String {
        val d = disease.lowercase()

        return when {
            d.contains("late blight") ||
                    d.contains("black rot") ||
                    d.contains("esca") ||
                    d.contains("black measles") ||
                    d.contains("haunglongbing") ||
                    d.contains("yellow leaf curl") ||
                    d.contains("mosaic") -> "High"

            d.contains("early blight") ||
                    d.contains("bacterial spot") ||
                    d.contains("rust") ||
                    d.contains("scab") ||
                    d.contains("leaf blight") ||
                    d.contains("target spot") ||
                    d.contains("septoria") ||
                    d.contains("leaf mold") ||
                    d.contains("spider mite") ||
                    d.contains("cercospora") ||
                    d.contains("gray leaf spot") -> "Moderate"

            d.contains("powdery mildew") ||
                    d.contains("leaf scorch") -> "Low"

            else -> "Moderate"
        }
    }

    // =========================================================
    // SYMPTOMS (PlantVillage diseases)
    // =========================================================

    private fun getSymptomsPV(disease: String): String {
        val d = disease.lowercase()

        return when {
            d.contains("early blight") ->
                "• Brown spots with concentric rings (target-like) on older leaves\n" +
                        "• Yellow area around the spots\n" +
                        "• Leaves dry up and fall early"

            d.contains("late blight") ->
                "• Large dark, water-soaked patches on leaves\n" +
                        "• White fuzzy growth under the leaf in humid weather\n" +
                        "• Leaves and stems turn brown and rot quickly"

            d.contains("northern leaf blight") ->
                "• Long, cigar-shaped grey-green or tan lesions on leaves\n" +
                        "• Lesions join together and leaves dry out"

            d.contains("leaf blight") ->
                "• Dark brown irregular spots on leaves\n" +
                        "• Spots grow and leaf edges dry up"

            d.contains("gray leaf spot") || d.contains("cercospora") ->
                "• Small grey or tan rectangular spots between the leaf veins\n" +
                        "• Spots join together and leaf dries"

            d.contains("common rust") || d.contains("cedar apple rust") ->
                "• Small orange-brown (rust-coloured) raised pustules on leaves\n" +
                        "• Yellow spots on the leaf surface\n" +
                        "• Leaves turn yellow and dry"

            d.contains("bacterial spot") ->
                "• Small dark, water-soaked spots on leaves\n" +
                        "• Spots turn brown/black with a yellow border\n" +
                        "• Leaves may get holes and fall off"

            d.contains("black rot") ->
                "• Brown circular spots with dark edges on leaves\n" +
                        "• Tiny black dots inside the spots\n" +
                        "• Fruits shrivel and turn black"

            d.contains("esca") || d.contains("black measles") ->
                "• Yellow or red stripes between leaf veins (tiger-stripe look)\n" +
                        "• Dark spots on fruit\n" +
                        "• Leaves dry from the edges"

            d.contains("apple scab") || d.contains("scab") ->
                "• Olive-green to dark brown rough spots on leaves\n" +
                        "• Leaves turn yellow and fall early\n" +
                        "• Scabby spots on fruit"

            d.contains("powdery mildew") ->
                "• White powder-like coating on leaves\n" +
                        "• Leaves curl, turn yellow and dry\n" +
                        "• Growth becomes slow"

            d.contains("leaf mold") ->
                "• Pale green or yellow spots on the upper side of leaves\n" +
                        "• Olive-green or grey mold under the leaves\n" +
                        "• Leaves curl and fall"

            d.contains("septoria") ->
                "• Many small round spots with grey centre and dark edge\n" +
                        "• Lower leaves are affected first\n" +
                        "• Leaves turn yellow and drop"

            d.contains("spider mite") ->
                "• Tiny yellow or white dots on leaves\n" +
                        "• Fine webbing under the leaves\n" +
                        "• Leaves turn bronze/yellow and dry"

            d.contains("target spot") ->
                "• Brown spots with rings like a target on leaves\n" +
                        "• Spots may also appear on stems and fruit\n" +
                        "• Leaves yellow and fall"

            d.contains("yellow leaf curl") ->
                "• Leaves curl upward and turn yellow\n" +
                        "• Leaves become small and thick\n" +
                        "• Plant growth is stunted and fruit is less"

            d.contains("mosaic") ->
                "• Light and dark green mottled (mosaic) patches on leaves\n" +
                        "• Leaves are twisted or distorted\n" +
                        "• Plant growth is stunted"

            d.contains("leaf scorch") ->
                "• Small purple or dark spots on leaves\n" +
                        "• Leaf edges look burnt and dry\n" +
                        "• Leaves turn brown"

            d.contains("haunglongbing") || d.contains("citrus greening") ->
                "• Yellow patches on leaves (blotchy mottling)\n" +
                        "• Fruits stay small, lopsided and green\n" +
                        "• Leaves drop and branches die back"

            else ->
                "• Spots, colour change or drying on leaves\n" +
                        "• Please check the plant closely or consult an agriculture expert"
        }
    }

    override fun onDestroy() {
        diseaseModelCache.values.forEach { it.close() }
        diseaseModelCache.clear()
        cropClassifier?.close()
        featureExtractor?.close()
        super.onDestroy()
    }
}