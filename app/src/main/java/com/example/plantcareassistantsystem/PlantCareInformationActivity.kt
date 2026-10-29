package com.example.plantcareassistantsystem

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PlantCareInformationActivity : AppCompatActivity() {

    private lateinit var database: CareInformationDatabaseHelper

    private lateinit var carePlantName: TextView
    private lateinit var careDiseaseName: TextView

    // Plant Care
    private lateinit var wateringDescription: TextView
    private lateinit var lightDescription: TextView
    private lateinit var temperatureDescription: TextView
    private lateinit var soilDescription: TextView
    private lateinit var fertilizerDescription: TextView
    private lateinit var maintenanceDescription: TextView

    // Disease Care
    private lateinit var symptomsDescription: TextView
    private lateinit var treatmentDescription: TextView
    private lateinit var preventionDescription: TextView
    private lateinit var pestManagementDescription: TextView
    private lateinit var precautionsDescription: TextView

    private lateinit var treatedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_plant_care_information)

        // DATABASE
        database = CareInformationDatabaseHelper(this)

        // FIND VIEWS
        initializeViews()

        // BUTTONS
        setupBackButton()
        setupTreatedButton()

        // EXPANDABLE ROWS
        setupExpandableSections()

        // LOAD CARE INFORMATION
        loadCareInformation()
    }


    // =====================================================
    // INITIALIZE VIEWS
    // =====================================================

    private fun initializeViews() {

        carePlantName = findViewById(R.id.carePlantName)
        careDiseaseName = findViewById(R.id.careDiseaseName)

        // PLANT CARE
        wateringDescription = findViewById(R.id.wateringDescription)
        lightDescription = findViewById(R.id.lightDescription)
        temperatureDescription = findViewById(R.id.temperatureDescription)
        soilDescription = findViewById(R.id.soilDescription)
        fertilizerDescription = findViewById(R.id.fertilizerDescription)
        maintenanceDescription = findViewById(R.id.maintenanceDescription)

        // DISEASE CARE
        symptomsDescription = findViewById(R.id.symptomsDescription)
        treatmentDescription = findViewById(R.id.treatmentDescription)
        preventionDescription = findViewById(R.id.preventionDescription)
        pestManagementDescription = findViewById(R.id.pestManagementDescription)
        precautionsDescription = findViewById(R.id.precautionsDescription)

        // BUTTON
        treatedButton = findViewById(R.id.treatedButton)
    }


    // =====================================================
    // BACK BUTTON
    // =====================================================

    private fun setupBackButton() {

        findViewById<TextView>(R.id.backButton).setOnClickListener {
            finish()
        }
    }


    // =====================================================
    // MARK AS TREATED
    // =====================================================

    private fun setupTreatedButton() {

        treatedButton.setOnClickListener {

            treatedButton.text = "✓  Plant Marked as Treated"
            treatedButton.isEnabled = false

            Toast.makeText(
                this,
                "Plant marked as treated",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    // =====================================================
    // EXPANDABLE SECTIONS
    // =====================================================

    private fun setupExpandableSections() {

        setupExpand(
            findViewById(R.id.fertilizerHeader),
            fertilizerDescription,
            findViewById(R.id.fertilizerArrow)
        )

        setupExpand(
            findViewById(R.id.maintenanceHeader),
            maintenanceDescription,
            findViewById(R.id.maintenanceArrow)
        )

        setupExpand(
            findViewById(R.id.preventionHeader),
            preventionDescription,
            findViewById(R.id.preventionArrow)
        )

        setupExpand(
            findViewById(R.id.pestHeader),
            pestManagementDescription,
            findViewById(R.id.pestArrow)
        )

        setupExpand(
            findViewById(R.id.precautionsHeader),
            precautionsDescription,
            findViewById(R.id.precautionsArrow)
        )
    }

    private fun setupExpand(
        header: View,
        body: View,
        arrow: TextView
    ) {

        header.setOnClickListener {

            val isOpen = body.visibility == View.VISIBLE

            body.visibility = if (isOpen) View.GONE else View.VISIBLE
            arrow.text = if (isOpen) "▾" else "▴"
        }
    }


    // =====================================================
    // LOAD CARE INFORMATION
    // =====================================================

    private fun loadCareInformation() {

        // GET AI RESULT
        val plantName =
            intent.getStringExtra("plant_name")
                ?.trim()
                .orEmpty()

        val diseaseName =
            intent.getStringExtra("disease_name")
                ?.trim()
                .orEmpty()

        // DISPLAY PLANT NAME
        carePlantName.text =
            if (plantName.isNotEmpty()) plantName else "Unknown Plant"

        // DISPLAY DISEASE NAME
        careDiseaseName.text =
            if (diseaseName.isNotEmpty()) diseaseName else "Unknown Condition"

        // LOAD CARE DATA
        loadPlantCare(plantName)
        loadDiseaseCare(diseaseName)
    }


    // =====================================================
    // PLANT CARE
    // =====================================================

    private fun loadPlantCare(plantName: String) {

        if (plantName.isEmpty()) {
            showPlantCareUnavailable()
            return
        }

        val plantCare = database.getPlantCare(plantName)

        if (plantCare != null) {

            wateringDescription.text = plantCare.watering
            lightDescription.text = plantCare.sunlight
            temperatureDescription.text = plantCare.temperature
            soilDescription.text = plantCare.soil
            fertilizerDescription.text = plantCare.fertilizer
            maintenanceDescription.text = plantCare.maintenance

        } else {

            showPlantCareUnavailable()
        }
    }


    // =====================================================
    // DISEASE CARE
    // =====================================================

    private fun loadDiseaseCare(diseaseName: String) {

        if (diseaseName.isEmpty()) {
            showDiseaseCareUnavailable()
            return
        }

        val diseaseCare = database.getDiseaseCare(diseaseName)

        if (diseaseCare != null) {

            symptomsDescription.text = diseaseCare.symptoms
            treatmentDescription.text = diseaseCare.treatment
            preventionDescription.text = diseaseCare.prevention
            pestManagementDescription.text = diseaseCare.pestManagement
            precautionsDescription.text = diseaseCare.precautions

        } else {

            showDiseaseCareUnavailable()
        }
    }


    // =====================================================
    // PLANT CARE FALLBACK
    // =====================================================

    private fun showPlantCareUnavailable() {

        val message = "Plant care information is not available yet."

        wateringDescription.text = message
        lightDescription.text = message
        temperatureDescription.text = message
        soilDescription.text = message
        fertilizerDescription.text = message
        maintenanceDescription.text = message
    }


    // =====================================================
    // DISEASE CARE FALLBACK
    // =====================================================

    private fun showDiseaseCareUnavailable() {

        symptomsDescription.text = "Disease information is not available yet."
        treatmentDescription.text = "Treatment information is not available yet."
        preventionDescription.text = "Prevention information is not available yet."
        pestManagementDescription.text = "Pest management information is not available yet."
        precautionsDescription.text = "Precautions information is not available yet."
    }


    // =====================================================
    // CLOSE DATABASE
    // =====================================================

    override fun onDestroy() {

        database.close()

        super.onDestroy()
    }
}