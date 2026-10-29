package com.example.plantcareassistantsystem

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

data class SavedPlantPhoto(
    val fileName: String,
    val plantName: String,
    val diseaseName: String,
    val timestamp: Long
)

class LocalPlantPhotoStorage(
    private val context: Context
) {

    private val preferences =
        context.getSharedPreferences(
            "PlantPhotoStorage",
            Context.MODE_PRIVATE
        )

    private fun getUserKey(): String {

        val userId =
            com.google.firebase.auth.FirebaseAuth
                .getInstance()
                .currentUser
                ?.uid
                ?: "guest"

        return userId
    }

    private fun getPhotoDirectory(): File {

        val directory =
            File(
                context.filesDir,
                "plant_photos/${getUserKey()}"
            )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        return directory
    }

    private fun getPreferenceKey(): String {

        return "photos_${getUserKey()}"
    }

    fun savePhoto(
        bitmap: Bitmap,
        plantName: String,
        diseaseName: String
    ) {

        val timestamp =
            System.currentTimeMillis()

        val fileName =
            "plant_$timestamp.jpg"

        val photoFile =
            File(
                getPhotoDirectory(),
                fileName
            )

        FileOutputStream(
            photoFile
        ).use { outputStream ->

            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                outputStream
            )
        }

        val photos =
            getSavedPhotos().toMutableList()

        photos.add(
            SavedPlantPhoto(
                fileName = fileName,
                plantName = plantName,
                diseaseName = diseaseName,
                timestamp = timestamp
            )
        )

        savePhotoList(photos)
    }

    fun getSavedPhotos(): List<SavedPlantPhoto> {

        val jsonString =
            preferences.getString(
                getPreferenceKey(),
                "[]"
            ) ?: "[]"

        val result =
            mutableListOf<SavedPlantPhoto>()

        try {

            val jsonArray =
                JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {

                val item =
                    jsonArray.getJSONObject(i)

                val fileName =
                    item.getString(
                        "fileName"
                    )

                val file =
                    File(
                        getPhotoDirectory(),
                        fileName
                    )

                if (!file.exists()) {
                    continue
                }

                result.add(
                    SavedPlantPhoto(
                        fileName = fileName,
                        plantName =
                            item.getString(
                                "plantName"
                            ),
                        diseaseName =
                            item.getString(
                                "diseaseName"
                            ),
                        timestamp =
                            item.getLong(
                                "timestamp"
                            )
                    )
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()
        }

        return result.sortedByDescending {
            it.timestamp
        }
    }

    fun getBitmap(
        fileName: String
    ): Bitmap? {

        val file =
            File(
                getPhotoDirectory(),
                fileName
            )

        if (!file.exists()) {
            return null
        }

        return BitmapFactory.decodeFile(
            file.absolutePath
        )
    }

    private fun savePhotoList(
        photos: List<SavedPlantPhoto>
    ) {

        val jsonArray =
            JSONArray()

        photos.forEach { photo ->

            val jsonObject =
                JSONObject()

            jsonObject.put(
                "fileName",
                photo.fileName
            )

            jsonObject.put(
                "plantName",
                photo.plantName
            )

            jsonObject.put(
                "diseaseName",
                photo.diseaseName
            )

            jsonObject.put(
                "timestamp",
                photo.timestamp
            )

            jsonArray.put(
                jsonObject
            )
        }

        preferences.edit()
            .putString(
                getPreferenceKey(),
                jsonArray.toString()
            )
            .apply()
    }
}

