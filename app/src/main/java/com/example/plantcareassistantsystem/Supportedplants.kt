package com.example.plantcareassistantsystem

/**
 * Water Reminder and Care Information ku support aagura ellaa plants inga irundhu varum.
 *
 * - PlantData.kt (plantList) la irukkura ellaa plants automatic ah include aagum.
 * - Watering days, plantList la irukkura "water" value paathu automatic ah set aagum.
 * - Oru plant ku thani watering days venum na, keezha customFrequencies la add pannunga.
 */
object SupportedPlants {

    // Thani watering days (days) venumnaa inga set pannunga.
    // AI model plants (Apple, Grape) PlantData la illa, so inga irukkanum.
    private val customFrequencies = linkedMapOf(
        "Tomato" to 2,
        "Potato" to 3,
        "Apple" to 3,
        "Corn" to 2,
        "Grape" to 2,
        "Paddy" to 1
    )

    // AI model vera name anuppina (lowercase key) canonical name ku map aagum
    private val aliases = mapOf(
        "rice" to "Paddy",
        "paddy" to "Paddy",
        "maize" to "Corn",
        "okra" to "Lady's Finger",
        "lady finger" to "Lady's Finger",
        "ladies finger" to "Lady's Finger",
        "eggplant" to "Brinjal",
        "aubergine" to "Brinjal",
        "drumstick" to "Moringa",
        "peanut" to "Groundnut",
        "finger millet" to "Ragi",
        "pearl millet" to "Pearl Millet",
        "sugar cane" to "Sugarcane",
        "bell pepper" to "Capsicum",
        "pepper bell" to "Capsicum",
        "chili" to "Chilli",
        "chilli pepper" to "Chilli",
        "coriander leaf" to "Coriander",
        "tapioca" to "Tapioca",
        "cassava" to "Tapioca"
    )

    private val allNames: List<String> by lazy {
        (customFrequencies.keys + plantList.map { it.name })
            .distinctBy { it.lowercase() }
            .sorted()
    }

    private val lookup: Map<String, String> by lazy {
        allNames.associateBy { it.lowercase() }
    }

    val names: List<String>
        get() = allNames

    private fun normalize(raw: String): String =
        raw.lowercase()
            .replace("_", " ")
            .replace("-", " ")
            .replace(Regex("[^a-z' ]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

    fun canonical(raw: String): String? {

        val key = normalize(raw)

        if (key.isEmpty()) return null

        aliases[key]?.let { return it }

        lookup[key]?.let { return it }

        // Fallback: "Potato___Late_blight", "Corn_(maize)___Common_rust_" maari
        // full label vandhaalum plant name ah kandupidikkum (longest match win aagum)
        val padded = " $key "

        allNames
            .filter { padded.contains(" ${it.lowercase()} ") }
            .maxByOrNull { it.length }
            ?.let { return it }

        return aliases.entries
            .filter { padded.contains(" ${it.key} ") }
            .maxByOrNull { it.key.length }
            ?.value
    }

    fun frequencyOf(plantName: String): Int {

        val name = canonical(plantName) ?: return 2

        customFrequencies[name]?.let { return it }

        val plant = plantList.firstOrNull {
            it.name.equals(name, ignoreCase = true)
        } ?: return 2

        return frequencyFromWater(plant.water)
    }

    // PlantData la irukkura "water" text ah days ah maathum
    private fun frequencyFromWater(water: String): Int {

        val w = water.lowercase().replace("–", "-")

        return when {
            w.contains("high") -> 1
            w.contains("regular") -> 2
            w.contains("low-moderate") -> 4
            w.contains("moderate") -> 3
            w.contains("low") -> 7
            else -> 2
        }
    }
}