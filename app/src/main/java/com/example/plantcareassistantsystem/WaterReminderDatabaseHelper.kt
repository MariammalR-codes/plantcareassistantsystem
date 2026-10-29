package com.example.plantcareassistantsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class WaterReminderDatabaseHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        "WaterReminder.db",
        null,
        2
    ) {

    companion object {

        private const val TABLE_NAME =
            "water_reminder"

        private const val DEFAULT_PLANT =
            "Tomato"

        private const val DEFAULT_FREQUENCY =
            2

        // Watering days ippo SupportedPlants.kt la irundhu varum
        fun getPlantFrequency(
            plantName: String
        ): Int {

            return SupportedPlants.frequencyOf(
                plantName
            )
        }
    }


    override fun onCreate(
        db: SQLiteDatabase
    ) {

        db.execSQL(
            """
            CREATE TABLE $TABLE_NAME (
                id INTEGER PRIMARY KEY,
                plant_name TEXT,
                frequency_days INTEGER,
                reminder_hour INTEGER,
                reminder_minute INTEGER,
                reminder_enabled INTEGER,
                last_watered TEXT
            )
            """.trimIndent()
        )


        val values =
            ContentValues().apply {

                put(
                    "id",
                    1
                )

                put(
                    "plant_name",
                    DEFAULT_PLANT
                )

                put(
                    "frequency_days",
                    DEFAULT_FREQUENCY
                )

                put(
                    "reminder_hour",
                    7
                )

                put(
                    "reminder_minute",
                    0
                )

                put(
                    "reminder_enabled",
                    1
                )

                put(
                    "last_watered",
                    "Not watered yet"
                )
            }


        db.insert(
            TABLE_NAME,
            null,
            values
        )
    }


    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL(
            "DROP TABLE IF EXISTS $TABLE_NAME"
        )

        onCreate(db)
    }


    fun getReminder(): ReminderData? {

        val db =
            readableDatabase

        val cursor =
            db.query(
                TABLE_NAME,
                null,
                "id = ?",
                arrayOf("1"),
                null,
                null,
                null
            )

        cursor.use {

            if (it.moveToFirst()) {

                return ReminderData(

                    plantName =
                        it.getString(
                            it.getColumnIndexOrThrow(
                                "plant_name"
                            )
                        ),

                    frequencyDays =
                        it.getInt(
                            it.getColumnIndexOrThrow(
                                "frequency_days"
                            )
                        ),

                    hour =
                        it.getInt(
                            it.getColumnIndexOrThrow(
                                "reminder_hour"
                            )
                        ),

                    minute =
                        it.getInt(
                            it.getColumnIndexOrThrow(
                                "reminder_minute"
                            )
                        ),

                    enabled =
                        it.getInt(
                            it.getColumnIndexOrThrow(
                                "reminder_enabled"
                            )
                        ) == 1,

                    lastWatered =
                        it.getString(
                            it.getColumnIndexOrThrow(
                                "last_watered"
                            )
                        )
                )
            }
        }

        return null
    }


    fun updatePlant(
        plantName: String
    ) {

        val frequency =
            getPlantFrequency(
                plantName
            )

        val values =
            ContentValues().apply {

                put(
                    "plant_name",
                    plantName
                )

                put(
                    "frequency_days",
                    frequency
                )

                // When a new plant is selected,
                // start a fresh watering cycle.
                put(
                    "last_watered",
                    "Not watered yet"
                )
            }


        writableDatabase.update(
            TABLE_NAME,
            values,
            "id = ?",
            arrayOf("1")
        )
    }


    fun updateFrequency(
        frequencyDays: Int
    ) {

        val values =
            ContentValues().apply {

                put(
                    "frequency_days",
                    frequencyDays
                )
            }


        writableDatabase.update(
            TABLE_NAME,
            values,
            "id = ?",
            arrayOf("1")
        )
    }


    fun updateTime(
        hour: Int,
        minute: Int
    ) {

        val values =
            ContentValues().apply {

                put(
                    "reminder_hour",
                    hour
                )

                put(
                    "reminder_minute",
                    minute
                )
            }


        writableDatabase.update(
            TABLE_NAME,
            values,
            "id = ?",
            arrayOf("1")
        )
    }


    fun updateEnabled(
        enabled: Boolean
    ) {

        val values =
            ContentValues().apply {

                put(
                    "reminder_enabled",
                    if (enabled) {
                        1
                    } else {
                        0
                    }
                )
            }


        writableDatabase.update(
            TABLE_NAME,
            values,
            "id = ?",
            arrayOf("1")
        )
    }


    fun updateLastWatered(
        date: String
    ) {

        val values =
            ContentValues().apply {

                put(
                    "last_watered",
                    date
                )
            }


        writableDatabase.update(
            TABLE_NAME,
            values,
            "id = ?",
            arrayOf("1")
        )
    }
}


data class ReminderData(

    val plantName: String,

    val frequencyDays: Int,

    val hour: Int,

    val minute: Int,

    val enabled: Boolean,

    val lastWatered: String
)