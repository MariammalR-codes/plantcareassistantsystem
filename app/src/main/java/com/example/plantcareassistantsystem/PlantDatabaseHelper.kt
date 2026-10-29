package com.example.plantcareassistantsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PlantDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "PlantCare.db", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE plants (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT UNIQUE,
                type TEXT,
                water TEXT,
                sunlight TEXT,
                soil TEXT,
                temperature TEXT,
                humidity TEXT,
                fertilizer TEXT,
                fertilizing_frequency TEXT,
                pruning TEXT,
                pot_size TEXT,
                location TEXT,
                growth_period TEXT,
                common_problems TEXT,
                common_pests TEXT,
                propagation TEXT,
                care TEXT
            )
            """.trimIndent()
        )

        // PlantData.kt-ல் இருக்கும் அனைத்து plants-ஐ SQLite-ல் insert செய்யும்
        for (plant in plantList) {
            insertPlant(db, plant)
        }
    }

    private fun insertPlant(
        db: SQLiteDatabase,
        plant: PlantData
    ) {

        val values = ContentValues()

        values.put("name", plant.name)
        values.put("type", plant.type)
        values.put("water", plant.water)
        values.put("sunlight", plant.sunlight)
        values.put("soil", plant.soil)
        values.put("temperature", plant.temperature)
        values.put("humidity", plant.humidity)
        values.put("fertilizer", plant.fertilizer)
        values.put(
            "fertilizing_frequency",
            plant.fertilizingFrequency
        )
        values.put("pruning", plant.pruning)
        values.put("pot_size", plant.potSize)
        values.put("location", plant.location)
        values.put("growth_period", plant.growthPeriod)
        values.put("common_problems", plant.commonProblems)
        values.put("common_pests", plant.commonPests)
        values.put("propagation", plant.propagation)
        values.put("care", plant.care)

        db.insertWithOnConflict(
            "plants",
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS plants")
        onCreate(db)
    }
}