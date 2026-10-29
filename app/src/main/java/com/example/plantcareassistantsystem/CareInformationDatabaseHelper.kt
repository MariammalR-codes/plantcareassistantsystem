package com.example.plantcareassistantsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// DATABASE VERSION 4: pudhu plant / disease data add pannum bodhu
// ovvoru thadavaiyum idha increase pannunga, appo onUpgrade run aagi data refresh aagum.
class CareInformationDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "CareInformation.db", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE plant_care (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                plant_name TEXT UNIQUE,
                watering TEXT,
                sunlight TEXT,
                temperature TEXT,
                soil TEXT,
                fertilizer TEXT,
                maintenance TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE disease_care (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                disease_name TEXT UNIQUE,
                symptoms TEXT,
                treatment TEXT,
                prevention TEXT,
                pest_management TEXT,
                precautions TEXT
            )
            """.trimIndent()
        )

        // ---------------- PLANT CARE DATA ----------------

        insertPlantCare(
            db, "Tomato",
            "Water at the base of the plant when the top layer of soil begins to dry. Avoid overwatering.",
            "Provide around 6 to 8 hours of sunlight daily.",
            "Tomatoes generally grow well in warm conditions. Avoid frost and extreme heat stress.",
            "Use well-drained, fertile soil with good organic matter.",
            "Use a balanced fertilizer during early growth and a suitable tomato fertilizer during flowering and fruiting.",
            "Remove damaged leaves, support the plant when needed and regularly check leaves and stems for problems."
        )

        insertPlantCare(
            db, "Potato",
            "Keep the soil evenly moist but avoid waterlogging.",
            "Provide good sunlight for healthy growth.",
            "Cool to moderate growing conditions are generally suitable.",
            "Use loose, well-drained soil that allows tubers to develop properly.",
            "Provide balanced nutrients according to the growth stage.",
            "Remove damaged foliage and regularly inspect the plant for disease and pest symptoms."
        )

        insertPlantCare(
            db, "Apple",
            "Water deeply when the soil begins to dry, especially during dry periods.",
            "Provide full sunlight and good air circulation.",
            "Protect young plants from extreme heat and frost when necessary.",
            "Use well-drained fertile soil with suitable organic matter.",
            "Apply appropriate fruit-tree fertilizer according to plant growth and soil conditions.",
            "Prune damaged or overcrowded branches and regularly inspect leaves and fruit."
        )

        insertPlantCare(
            db, "Corn",
            "Maintain consistent soil moisture, especially during flowering and grain development.",
            "Provide full sunlight for healthy growth.",
            "Warm growing conditions are generally suitable.",
            "Use fertile, well-drained soil with adequate organic matter.",
            "Provide balanced nutrients, with attention to nitrogen requirements during active growth.",
            "Remove heavily damaged leaves and monitor regularly for pests and disease."
        )

        insertPlantCare(
            db, "Grape",
            "Water deeply when needed and avoid continuously wet soil.",
            "Provide full sunlight and good air circulation.",
            "Protect the plant from extreme temperature conditions.",
            "Use well-drained soil with good drainage and organic matter.",
            "Provide suitable nutrients during active growth and fruit development.",
            "Prune appropriately, support vines and inspect leaves and fruit regularly."
        )

        insertPlantCare(
            db, "Paddy",
            "Keep the field with a thin layer of standing water during most growth stages and drain before harvest.",
            "Provide full sunlight for healthy tillering and grain filling.",
            "Warm and humid conditions are generally suitable.",
            "Use fertile clay or clay-loam soil that can hold water.",
            "Apply balanced nitrogen, phosphorus and potassium according to growth stage.",
            "Control weeds regularly, maintain water level and inspect leaves for disease and pests."
        )

        // PlantData.kt la irukkura ellaa plants-kum care info automatic ah insert aagum.
        // Mela thaniya kudutha plants (Tomato, Potato, Paddy...) munnadiye insert aagiduchu,
        // so avanga data override aagaadhu.
        insertAllPlantsFromList(db)

        // ---------------- DISEASE CARE DATA ----------------

        insertDiseaseCare(
            db, "Early Blight",
            "Brown or dark leaf spots, yellowing and progressive leaf damage.",
            "Remove severely affected leaves and dispose of infected plant material. Avoid prolonged leaf wetness.",
            "Maintain good spacing, improve air circulation and keep foliage dry when possible.",
            "Monitor regularly for insect damage and other stresses that can weaken the plant.",
            "Avoid spreading infected plant material and clean gardening tools after handling affected plants."
        )

        insertDiseaseCare(
            db, "Late Blight",
            "Dark water-soaked lesions, rapid browning and damaged leaves or stems.",
            "Remove severely affected plant material and keep infected debris away from healthy plants.",
            "Avoid prolonged leaf moisture, maintain good spacing and provide good airflow.",
            "Inspect plants regularly for signs of insects and other stresses.",
            "Do not move infected plant material to healthy areas and disinfect tools after use."
        )

        insertDiseaseCare(
            db, "Bacterial Spot",
            "Small dark spots or lesions on leaves and fruit.",
            "Remove severely affected leaves and avoid working with plants when foliage is wet.",
            "Use clean gardening tools, maintain spacing and avoid unnecessary leaf wetness.",
            "Regularly inspect plants for insect damage that may increase plant stress.",
            "Remove infected plant debris and sanitize tools after handling affected plants."
        )

        insertDiseaseCare(
            db, "Powdery Mildew",
            "White powder-like growth on leaves and sometimes stems.",
            "Remove severely affected leaves and improve air circulation around the plant.",
            "Avoid overcrowding and maintain good ventilation.",
            "Monitor regularly for pests and plant stress.",
            "Remove infected plant material and keep the growing area clean."
        )

        insertDiseaseCare(
            db, "Black Rot",
            "Dark lesions, leaf yellowing and damaged plant tissue.",
            "Remove affected plant parts and dispose of infected material properly.",
            "Keep foliage dry, clean fallen leaves and avoid overcrowding.",
            "Regularly inspect plants for insects and other sources of damage.",
            "Clean gardening tools and remove infected debris from the growing area."
        )

        insertDiseaseCare(
            db, "Leaf Mold",
            "Yellowish leaf spots and mold-like growth, especially under humid conditions.",
            "Remove affected leaves and improve ventilation around the plant.",
            "Reduce excessive humidity and provide sufficient spacing.",
            "Monitor the plant regularly for insects and other stresses.",
            "Keep the growing area clean and avoid prolonged moisture on foliage."
        )

        insertDiseaseCare(
            db, "Septoria",
            "Small circular leaf spots that may have dark borders.",
            "Remove affected leaves and keep infected plant debris away from healthy plants.",
            "Maintain good spacing and avoid prolonged leaf wetness.",
            "Inspect the plant regularly for pests and other damage.",
            "Clean fallen leaves and sanitize gardening tools."
        )

        insertDiseaseCare(
            db, "Spider Mites",
            "Tiny spots, yellowing leaves and fine webbing may appear on affected plants.",
            "Inspect the underside of leaves and remove heavily affected leaves. A gentle water spray can help dislodge mites.",
            "Maintain consistent watering and reduce severe plant stress.",
            "Regularly inspect leaf undersides and monitor for mite activity.",
            "Avoid allowing plants to become severely dry and remove heavily infested plant material."
        )

        insertDiseaseCare(
            db, "Yellow Leaf Curl Virus",
            "Leaf curling, yellowing and reduced plant growth.",
            "Remove severely affected plants or plant parts where appropriate and manage insect vectors.",
            "Monitor regularly and control whiteflies where present.",
            "Inspect plants frequently for whiteflies and other insect vectors.",
            "Remove infected plant material promptly and avoid propagating from infected plants."
        )

        insertDiseaseCare(
            db, "Mosaic Virus",
            "Mosaic-like light and dark green patterns, leaf distortion and reduced growth.",
            "Remove severely infected plant material and disinfect tools after handling affected plants.",
            "Control insect vectors and avoid propagating from infected plants.",
            "Monitor for insects that may spread plant viruses.",
            "Keep tools clean and remove infected plants or plant parts promptly."
        )

        insertDiseaseCare(
            db, "Target Spot",
            "Circular target-like spots may develop on affected leaves.",
            "Remove severely affected leaves and improve airflow around the plant.",
            "Avoid prolonged leaf wetness and maintain adequate spacing.",
            "Monitor plants regularly for insects and other stress factors.",
            "Remove infected plant debris and keep the growing area clean."
        )

        insertDiseaseCare(
            db, "Leaf Blight",
            "Brown lesions, leaf discoloration and drying of affected foliage.",
            "Remove affected leaves and dispose of infected plant material properly.",
            "Maintain good spacing, keep foliage dry and remove fallen leaves.",
            "Inspect plants regularly for pests and other sources of damage.",
            "Clean gardening tools and remove infected debris."
        )

        insertDiseaseCare(
            db, "Cedar Apple Rust",
            "Yellow-orange spots or rust-like symptoms may appear on apple leaves.",
            "Remove severely affected leaves and monitor the plant for further symptoms.",
            "Maintain good airflow and remove infected plant debris.",
            "Inspect the plant regularly for insects and other stresses.",
            "Keep the growing area clean and remove heavily affected plant material."
        )

        insertDiseaseCare(
            db, "Apple Scab",
            "Dark or olive-colored spots may develop on leaves and fruit.",
            "Remove affected leaves and fallen infected debris from around the plant.",
            "Keep the growing area clean, improve airflow and avoid prolonged leaf wetness.",
            "Monitor plants regularly for pests and damage.",
            "Remove fallen leaves and sanitize gardening tools."
        )

        // ---------------- PADDY DISEASES ----------------
        // "Bacterial Leaf Blight" ku thani entry irukanum. Illana generic
        // "Leaf Blight" info thaan varum (longest match rule).

        insertDiseaseCare(
            db, "Bacterial Leaf Blight",
            "Water-soaked yellow to white stripes along leaf edges that dry out and turn grey.",
            "Remove severely affected plant parts and avoid excess nitrogen. Drain the field if possible.",
            "Use healthy seed, maintain balanced fertilizer and avoid injuring leaves.",
            "Monitor regularly for insects and other plant stress.",
            "Do not move water or tools from infected fields to healthy fields."
        )

        insertDiseaseCare(
            db, "Brown Spot",
            "Oval brown spots with yellow halo on leaves and grains.",
            "Remove severely affected leaves and improve plant nutrition, especially potassium.",
            "Use healthy seed, balanced fertilizer and avoid water stress.",
            "Inspect leaves regularly for insect damage.",
            "Remove infected straw and debris from the field."
        )

        insertDiseaseCare(
            db, "Leaf Blast",
            "Diamond-shaped spots with grey centre and brown edges on leaves.",
            "Remove severely affected plant parts and avoid heavy nitrogen application.",
            "Maintain balanced fertilizer, proper spacing and avoid long leaf wetness.",
            "Monitor the field regularly for pests and other stress.",
            "Remove infected debris and avoid moving infected plant material."
        )

        insertDiseaseCare(
            db, "Tungro",
            "Yellow-orange leaves, stunted growth and fewer tillers.",
            "Remove infected plants and manage green leafhoppers that spread the virus.",
            "Control leafhoppers and use healthy seedlings.",
            "Inspect regularly for leafhoppers.",
            "Remove infected plants promptly and avoid planting near infected fields."
        )

        // ---------------- HEALTHY ----------------

        insertDiseaseCare(
            db, "Healthy",
            "No major disease symptoms are indicated by the AI result.",
            "No specific disease treatment is indicated. Continue normal plant care and monitoring.",
            "Continue proper watering, sunlight, nutrition and regular inspection.",
            "Continue checking the plant regularly for early signs of pests.",
            "Maintain good plant hygiene and remove damaged or dead plant material."
        )
    }

    // ---------------- AUTO INSERT FROM PlantData.kt ----------------

    private fun insertAllPlantsFromList(db: SQLiteDatabase) {

        for (p in plantList) {

            val values = ContentValues().apply {
                put("plant_name", p.name)
                put("watering", "Water need: ${p.water}. ${p.care}")
                put("sunlight", "Sunlight requirement: ${p.sunlight}.")
                put("temperature", "Suitable temperature: ${p.temperature}. Humidity: ${p.humidity}.")
                put("soil", "${p.soil.trimEnd('.')}.")
                put(
                    "fertilizer",
                    "${p.fertilizer.trimEnd('.')}. Frequency: ${p.fertilizingFrequency.trimEnd('.')}."
                )
                put(
                    "maintenance",
                    "Pruning / upkeep: ${p.pruning.trimEnd('.')}. " +
                            "Common problems: ${p.commonProblems}. " +
                            "Common pests: ${p.commonPests}."
                )
            }

            // Already irukkura plant (hand-written data) ah skip pannum
            db.insertWithOnConflict(
                "plant_care",
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
            )
        }
    }

    // ---------------- INSERT HELPERS ----------------

    private fun insertPlantCare(
        db: SQLiteDatabase,
        plantName: String,
        watering: String,
        sunlight: String,
        temperature: String,
        soil: String,
        fertilizer: String,
        maintenance: String
    ) {
        val values = ContentValues().apply {
            put("plant_name", plantName)
            put("watering", watering)
            put("sunlight", sunlight)
            put("temperature", temperature)
            put("soil", soil)
            put("fertilizer", fertilizer)
            put("maintenance", maintenance)
        }
        db.insert("plant_care", null, values)
    }

    private fun insertDiseaseCare(
        db: SQLiteDatabase,
        diseaseName: String,
        symptoms: String,
        treatment: String,
        prevention: String,
        pestManagement: String,
        precautions: String
    ) {
        val values = ContentValues().apply {
            put("disease_name", diseaseName)
            put("symptoms", symptoms)
            put("treatment", treatment)
            put("prevention", prevention)
            put("pest_management", pestManagement)
            put("precautions", precautions)
        }
        db.insert("disease_care", null, values)
    }

    // ---------------- GET PLANT CARE ----------------

    fun getPlantCare(plantName: String): PlantCareData? {

        // "Rice" -> "Paddy" maari alias names-um handle aagum
        val lookupName = SupportedPlants.canonical(plantName) ?: plantName.trim()

        val db = readableDatabase

        db.query(
            "plant_care",
            null,
            "plant_name = ? COLLATE NOCASE",
            arrayOf(lookupName),
            null, null, null
        ).use {

            if (it.moveToFirst()) {

                return PlantCareData(
                    plantName = it.getString(it.getColumnIndexOrThrow("plant_name")),
                    watering = it.getString(it.getColumnIndexOrThrow("watering")),
                    sunlight = it.getString(it.getColumnIndexOrThrow("sunlight")),
                    temperature = it.getString(it.getColumnIndexOrThrow("temperature")),
                    soil = it.getString(it.getColumnIndexOrThrow("soil")),
                    fertilizer = it.getString(it.getColumnIndexOrThrow("fertilizer")),
                    maintenance = it.getString(it.getColumnIndexOrThrow("maintenance"))
                )
            }
        }

        return null
    }

    // ---------------- NAME NORMALIZER ----------------

    private fun normalize(text: String): String =
        text.lowercase()
            .replace("_", " ")
            .replace("-", " ")
            .replace(Regex("[^a-z ]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
            .removeSuffix("s")

    // ---------------- GET DISEASE CARE ----------------

    fun getDiseaseCare(diseaseName: String): DiseaseCareData? {

        val wanted = normalize(diseaseName)
        if (wanted.isEmpty()) return null

        val db = readableDatabase
        var best: DiseaseCareData? = null
        var bestLength = 0

        db.query("disease_care", null, null, null, null, null, null).use {

            while (it.moveToNext()) {

                val name = it.getString(it.getColumnIndexOrThrow("disease_name"))
                val key = normalize(name)

                if ((wanted.contains(key) || key.contains(wanted)) && key.length > bestLength) {

                    bestLength = key.length

                    best = DiseaseCareData(
                        diseaseName = name,
                        symptoms = it.getString(it.getColumnIndexOrThrow("symptoms")),
                        treatment = it.getString(it.getColumnIndexOrThrow("treatment")),
                        prevention = it.getString(it.getColumnIndexOrThrow("prevention")),
                        pestManagement = it.getString(it.getColumnIndexOrThrow("pest_management")),
                        precautions = it.getString(it.getColumnIndexOrThrow("precautions"))
                    )
                }
            }
        }

        return best
    }

    // ---------------- UPGRADE ----------------

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS plant_care")
        db.execSQL("DROP TABLE IF EXISTS disease_care")
        onCreate(db)
    }
}

data class PlantCareData(
    val plantName: String,
    val watering: String,
    val sunlight: String,
    val temperature: String,
    val soil: String,
    val fertilizer: String,
    val maintenance: String
)

data class DiseaseCareData(
    val diseaseName: String,
    val symptoms: String,
    val treatment: String,
    val prevention: String,
    val pestManagement: String,
    val precautions: String
)