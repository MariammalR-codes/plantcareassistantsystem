package com.example.plantcareassistantsystem

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WaterReminderActivity : AppCompatActivity() {

    private lateinit var database: WaterReminderDatabaseHelper

    private lateinit var backButton: TextView
    private lateinit var reminderSwitch: Switch
    private lateinit var changeTimeButton: Button
    private lateinit var wateredButton: Button

    private lateinit var reminderTime: TextView
    private lateinit var lastWatered: TextView
    private lateinit var plantName: TextView
    private lateinit var wateringFrequency: TextView
    private lateinit var nextWatering: TextView
    private lateinit var plantStatus: TextView
    private lateinit var plantSpinner: Spinner

    // Plant list now comes from SupportedPlants.kt
    private val plants = SupportedPlants.names

    private var isSpinnerUpdating = false

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 101
        private const val AI_PLANT_EXTRA = "plant_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_water_reminder)

        database = WaterReminderDatabaseHelper(this)

        initializeViews()
        setupPlantSpinner()
        setupButtons()
        requestNotificationPermission()

        loadReminderData()

        // Check whether this screen was opened from AI detection
        val aiPlantName =
            intent.getStringExtra(AI_PLANT_EXTRA)

        if (!aiPlantName.isNullOrBlank()) {
            selectPlantFromAi(aiPlantName)
        }
    }

    private fun initializeViews() {

        backButton =
            findViewById(R.id.backButton)

        reminderSwitch =
            findViewById(R.id.reminderSwitch)

        changeTimeButton =
            findViewById(R.id.changeTimeButton)

        wateredButton =
            findViewById(R.id.wateredButton)

        reminderTime =
            findViewById(R.id.reminderTime)

        lastWatered =
            findViewById(R.id.lastWatered)

        plantName =
            findViewById(R.id.plantName)

        wateringFrequency =
            findViewById(R.id.wateringFrequency)

        nextWatering =
            findViewById(R.id.nextWatering)

        plantStatus =
            findViewById(R.id.plantStatus)

        plantSpinner =
            findViewById(R.id.plantSpinner)
    }

    private fun setupPlantSpinner() {

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                plants
            )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        plantSpinner.adapter = adapter

        plantSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (isSpinnerUpdating) {
                        return
                    }

                    val selectedPlant =
                        plants[position]

                    updateSelectedPlant(
                        selectedPlant
                    )
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }
    }

    private fun setupButtons() {

        backButton.setOnClickListener {
            finish()
        }

        changeTimeButton.setOnClickListener {
            openTimePicker()
        }

        reminderSwitch.setOnCheckedChangeListener {
                _,
                isChecked ->

            database.updateEnabled(
                isChecked
            )

            if (isChecked) {

                requestNotificationPermission()

                scheduleReminder()

                Toast.makeText(
                    this,
                    "Water reminder enabled",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Water reminder disabled",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        wateredButton.setOnClickListener {

            val today =
                SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.getDefault()
                ).format(Date())

            database.updateLastWatered(
                today
            )

            lastWatered.text =
                "Last watered: $today"

            updateNextWatering()

            if (reminderSwitch.isChecked) {
                scheduleReminder()
            }

            Toast.makeText(
                this,
                "Watered today ✓",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadReminderData() {

        val reminder =
            database.getReminder()
                ?: return

        plantName.text =
            reminder.plantName

        plantStatus.text =
            "Watering plan active"

        wateringFrequency.text =
            "Every ${reminder.frequencyDays} Days"

        reminderTime.text =
            formatTime(
                reminder.hour,
                reminder.minute
            )

        lastWatered.text =
            "Last watered: ${reminder.lastWatered}"

        reminderSwitch.setOnCheckedChangeListener(
            null
        )

        reminderSwitch.isChecked =
            reminder.enabled

        reminderSwitch.setOnCheckedChangeListener {
                _,
                isChecked ->

            database.updateEnabled(
                isChecked
            )

            if (isChecked) {

                requestNotificationPermission()

                scheduleReminder()

                Toast.makeText(
                    this,
                    "Water reminder enabled",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Water reminder disabled",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        selectSpinnerPlant(
            reminder.plantName
        )

        updateNextWatering()
    }

    private fun selectSpinnerPlant(
        plant: String
    ) {

        val position =
            plants.indexOfFirst {
                it.equals(
                    plant,
                    ignoreCase = true
                )
            }

        if (position >= 0) {

            isSpinnerUpdating = true

            plantSpinner.setSelection(
                position
            )

            isSpinnerUpdating = false
        }
    }

    private fun selectPlantFromAi(
        aiPlant: String
    ) {

        // "Rice" -> "Paddy" maari alias names-um handle aagum
        val canonicalPlant =
            SupportedPlants.canonical(aiPlant)

        val position =
            if (canonicalPlant == null) {
                -1
            } else {
                plants.indexOfFirst {
                    it.equals(
                        canonicalPlant,
                        ignoreCase = true
                    )
                }
            }

        if (position == -1) {

            Toast.makeText(
                this,
                "$aiPlant not supported in reminders yet",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        isSpinnerUpdating = true

        plantSpinner.setSelection(
            position
        )

        isSpinnerUpdating = false

        updateSelectedPlant(
            plants[position]
        )

        Toast.makeText(
            this,
            "${plants[position]} selected from AI result",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateSelectedPlant(
        selectedPlant: String
    ) {

        database.updatePlant(
            selectedPlant
        )

        val frequency =
            WaterReminderDatabaseHelper
                .getPlantFrequency(
                    selectedPlant
                )

        plantName.text =
            selectedPlant

        plantStatus.text =
            "Watering plan active"

        wateringFrequency.text =
            "Every $frequency Days"

        val reminder =
            database.getReminder()

        if (reminder != null) {

            lastWatered.text =
                "Last watered: ${reminder.lastWatered}"
        }

        updateNextWatering()

        if (reminderSwitch.isChecked) {
            scheduleReminder()
        }
    }

    private fun openTimePicker() {

        val reminder =
            database.getReminder()

        val currentHour =
            reminder?.hour ?: 7

        val currentMinute =
            reminder?.minute ?: 0

        val timePickerDialog =
            TimePickerDialog(
                this,
                {
                        _,
                        selectedHour,
                        selectedMinute ->

                    database.updateTime(
                        selectedHour,
                        selectedMinute
                    )

                    reminderTime.text =
                        formatTime(
                            selectedHour,
                            selectedMinute
                        )

                    updateNextWatering()

                    if (reminderSwitch.isChecked) {
                        scheduleReminder()
                    }
                },
                currentHour,
                currentMinute,
                false
            )

        timePickerDialog.show()
    }

    private fun formatTime(
        hour: Int,
        minute: Int
    ): String {

        val calendar =
            Calendar.getInstance()

        calendar.set(
            Calendar.HOUR_OF_DAY,
            hour
        )

        calendar.set(
            Calendar.MINUTE,
            minute
        )

        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(calendar.time)
    }

    private fun updateNextWatering() {

        val reminder =
            database.getReminder()
                ?: return

        val frequencyDays =
            reminder.frequencyDays

        val calendar =
            Calendar.getInstance()

        calendar.set(
            Calendar.HOUR_OF_DAY,
            reminder.hour
        )

        calendar.set(
            Calendar.MINUTE,
            reminder.minute
        )

        calendar.set(
            Calendar.SECOND,
            0
        )

        calendar.set(
            Calendar.MILLISECOND,
            0
        )

        val lastWateredText =
            reminder.lastWatered

        if (
            lastWateredText.isBlank() ||
            lastWateredText.equals(
                "Not watered yet",
                ignoreCase = true
            )
        ) {

            if (
                calendar.timeInMillis <=
                System.currentTimeMillis()
            ) {

                calendar.add(
                    Calendar.DAY_OF_YEAR,
                    frequencyDays
                )
            }

        } else {

            try {

                val dateFormat =
                    SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                    )

                val lastDate =
                    dateFormat.parse(
                        lastWateredText
                    )

                if (lastDate != null) {

                    calendar.time =
                        lastDate

                    calendar.set(
                        Calendar.HOUR_OF_DAY,
                        reminder.hour
                    )

                    calendar.set(
                        Calendar.MINUTE,
                        reminder.minute
                    )

                    calendar.set(
                        Calendar.SECOND,
                        0
                    )

                    calendar.set(
                        Calendar.MILLISECOND,
                        0
                    )

                    calendar.add(
                        Calendar.DAY_OF_YEAR,
                        frequencyDays
                    )

                    while (
                        calendar.timeInMillis <=
                        System.currentTimeMillis()
                    ) {

                        calendar.add(
                            Calendar.DAY_OF_YEAR,
                            frequencyDays
                        )
                    }
                }

            } catch (e: Exception) {

                e.printStackTrace()

                calendar.add(
                    Calendar.DAY_OF_YEAR,
                    frequencyDays
                )
            }
        }

        val dateFormat =
            SimpleDateFormat(
                "dd MMM",
                Locale.getDefault()
            )

        val dateText =
            dateFormat.format(
                calendar.time
            )

        val timeText =
            formatTime(
                reminder.hour,
                reminder.minute
            )

        nextWatering.text =
            "Next watering • $dateText, $timeText"
    }

    private fun scheduleReminder() {

        val reminder =
            database.getReminder()
                ?: return

        if (!reminder.enabled) {
            return
        }

        try {

            WaterReminderReceiver
                .scheduleReminder(this)

        } catch (e: Exception) {

            e.printStackTrace()

            Toast.makeText(
                this,
                "Could not schedule reminder",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun requestNotificationPermission() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onDestroy() {

        database.close()

        super.onDestroy()
    }
}