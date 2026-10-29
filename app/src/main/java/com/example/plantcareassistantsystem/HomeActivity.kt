package com.example.plantcareassistantsystem

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var profilePanel: CardView
    private lateinit var uploadedPhotosContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        // ================= HOME BUTTONS =================

        val checkHealthButton =
            findViewById<Button>(R.id.checkHealthButton)

        val aiHealthCard =
            findViewById<CardView>(R.id.aiHealthCard)

        val plantInformationCard =
            findViewById<CardView>(R.id.plantInformationCard)

        val waterReminderCard =
            findViewById<CardView>(R.id.waterReminderCard)

        // ================= PROFILE =================

        val profileButton =
            findViewById<TextView>(R.id.profileButton)

        profilePanel =
            findViewById(R.id.profilePanel)

        val closeProfile =
            findViewById<TextView>(R.id.closeProfile)

        val editProfileButton =
            findViewById<Button>(R.id.editProfileButton)

        val logoutButton =
            findViewById<Button>(R.id.logoutButton)

        uploadedPhotosContainer =
            findViewById(R.id.uploadedPhotosContainer)

        // ================= AI HEALTH =================

        checkHealthButton.setOnClickListener {
            openAIDiseaseDetection()
        }

        aiHealthCard.setOnClickListener {
            openAIDiseaseDetection()
        }

        // ================= PLANT INFORMATION =================

        plantInformationCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    PlantInformationActivity::class.java
                )
            )
        }

        // ================= WATER REMINDER =================

        waterReminderCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    WaterReminderActivity::class.java
                )
            )
        }

        // ================= PROFILE ACTIONS =================

        profileButton.setOnClickListener {
            openProfilePanel()
        }

        closeProfile.setOnClickListener {
            closeProfilePanel()
        }

        editProfileButton.setOnClickListener {
            showEditProfileDialog()
        }

        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    // =====================================================
    // DP -> PX
    // =====================================================

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    // =====================================================
    // AI DISEASE DETECTION
    // =====================================================

    private fun openAIDiseaseDetection() {

        startActivity(
            Intent(
                this,
                AIDiseaseDetectionActivity::class.java
            )
        )
    }

    // =====================================================
    // OPEN PROFILE
    // =====================================================

    private fun openProfilePanel() {

        profilePanel.visibility = View.VISIBLE

        val slideIn = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            1f,

            Animation.RELATIVE_TO_SELF,
            0f,

            Animation.RELATIVE_TO_SELF,
            0f,

            Animation.RELATIVE_TO_SELF,
            0f
        )

        slideIn.duration = 300

        profilePanel.startAnimation(slideIn)

        loadProfileDetails()
        loadUploadedPhotos()
    }

    // =====================================================
    // CLOSE PROFILE
    // =====================================================

    private fun closeProfilePanel() {

        val slideOut = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,

            Animation.RELATIVE_TO_SELF,
            1f,

            Animation.RELATIVE_TO_SELF,
            0f,

            Animation.RELATIVE_TO_SELF,
            0f
        )

        slideOut.duration = 250

        slideOut.setAnimationListener(
            object : Animation.AnimationListener {

                override fun onAnimationStart(
                    animation: Animation?
                ) {
                }

                override fun onAnimationEnd(
                    animation: Animation?
                ) {
                    profilePanel.visibility = View.GONE
                }

                override fun onAnimationRepeat(
                    animation: Animation?
                ) {
                }
            }
        )

        profilePanel.startAnimation(slideOut)
    }

    // =====================================================
    // PROFILE DETAILS
    // =====================================================

    private fun getSavedName(): String {

        val preferences =
            getSharedPreferences(
                "PlantCareProfile",
                MODE_PRIVATE
            )

        return preferences
            .getString("name", null)
            ?.takeIf {
                it.isNotBlank()
            }
            ?: "User Name"
    }

    // =====================================================
    // DISPLAY EMAIL
    // =====================================================

    private fun getDisplayEmail(): String {

        val preferences =
            getSharedPreferences(
                "PlantCareProfile",
                MODE_PRIVATE
            )

        val savedEmail =
            preferences.getString(
                "email",
                null
            )

        if (!savedEmail.isNullOrBlank()) {
            return savedEmail
        }

        return FirebaseAuth
            .getInstance()
            .currentUser
            ?.email
            ?: "user@email.com"
    }

    // =====================================================
    // LOAD PROFILE DETAILS
    // =====================================================

    private fun loadProfileDetails() {

        findViewById<TextView>(
            R.id.profileName
        ).text = getSavedName()

        findViewById<TextView>(
            R.id.profileEmail
        ).text = getDisplayEmail()
    }

    // =====================================================
    // EDIT PROFILE
    // =====================================================

    private fun showEditProfileDialog() {

        val preferences =
            getSharedPreferences(
                "PlantCareProfile",
                MODE_PRIVATE
            )

        val layout =
            LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        layout.setPadding(
            40,
            10,
            40,
            0
        )

        // ================= NAME =================

        val nameInput =
            EditText(this)

        nameInput.hint = "Name"

        nameInput.setText(
            getSavedName()
        )

        layout.addView(nameInput)

        // ================= EMAIL =================

        val emailInput =
            EditText(this)

        emailInput.hint = "Email"

        emailInput.setText(
            getDisplayEmail()
        )

        emailInput.inputType =
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        layout.addView(emailInput)

        // ================= DIALOG =================

        AlertDialog.Builder(this)

            .setTitle("Edit Profile")

            .setView(layout)

            .setNegativeButton(
                "Cancel",
                null
            )

            .setPositiveButton(
                "Save"
            ) { _, _ ->

                val newName =
                    nameInput.text
                        .toString()
                        .trim()

                val newEmail =
                    emailInput.text
                        .toString()
                        .trim()

                if (newName.isNotEmpty()) {

                    preferences
                        .edit()
                        .putString(
                            "name",
                            newName
                        )
                        .putString(
                            "email",
                            newEmail
                        )
                        .apply()

                    loadProfileDetails()
                }
            }

            .show()
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    private fun logoutUser() {

        // ================= CLEAR LOGIN STATE =================

        getSharedPreferences(
            "PlantCarePrefs",
            MODE_PRIVATE
        )
            .edit()
            .putBoolean(
                "isLoggedIn",
                false
            )
            .apply()

        // ================= FIREBASE LOGOUT =================

        FirebaseAuth
            .getInstance()
            .signOut()

        // ================= GO TO LOGIN =================

        val intent =
            Intent(
                this,
                MainActivity::class.java
            )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        finish()
    }

    // =====================================================
    // LOAD UPLOADED PHOTOS
    // =====================================================

    private fun loadUploadedPhotos() {

        uploadedPhotosContainer.removeAllViews()

        val photoStorage =
            LocalPlantPhotoStorage(this)

        val savedPhotos =
            photoStorage.getSavedPhotos()

        // ================= NO PHOTOS =================

        if (savedPhotos.isEmpty()) {

            val emptyText =
                TextView(this)

            emptyText.text =
                "No uploaded photos yet"

            emptyText.textSize = 14f

            emptyText.setTextColor(
                Color.GRAY
            )

            emptyText.gravity =
                Gravity.CENTER_VERTICAL

            emptyText.setPadding(
                dp(2),
                dp(20),
                dp(2),
                dp(20)
            )

            uploadedPhotosContainer
                .addView(emptyText)

            return
        }

        // ================= DISPLAY PHOTOS =================

        savedPhotos.forEach { photo ->

            val photoLayout =
                LinearLayout(this)

            photoLayout.orientation =
                LinearLayout.VERTICAL

            photoLayout.gravity =
                Gravity.CENTER

            photoLayout.setPadding(
                0,
                0,
                dp(10),
                0
            )

            // ================= IMAGE =================

            val imageView =
                ImageView(this)

            imageView.layoutParams =
                LinearLayout.LayoutParams(
                    dp(80),
                    dp(80)
                )

            imageView.scaleType =
                ImageView.ScaleType.CENTER_CROP

            imageView.setBackgroundResource(
                R.drawable.bg_preview
            )

            imageView.clipToOutline = true

            val bitmap =
                photoStorage.getBitmap(
                    photo.fileName
                )

            if (bitmap != null) {
                imageView.setImageBitmap(
                    bitmap
                )
            }

            photoLayout.addView(
                imageView
            )

            // ================= PLANT NAME =================

            val plantText =
                TextView(this)

            plantText.text =
                photo.plantName

            plantText.textSize = 12f

            plantText.setTextColor(
                Color.rgb(
                    23,
                    52,
                    4
                )
            )

            plantText.gravity =
                Gravity.CENTER

            plantText.maxLines = 1

            plantText.setPadding(
                0,
                dp(4),
                0,
                0
            )

            photoLayout.addView(
                plantText
            )

            // ================= CLICK =================

            photoLayout.setOnClickListener {

                showUploadedPhotoDetails(
                    photo,
                    bitmap
                )
            }

            uploadedPhotosContainer
                .addView(photoLayout)
        }
    }

    // =====================================================
    // SHOW PHOTO DETAILS
    // =====================================================

    private fun showUploadedPhotoDetails(
        photo: SavedPlantPhoto,
        bitmap: Bitmap?
    ) {

        val layout =
            LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        layout.setPadding(
            30,
            10,
            30,
            10
        )

        // ================= LARGE IMAGE =================

        if (bitmap != null) {

            val imageView =
                ImageView(this)

            imageView.layoutParams =
                LinearLayout.LayoutParams(
                    600,
                    450
                )

            imageView.scaleType =
                ImageView.ScaleType.CENTER_CROP

            imageView.setImageBitmap(
                bitmap
            )

            layout.addView(
                imageView
            )
        }

        // ================= PLANT =================

        val plantText =
            TextView(this)

        plantText.text =
            "Plant: ${photo.plantName}"

        plantText.textSize = 17f

        plantText.setTextColor(
            Color.rgb(
                35,
                77,
                32
            )
        )

        plantText.setPadding(
            0,
            15,
            0,
            5
        )

        layout.addView(
            plantText
        )

        // ================= DISEASE =================

        val diseaseText =
            TextView(this)

        diseaseText.text =
            "Disease: ${photo.diseaseName}"

        diseaseText.textSize = 15f

        diseaseText.setTextColor(
            Color.DKGRAY
        )

        diseaseText.setPadding(
            0,
            5,
            0,
            5
        )

        layout.addView(
            diseaseText
        )

        // ================= DIALOG =================

        AlertDialog.Builder(this)

            .setTitle(
                "Uploaded Plant Photo"
            )

            .setView(layout)

            .setPositiveButton(
                "Close",
                null
            )

            .show()
    }
}