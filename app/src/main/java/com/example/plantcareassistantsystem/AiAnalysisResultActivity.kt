package com.example.plantcareassistantsystem

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AiAnalysisResultActivity : AppCompatActivity() {

    // ---------------- UI ----------------

    private lateinit var resultPlantImage: ImageView
    private lateinit var detectedPlantText: TextView
    private lateinit var detectedDiseaseText: TextView
    private lateinit var symptomsText: TextView
    private lateinit var severityText: TextView

    private lateinit var viewCareTipsButton: Button
    private lateinit var setWaterReminderButton: Button
    private lateinit var backButton: TextView

    // ---------------- RESULT DATA ----------------

    private var plantName = ""
    private var diseaseName = ""
    private var symptoms = ""
    private var severity = ""

    private var photoFileName = ""
    private var imagePath = ""

    private var resultStatus = ""

    // =========================================================
    // ON CREATE
    // =========================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_ai_analysis_result
        )

        initialiseViews()

        readIntentData()

        displayResult()

        setupButtons()
    }

    // =========================================================
    // INITIALISE VIEWS
    // =========================================================

    private fun initialiseViews() {

        resultPlantImage =
            findViewById(R.id.resultPlantImage)

        detectedPlantText =
            findViewById(R.id.detectedPlantText)

        detectedDiseaseText =
            findViewById(R.id.detectedDiseaseText)

        symptomsText =
            findViewById(R.id.symptomsText)

        severityText =
            findViewById(R.id.severityText)

        viewCareTipsButton =
            findViewById(R.id.viewCareTipsButton)

        setWaterReminderButton =
            findViewById(R.id.setWaterReminderButton)

        backButton =
            findViewById(R.id.backButton)
    }

    // =========================================================
    // READ INTENT DATA
    // =========================================================

    private fun readIntentData() {

        plantName =
            intent.getStringExtra(
                "plant_name"
            ) ?: "Unknown Plant"

        diseaseName =
            intent.getStringExtra(
                "disease_name"
            ) ?: "Unknown Disease"

        symptoms =
            intent.getStringExtra(
                "symptoms"
            ) ?: "No symptoms available."

        severity =
            intent.getStringExtra(
                "severity"
            ) ?: "Unknown"

        photoFileName =
            intent.getStringExtra(
                "photo_file_name"
            ) ?: ""

        imagePath =
            intent.getStringExtra(
                "image_path"
            ) ?: ""

        resultStatus =
            intent.getStringExtra(
                "result_status"
            ) ?: "DISEASE"
    }

    // =========================================================
    // DISPLAY RESULT
    // =========================================================

    private fun displayResult() {

        detectedPlantText.text =
            "Plant: $plantName"

        detectedDiseaseText.text =
            "Disease: $diseaseName"

        symptomsText.text =
            "Symptoms: $symptoms"

        severityText.text =
            "Severity: $severity"

        // =====================================================
        // HEALTHY RESULT
        // =====================================================

        when (resultStatus) {

            "HEALTHY" -> {

                detectedDiseaseText.setTextColor(
                    Color.parseColor("#2E7D32")
                )

                severityText.setTextColor(
                    Color.parseColor("#2E7D32")
                )

                severityText.text =
                    "Severity: None"
            }

            // =================================================
            // INVALID RESULT
            // =================================================

            "INVALID" -> {

                detectedDiseaseText.setTextColor(
                    Color.parseColor("#EF6C00")
                )

                severityText.setTextColor(
                    Color.parseColor("#757575")
                )

                viewCareTipsButton.visibility =
                    View.GONE

                setWaterReminderButton.visibility =
                    View.GONE
            }

            // =================================================
            // DISEASE RESULT
            // =================================================

            else -> {

                val colour = when (severity) {

                    "High" ->
                        "#C62828"

                    "Moderate" ->
                        "#E65100"

                    "Low" ->
                        "#F9A825"

                    else ->
                        "#59675C"
                }

                severityText.setTextColor(
                    Color.parseColor(colour)
                )

                detectedDiseaseText.setTextColor(
                    Color.parseColor("#C62828")
                )
            }
        }

        // =====================================================
        // LOAD IMAGE
        // =====================================================

        if (imagePath.isNotBlank()) {

            val bitmap =
                BitmapFactory.decodeFile(
                    imagePath
                )

            if (bitmap != null) {

                resultPlantImage.setImageBitmap(
                    bitmap
                )
            }

        } else if (photoFileName.isNotBlank()) {

            val storage =
                LocalPlantPhotoStorage(this)

            val bitmap =
                storage.getBitmap(
                    photoFileName
                )

            if (bitmap != null) {

                resultPlantImage.setImageBitmap(
                    bitmap
                )
            }
        }
    }

    // =========================================================
    // BUTTONS
    // =========================================================

    private fun setupButtons() {

        // -----------------------------------------------------
        // BACK
        // -----------------------------------------------------

        backButton.setOnClickListener {

            finish()
        }

        // -----------------------------------------------------
        // CARE TIPS
        // -----------------------------------------------------

        viewCareTipsButton.setOnClickListener {

            val intent =
                Intent(
                    this,
                    PlantCareInformationActivity::class.java
                )

            intent.putExtra(
                "plant_name",
                plantName
            )

            intent.putExtra(
                "disease_name",
                diseaseName
            )

            startActivity(intent)
        }

        // -----------------------------------------------------
        // WATER REMINDER
        // -----------------------------------------------------

        setWaterReminderButton.setOnClickListener {

            // Supported plant list now comes from SupportedPlants.kt
            val matchedPlant =
                SupportedPlants.canonical(
                    plantName
                )

            if (matchedPlant == null) {

                Toast.makeText(
                    this,
                    "Water reminder is not available for this plant.",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            val intent =
                Intent(
                    this,
                    WaterReminderActivity::class.java
                )

            intent.putExtra(
                "plant_name",
                matchedPlant
            )

            startActivity(intent)
        }
    }
}