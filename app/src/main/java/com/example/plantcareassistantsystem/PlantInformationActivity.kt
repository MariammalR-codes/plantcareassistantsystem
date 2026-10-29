package com.example.plantcareassistantsystem

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class PlantInformationActivity : AppCompatActivity() {

    private lateinit var plantSearch: EditText
    private lateinit var searchButton: Button
    private lateinit var cameraButton: TextView

    private lateinit var plantPreview: ImageView

    private lateinit var infoTitle: TextView
    private lateinit var infoCard: View

    private lateinit var plantName: TextView
    private lateinit var plantType: TextView
    private lateinit var plantWater: TextView
    private lateinit var plantSunlight: TextView
    private lateinit var plantSoil: TextView

    private lateinit var plantTemperature: TextView
    private lateinit var plantHumidity: TextView
    private lateinit var plantFertilizer: TextView
    private lateinit var plantFertilizingFrequency: TextView
    private lateinit var plantPruning: TextView
    private lateinit var plantPotSize: TextView
    private lateinit var plantLocation: TextView
    private lateinit var plantGrowthPeriod: TextView
    private lateinit var plantCommonProblems: TextView
    private lateinit var plantCommonPests: TextView
    private lateinit var plantPropagation: TextView

    private lateinit var careDescription: TextView

    private lateinit var databaseHelper: PlantDatabaseHelper

    companion object {
        private const val PICK_IMAGE_REQUEST = 1001
        private const val CAMERA_REQUEST = 2002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_plant_information
        )

        // ====================================
        // Initialize SQLite Database
        // ====================================

        databaseHelper =
            PlantDatabaseHelper(this)


        // ====================================
        // Find Views
        // ====================================

        val backButton =
            findViewById<TextView>(
                R.id.backButton
            )

        plantSearch =
            findViewById<EditText>(
                R.id.plantSearch
            )

        searchButton =
            findViewById<Button>(
                R.id.searchButton
            )
        cameraButton =
            findViewById<TextView>(
                R.id.cameraButton
            )

        plantPreview =
            findViewById<ImageView>(
                R.id.plantPreview
            )

        infoTitle =
            findViewById<TextView>(
                R.id.infoTitle
            )

        infoCard =
            findViewById<View>(
                R.id.infoCard
            )

        plantName =
            findViewById<TextView>(
                R.id.plantName
            )

        plantType =
            findViewById<TextView>(
                R.id.plantType
            )

        plantWater =
            findViewById<TextView>(
                R.id.plantWater
            )

        plantSunlight =
            findViewById<TextView>(
                R.id.plantSunlight
            )

        plantSoil =
            findViewById<TextView>(
                R.id.plantSoil
            )

        plantTemperature =
            findViewById<TextView>(
                R.id.plantTemperature
            )

        plantHumidity =
            findViewById<TextView>(
                R.id.plantHumidity
            )

        plantFertilizer =
            findViewById<TextView>(
                R.id.plantFertilizer
            )

        plantFertilizingFrequency =
            findViewById<TextView>(
                R.id.plantFertilizingFrequency
            )

        plantPruning =
            findViewById<TextView>(
                R.id.plantPruning
            )

        plantPotSize =
            findViewById<TextView>(
                R.id.plantPotSize
            )

        plantLocation =
            findViewById<TextView>(
                R.id.plantLocation
            )

        plantGrowthPeriod =
            findViewById<TextView>(
                R.id.plantGrowthPeriod
            )

        plantCommonProblems =
            findViewById<TextView>(
                R.id.plantCommonProblems
            )

        plantCommonPests =
            findViewById<TextView>(
                R.id.plantCommonPests
            )

        plantPropagation =
            findViewById<TextView>(
                R.id.plantPropagation
            )

        careDescription =
            findViewById<TextView>(
                R.id.careDescription
            )


        // ====================================
        // Back Button
        // ====================================

        backButton.setOnClickListener {

            finish()
        }


        // ====================================
        // Search Button
        // ====================================

        searchButton.setOnClickListener {

            val name =
                plantSearch.text
                    .toString()
                    .trim()

            if (name.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please enter a plant name",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                showPlantInformation(name)
            }
        }


        // ====================================
        // Keyboard Search
        // ====================================

        plantSearch.setOnEditorActionListener {

                _, _, _ ->

            val name =
                plantSearch.text
                    .toString()
                    .trim()

            if (name.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please enter a plant name",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                showPlantInformation(name)
            }

            true
        }


        // ====================================
        // Camera Icon
        // ====================================

        cameraButton.setOnClickListener {

            showPhotoOptions()
        }
    }


    // ====================================
    // Photo Options
    // ====================================

    private fun showPhotoOptions() {

        val options = arrayOf(
            "📸 Take Photo",
            "🖼️ Choose from Gallery"
        )

        AlertDialog.Builder(this)
            .setTitle("Select Plant Photo")
            .setItems(options) { _, which ->

                when (which) {

                    0 -> openCamera()

                    1 -> openImagePicker()
                }
            }
            .show()
    }


    // ====================================
    // Open Camera
    // ====================================

    private fun openCamera() {

        val intent =
            Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
            )

        if (
            intent.resolveActivity(
                packageManager
            ) != null
        ) {

            startActivityForResult(
                intent,
                CAMERA_REQUEST
            )

        } else {

            Toast.makeText(
                this,
                "Camera not available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    // ====================================
    // Open Gallery
    // ====================================

    private fun openImagePicker() {

        val intent =
            Intent(
                Intent.ACTION_OPEN_DOCUMENT
            )

        intent.addCategory(
            Intent.CATEGORY_OPENABLE
        )

        intent.type = "image/*"

        startActivityForResult(
            intent,
            PICK_IMAGE_REQUEST
        )
    }


    // ====================================
    // Search Plant From SQLite
    // ====================================

    private fun showPlantInformation(
        searchName: String
    ) {

        val normalizedName =
            searchName
                .trim()
                .lowercase()


        // ====================================
        // Common Name Mapping
        // ====================================

        val searchValue = when {

            normalizedName.contains("rose") ->
                "Rose"

            normalizedName.contains("tulsi") ||
                    normalizedName.contains("holy basil") ->
                "Tulsi"

            normalizedName.contains("aloe") ->
                "Aloe Vera"

            normalizedName.contains("tomato") ->
                "Tomato"

            normalizedName.contains("money plant") ||
                    normalizedName.contains("pothos") ->
                "Money Plant"

            else ->
                searchName.trim()
        }


        // ====================================
        // Open SQLite Database
        // ====================================

        val db =
            databaseHelper.readableDatabase


        // ====================================
        // Search Database
        // ====================================

        val cursor: Cursor =
            db.query(

                "plants",

                arrayOf(
                    "name",
                    "type",
                    "water",
                    "sunlight",
                    "soil",
                    "temperature",
                    "humidity",
                    "fertilizer",
                    "fertilizing_frequency",
                    "pruning",
                    "pot_size",
                    "location",
                    "growth_period",
                    "common_problems",
                    "common_pests",
                    "propagation",
                    "care"
                ),

                "LOWER(name) = LOWER(?)",

                arrayOf(
                    searchValue
                ),

                null,
                null,
                null
            )


        // ====================================
        // Plant Found
        // ====================================

        if (cursor.moveToFirst()) {

            val name =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "name"
                    )
                )

            val type =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "type"
                    )
                )

            val water =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "water"
                    )
                )

            val sunlight =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "sunlight"
                    )
                )

            val soil =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "soil"
                    )
                )

            val temperature =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "temperature"
                    )
                )

            val humidity =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "humidity"
                    )
                )

            val fertilizer =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "fertilizer"
                    )
                )

            val fertilizingFrequency =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "fertilizing_frequency"
                    )
                )

            val pruning =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "pruning"
                    )
                )

            val potSize =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "pot_size"
                    )
                )

            val location =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "location"
                    )
                )

            val growthPeriod =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "growth_period"
                    )
                )

            val commonProblems =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "common_problems"
                    )
                )

            val commonPests =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "common_pests"
                    )
                )

            val propagation =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "propagation"
                    )
                )

            val care =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "care"
                    )
                )


            // ====================================
            // Display Information
            // ====================================

            displayInformation(

                name,

                "Type: $type",

                "💧 Water: $water",

                "☀️ Sunlight: $sunlight",

                "🌱 Soil: $soil",

                "🌡️ Temperature: $temperature",

                "💦 Humidity: $humidity",

                "🌿 Fertilizer: $fertilizer",

                "🧴 Fertilizing Frequency: $fertilizingFrequency",

                "✂️ Pruning: $pruning",

                "🪴 Pot Size: $potSize",

                "📍 Location: $location",

                "🌱 Growth Period: $growthPeriod",

                "⚠️ Common Problems: $commonProblems",

                "🐛 Common Pests: $commonPests",

                "🌱 Propagation: $propagation",

                care
            )

        } else {

            // ====================================
            // Plant Not Found
            // ====================================

            Toast.makeText(
                this,
                "Plant information not found",
                Toast.LENGTH_SHORT
            ).show()

            infoTitle.visibility =
                View.GONE

            infoCard.visibility =
                View.GONE
        }


        cursor.close()

        db.close()
    }


    // ====================================
    // Display Plant Information
    // ====================================

    private fun displayInformation(

        name: String,

        type: String,

        water: String,

        sunlight: String,

        soil: String,

        temperature: String,

        humidity: String,

        fertilizer: String,

        fertilizingFrequency: String,

        pruning: String,

        potSize: String,

        location: String,

        growthPeriod: String,

        commonProblems: String,

        commonPests: String,

        propagation: String,

        care: String

    ) {

        plantName.text =
            name

        plantType.text =
            type

        plantWater.text =
            water

        plantSunlight.text =
            sunlight

        plantSoil.text =
            soil

        plantTemperature.text =
            temperature

        plantHumidity.text =
            humidity

        plantFertilizer.text =
            fertilizer

        plantFertilizingFrequency.text =
            fertilizingFrequency

        plantPruning.text =
            pruning

        plantPotSize.text =
            potSize

        plantLocation.text =
            location

        plantGrowthPeriod.text =
            growthPeriod

        plantCommonProblems.text =
            commonProblems

        plantCommonPests.text =
            commonPests

        plantPropagation.text =
            propagation

        careDescription.text =
            care


        infoTitle.visibility =
            View.VISIBLE

        infoCard.visibility =
            View.VISIBLE
    }


    // ====================================
    // Receive Camera / Gallery Result
    // ====================================

    @Deprecated("Deprecated in Android API")
    override fun onActivityResult(

        requestCode: Int,

        resultCode: Int,

        data: Intent?

    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )


        // ====================================
        // Gallery Photo
        // ====================================

        if (
            requestCode ==
            PICK_IMAGE_REQUEST &&
            resultCode ==
            Activity.RESULT_OK
        ) {

            val imageUri: Uri? =
                data?.data

            if (imageUri != null) {

                plantPreview.setImageURI(
                    imageUri
                )

                plantPreview.visibility =
                    View.VISIBLE

                Toast.makeText(
                    this,
                    "Plant photo selected 🌿",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        // ====================================
        // Camera Photo
        // ====================================

        if (
            requestCode ==
            CAMERA_REQUEST &&
            resultCode ==
            Activity.RESULT_OK
        ) {

            val bitmap =
                data?.extras?.get(
                    "data"
                ) as? android.graphics.Bitmap

            if (bitmap != null) {

                plantPreview.setImageBitmap(
                    bitmap
                )

                plantPreview.visibility =
                    View.VISIBLE

                Toast.makeText(
                    this,
                    "Plant photo captured 📸",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    // ====================================
    // Close Database
    // ====================================

    override fun onDestroy() {

        databaseHelper.close()

        super.onDestroy()
    }
}