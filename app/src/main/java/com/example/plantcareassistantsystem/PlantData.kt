package com.example.plantcareassistantsystem

data class PlantData(
    val name: String,
    val type: String,
    val water: String,
    val sunlight: String,
    val soil: String,
    val temperature: String,
    val humidity: String,
    val fertilizer: String,
    val fertilizingFrequency: String,
    val pruning: String,
    val potSize: String,
    val location: String,
    val growthPeriod: String,
    val commonProblems: String,
    val commonPests: String,
    val propagation: String,
    val care: String
)

// Periya trees and field crops ku pot size suitable illa, so idha use pannirukkom
private const val GROUND = "Open ground planting (not suitable for pots)"
private const val FIELD = "Field cultivation (not suitable for pots)"
private const val LARGE = "Very large container or open ground"

// Argument order:
// name, type, water, sunlight, soil, temperature, humidity, fertilizer,
// fertilizingFrequency, pruning, potSize, location, growthPeriod,
// commonProblems, commonPests, propagation, care

val plantList = listOf(

    // ---------------- FLOWERING PLANTS ----------------

    PlantData("Rose", "Flowering Plant", "Moderate", "4–6 hours", "Well-drained soil", "18–30°C", "40–60%", "Balanced NPK fertilizer", "Every 3–4 weeks", "Remove dry flowers and dead branches", "12–16 inch pot", "Balcony / Garden", "2–3 years", "Yellow leaves, root rot, poor flowering", "Aphids, spider mites, thrips", "Stem cuttings", "Keep in bright sunlight. Water regularly but avoid overwatering."),

    PlantData("Jasmine", "Flowering Plant (Malligai)", "Moderate", "5–6 hours", "Well-drained fertile soil", "18–30°C", "50–70%", "Organic compost / NPK", "Every 4 weeks", "Prune after flowering", "12–16 inch pot", "Balcony / Garden", "1–2 years", "Yellow leaves, poor flowering", "Aphids, mealybugs", "Stem cuttings", "Give sunlight and support the climbing branches."),

    PlantData("Mullai", "Flowering Shrub (Mullai)", "Moderate", "5–6 hours", "Well-drained sandy loam soil", "20–35°C", "50–70%", "Compost + NPK", "Every 4 weeks", "Prune after the flowering season", "14–18 inch pot", "Garden / Terrace", "1–2 years", "Bud drop, leaf spots", "Budworm, aphids", "Stem cuttings / Layering", "Needs full sunlight and regular watering during flowering."),

    PlantData("Hibiscus", "Flowering Plant (Sembaruthi)", "Regular", "5–7 hours", "Fertile, well-drained soil", "18–32°C", "50–70%", "NPK fertilizer", "Every 3–4 weeks", "Prune after flowering", "14–18 inch pot", "Balcony / Garden", "1–2 years", "Bud drop, yellow leaves", "Aphids, whiteflies, mealybugs", "Stem cuttings", "Provide good sunlight and regular watering."),

    PlantData("Marigold", "Flowering Plant (Samanthi Poo)", "Moderate", "5–7 hours", "Well-drained fertile soil", "18–30°C", "40–60%", "NPK / Compost", "Every 3–4 weeks", "Remove faded flowers", "8–12 inch pot", "Balcony / Garden", "2–4 months", "Poor flowering, leaf spots", "Aphids, spider mites", "Seeds", "Deadhead flowers regularly for continuous blooming."),

    PlantData("Sunflower", "Flowering Plant", "Regular", "6–8 hours", "Rich, well-drained soil", "18–30°C", "40–60%", "Compost / NPK", "Every 3–4 weeks", "Remove damaged leaves", "16–20 inch pot", "Garden / Outdoor", "3–4 months", "Weak stems, leaf spots", "Aphids, caterpillars", "Seeds", "Needs strong sunlight and regular watering."),

    PlantData("Crossandra", "Flowering Plant (Kanakambaram)", "Moderate", "4–6 hours", "Rich, well-drained soil", "20–35°C", "50–80%", "Compost + Flower fertilizer", "Every 4–6 weeks", "Remove faded flowers", "12–16 inch pot", "Garden / Balcony", "6–12 months", "Leaf spots, root rot", "Aphids, mealybugs", "Seeds / Cuttings", "Prefers warm conditions, moderate sunlight and moisture."),

    PlantData("Bougainvillea", "Flowering Climber", "Low", "6–8 hours", "Sandy, well-drained soil", "20–35°C", "40–70%", "Compost + Flower fertilizer", "Every 6–8 weeks", "Prune after flowering", "18–24 inch pot", "Garden / Terrace", "1–3 years", "Poor flowering, root rot", "Aphids, mealybugs", "Stem cuttings", "Needs strong sunlight and should not be overwatered."),

    PlantData("Periwinkle", "Flowering Plant (Nithyakalyani)", "Low", "4–6 hours", "Well-drained sandy loam soil", "20–35°C", "40–70%", "Compost", "Every 6–8 weeks", "Trim overgrown stems", "10–14 inch pot", "Garden / Balcony", "6–12 months", "Root rot, leaf spots", "Aphids, mites", "Seeds / Cuttings", "Needs sunlight, warm weather and well-drained soil."),

    PlantData("Lotus", "Aquatic Flowering Plant (Thamarai)", "High", "6–8 hours", "Heavy loamy clay soil", "20–35°C", "60–90%", "Aquatic plant fertilizer", "Every 6–8 weeks", "Remove old leaves and flowers", "18–24 inch water container", "Pond / Water Garden", "6–12 months", "Leaf rot, algae growth", "Aphids, caterpillars", "Rhizomes / Seeds", "Requires standing water, warm temperature and plenty of sunlight."),

    PlantData("Water Lily", "Aquatic Flowering Plant (Ambal)", "High", "5–7 hours", "Heavy clay loam soil", "22–35°C", "60–90%", "Aquatic plant fertilizer tablets", "Every 6–8 weeks", "Remove old leaves and faded flowers", "16–20 inch water container", "Pond / Water tub", "2–3 months", "Algae growth, leaf rot", "Aphids", "Tubers / Division", "Keep roots submerged in still water with direct sunlight."),

    PlantData("Plumeria", "Flowering Tree (Kadu Arali)", "Low", "6–8 hours", "Sandy, well-drained soil", "20–35°C", "40–70%", "Compost + Flower fertilizer", "Every 6–8 weeks", "Prune dry branches", "20–24 inch pot or open ground", "Garden / Temple garden", "2–4 years", "Root rot, rust disease", "Mealybugs, scale insects", "Stem cuttings", "Needs strong sunlight and low-to-moderate watering."),

    PlantData("Chrysanthemum", "Flowering Plant (Sevvanthi)", "Moderate", "5–7 hours", "Well-drained loamy soil", "15–30°C", "50–70%", "Compost + Flower fertilizer", "Every 3–4 weeks", "Pinch growing tips and remove faded flowers", "10–14 inch pot", "Balcony / Garden", "3–5 months", "Leaf spots, powdery mildew", "Aphids, thrips", "Stem cuttings", "Needs good sunlight and regular pinching for more flowers."),

    PlantData("Tuberose", "Flowering Plant (Sambangi)", "Moderate", "5–7 hours", "Well-drained loamy soil", "20–35°C", "50–70%", "Compost + NPK", "Every 4 weeks", "Remove dry leaves and spent flower spikes", "10–14 inch pot", "Garden / Balcony", "3–4 months", "Leaf spots, bulb rot", "Thrips, aphids", "Bulbs", "Needs sunlight and good drainage. Cut the spikes when buds begin to open."),

    PlantData("Oleander", "Flowering Shrub (Arali)", "Low", "6–8 hours", "Well-drained soil", "20–38°C", "40–70%", "Compost", "Every 2–3 months", "Prune after flowering", "18–24 inch pot or open ground", "Garden / Roadside", "1–2 years", "Leaf spots, poor flowering", "Aphids, scale insects", "Stem cuttings", "Hardy plant. All parts are poisonous, so keep away from children and pets."),

    PlantData("Crape Jasmine", "Flowering Shrub (Nandiyavattai)", "Moderate", "4–6 hours", "Fertile, well-drained soil", "20–35°C", "50–70%", "Compost", "Every 6–8 weeks", "Trim after flowering", "14–18 inch pot", "Garden / Temple garden", "1–2 years", "Leaf yellowing, poor flowering", "Aphids, mealybugs", "Stem cuttings", "Gives white flowers most of the year with sunlight and moderate watering."),

    PlantData("Globe Amaranth", "Flowering Plant (Vadamalli)", "Moderate", "5–7 hours", "Well-drained soil", "20–35°C", "40–60%", "Compost", "Every 4 weeks", "Pinch growing tips", "8–12 inch pot", "Garden / Balcony", "2–3 months", "Leaf spots, root rot", "Aphids, mites", "Seeds", "Pinch growing tips for bushy growth and more flowers."),

    PlantData("Night Jasmine", "Flowering Shrub (Pavazhamalli)", "Moderate", "5–7 hours", "Well-drained fertile soil", "20–35°C", "50–70%", "Compost", "Every 6–8 weeks", "Prune after flowering", "18–24 inch pot or open ground", "Garden / Temple garden", "2–3 years", "Leaf spots, poor flowering", "Mealybugs, aphids", "Seeds / Cuttings", "Flowers bloom at night and fall by morning. Needs good sunlight."),

    PlantData("Butterfly Pea", "Flowering / Medicinal Climber (Sangu Pushpam)", "Moderate", "5–7 hours", "Well-drained loamy soil", "20–35°C", "50–70%", "Compost", "Every 4–6 weeks", "Trim long vines", "10–14 inch pot", "Balcony / Garden", "2–3 months", "Leaf spots, powdery mildew", "Aphids, mites", "Seeds", "Give support to climb. Blue flowers are used for herbal tea."),

    // ---------------- MEDICINAL & HERBS ----------------

    PlantData("Tulsi", "Medicinal Herb", "Moderate", "5–6 hours", "Fertile, well-drained soil", "20–30°C", "50–70%", "Organic compost / NPK", "Every 4–6 weeks", "Pinch flower buds and trim weak branches", "8–12 inch pot", "Balcony / Window / Garden", "4–6 months", "Leaf yellowing, fungal infection", "Aphids, whiteflies, mealybugs", "Stem cuttings / Seeds", "Keep Tulsi in sunlight. Water when the top soil becomes dry."),

    PlantData("Aloe Vera", "Succulent Plant (Katralai)", "Low", "4–6 hours", "Sandy, well-drained soil", "18–30°C", "30–50%", "Succulent fertilizer", "Every 2–3 months", "Remove damaged or dry leaves", "8–12 inch pot", "Indoor / Balcony", "1–2 years", "Root rot, brown tips, soft leaves", "Mealybugs, scale insects", "Offsets / Pups", "Allow soil to dry between watering. Keep in bright sunlight."),

    PlantData("Neem", "Medicinal Tree (Vembu)", "Low", "6–8 hours", "Well-drained soil", "20–35°C", "40–70%", "Organic compost", "Every 2–3 months", "Remove dry branches", GROUND, "Garden / Outdoor", "3–5 years", "Leaf yellowing, slow growth", "Scale insects, caterpillars", "Seeds", "Neem grows well in warm outdoor conditions with good sunlight."),

    PlantData("Mint", "Herb (Pudina)", "High", "3–5 hours", "Moist, well-drained soil", "15–30°C", "50–70%", "Compost / Organic fertilizer", "Every 4 weeks", "Trim regularly to encourage bushy growth", "8–12 inch pot", "Kitchen / Balcony", "2–4 months", "Wilting, yellow leaves", "Aphids, spider mites", "Stem cuttings", "Keep soil moist but avoid waterlogging."),

    PlantData("Curry Leaf", "Herb / Small Tree (Karuveppilai)", "Moderate", "5–7 hours", "Fertile, well-drained soil", "20–35°C", "50–70%", "Organic compost / NPK", "Every 4–6 weeks", "Remove weak branches", "12–18 inch pot", "Balcony / Garden", "1–2 years", "Leaf curling, slow growth", "Aphids, scale insects", "Seeds / Stem cuttings", "Provide sunlight and avoid excessive watering."),

    PlantData("Manathakkali", "Medicinal Vegetable Plant", "Moderate", "4–6 hours", "Fertile, well-drained soil", "20–32°C", "50–70%", "Compost / Organic manure", "Every 4–6 weeks", "Remove old leaves", "10–14 inch pot", "Balcony / Garden", "3–5 months", "Leaf spots, yellowing", "Aphids, caterpillars", "Seeds", "Provide moderate sunlight and avoid waterlogging."),

    PlantData("Pirandai", "Medicinal Climbing Plant", "Low–Moderate", "4–6 hours", "Sandy, well-drained soil", "22–35°C", "40–60%", "Compost", "Every 6–8 weeks", "Trim long and weak stems", "12–16 inch pot", "Balcony / Garden", "6–12 months", "Stem rot, weak growth", "Mealybugs, scale insects", "Stem cuttings", "Give support for climbing and avoid excess water."),

    PlantData("Karpooravalli", "Medicinal Herb", "Moderate", "3–5 hours", "Well-drained potting soil", "20–32°C", "50–70%", "Compost", "Every 4–6 weeks", "Pinch growing tips", "8–12 inch pot", "Balcony / Indoor", "3–6 months", "Root rot, yellow leaves", "Aphids, mealybugs", "Stem cuttings", "Provide partial sunlight and moderate watering."),

    PlantData("Nochi", "Medicinal Shrub", "Moderate", "5–7 hours", "Well-drained soil", "20–35°C", "40–70%", "Organic compost", "Every 2–3 months", "Remove dry branches", "18–24 inch pot or open ground", "Garden / Outdoor", "1–3 years", "Leaf spots, weak growth", "Aphids, caterpillars", "Seeds / Stem cuttings", "Suitable for warm outdoor conditions and sunlight."),

    PlantData("Adathodai", "Medicinal Shrub", "Moderate", "4–6 hours", "Fertile, well-drained soil", "20–32°C", "50–70%", "Compost / Organic manure", "Every 2–3 months", "Prune old branches", "16–20 inch pot or open ground", "Garden / Outdoor", "1–3 years", "Leaf yellowing, fungal spots", "Aphids, scale insects", "Stem cuttings", "Provide moderate sunlight and well-drained soil."),

    PlantData("Avaram", "Medicinal Shrub", "Low–Moderate", "5–7 hours", "Sandy, well-drained soil", "22–35°C", "40–60%", "Organic compost", "Every 2–3 months", "Remove dry branches", "16–20 inch pot or open ground", "Garden / Outdoor", "1–3 years", "Slow growth, leaf spots", "Aphids, caterpillars", "Seeds", "Prefers warm weather, sunlight and well-drained soil."),

    PlantData("Vilvam", "Medicinal Fruit Tree (Bael)", "Low–Moderate", "6–8 hours", "Well-drained loamy soil", "20–35°C", "40–70%", "Organic manure", "Every 2–3 months", "Remove dead branches", GROUND, "Garden / Temple garden", "3–5 years", "Leaf spots, slow growth", "Aphids, scale insects", "Seeds / Grafting", "Needs sunlight, warm weather and enough growing space."),

    PlantData("Nannari", "Medicinal Climbing Plant", "Moderate", "4–6 hours", "Well-drained sandy soil", "20–35°C", "40–70%", "Organic compost", "Every 6–8 weeks", "Trim long vines", "12–16 inch pot", "Balcony / Garden", "1–2 years", "Root rot, weak growth", "Aphids, mealybugs", "Stem cuttings", "Provide support for vines and avoid excess watering."),

    PlantData("Vetiver", "Aromatic Grass (Vettiver)", "Moderate", "5–7 hours", "Sandy, well-drained soil", "20–35°C", "40–70%", "Compost / Organic manure", "Every 2–3 months", "Trim dry leaves", "12–18 inch pot", "Garden / Balcony", "6–12 months", "Dry leaves, slow growth", "Usually pest resistant", "Root slips / Division", "Needs sunlight and moderate watering with good drainage."),

    PlantData("Lemongrass", "Aromatic Grass", "Moderate", "6–8 hours", "Well-drained loamy soil", "20–35°C", "50–80%", "Compost + NPK", "Every 6–8 weeks", "Cut old leaves regularly", "16–20 inch pot", "Garden / Balcony", "4–8 months", "Leaf drying, root rot", "Grasshoppers, mites", "Clump division", "Needs full sunlight and regular watering."),

    PlantData("Vallarai", "Medicinal Herb", "High", "3–5 hours", "Moist, fertile soil", "20–32°C", "60–80%", "Compost", "Every 4–6 weeks", "Trim overgrown runners", "8–12 inch wide pot", "Balcony / Shaded Garden", "2–3 months", "Leaf spots, drying", "Snails, aphids", "Runners / Stem cuttings", "Keep soil moist and provide partial shade."),

    PlantData("Thoothuvalai", "Medicinal Climbing Plant", "Moderate", "4–6 hours", "Well-drained loamy soil", "20–35°C", "50–70%", "Compost", "Every 4–6 weeks", "Trim long stems carefully, it has thorns", "12–16 inch pot", "Balcony / Garden", "3–5 months", "Leaf spots, yellowing", "Aphids, mealybugs", "Seeds / Stem cuttings", "Give support for climbing and avoid waterlogging."),

    PlantData("Keezhanelli", "Medicinal Herb", "Moderate", "4–6 hours", "Sandy, well-drained soil", "22–35°C", "50–70%", "Compost", "Every 6–8 weeks", "Remove weeds around the plant", "8–12 inch pot", "Garden / Balcony", "2–3 months", "Leaf yellowing, rust", "Aphids, mites", "Seeds", "Grows easily in warm weather with moderate watering."),

    PlantData("Ashwagandha", "Medicinal Herb (Amukkara)", "Low", "5–7 hours", "Sandy loam, well-drained soil", "20–35°C", "40–60%", "Compost", "Every 6–8 weeks", "Remove weak branches", "12–16 inch deep pot", "Garden / Balcony", "5–6 months", "Root rot, leaf spots", "Aphids, mites", "Seeds", "Needs sunlight and dry conditions. Do not overwater."),

    PlantData("Nilavembu", "Medicinal Herb", "Moderate", "4–6 hours", "Well-drained loamy soil", "20–35°C", "50–70%", "Compost", "Every 4–6 weeks", "Pinch tips for bushy growth", "10–12 inch pot", "Garden / Balcony", "3–4 months", "Leaf spots, root rot", "Aphids, caterpillars", "Seeds", "Grows well in warm weather with moderate watering."),

    PlantData("Thippili", "Medicinal Climber (Long Pepper)", "Moderate", "3–5 hours", "Rich, well-drained soil", "22–35°C", "60–80%", "Compost + Organic manure", "Every 4–6 weeks", "Trim old stems", "12–16 inch pot", "Balcony / Shade garden", "6–12 months", "Root rot, leaf spots", "Mealybugs, caterpillars", "Stem cuttings", "Needs partial shade, humidity and support to climb."),

    PlantData("Brahmi", "Medicinal Herb", "High", "3–5 hours", "Moist, fertile soil", "20–32°C", "60–80%", "Compost", "Every 4–6 weeks", "Trim overgrown stems", "8–12 inch wide pot", "Balcony / Shaded garden", "2–3 months", "Root rot, yellowing", "Snails, aphids", "Stem cuttings", "Keep soil moist and provide partial sunlight."),

    PlantData("Kuppaimeni", "Medicinal Herb", "Moderate", "4–6 hours", "Well-drained loamy soil", "20–35°C", "50–70%", "Compost", "Every 6–8 weeks", "Remove old stems", "10–14 inch pot", "Garden / Balcony", "2–3 months", "Leaf spots", "Aphids, caterpillars", "Seeds / Stem cuttings", "A hardy herb that grows easily in sunlight."),

    PlantData("Thumbai", "Medicinal Herb (Thumbai)", "Low–Moderate", "5–7 hours", "Sandy, well-drained soil", "22–35°C", "40–60%", "Compost", "Every 6–8 weeks", "Trim old stems", "8–12 inch pot", "Garden / Balcony", "1–2 months", "Leaf spots", "Aphids, caterpillars", "Seeds", "Grows well in dry weather with sunlight."),

    PlantData("Henna", "Medicinal Shrub (Maruthani)", "Moderate", "6–8 hours", "Well-drained loamy soil", "20–38°C", "40–60%", "Compost + NPK", "Every 2–3 months", "Prune regularly to encourage new leaves", "18–24 inch pot or open ground", "Garden / Outdoor", "6–12 months", "Leaf spots, root rot", "Aphids, caterpillars", "Seeds / Stem cuttings", "Needs plenty of sunlight and warm weather."),

    PlantData("Castor", "Oil Seed Plant (Amanakku)", "Low–Moderate", "6–8 hours", "Well-drained loamy soil", "20–35°C", "40–60%", "Compost + NPK", "Every 6–8 weeks", "Remove lower leaves", "Open ground or large pot", "Garden / Farm", "4–6 months", "Leaf spots, wilt", "Semilooper caterpillar, hairy caterpillar", "Seeds", "Seeds are poisonous. Keep away from children and pets."),

    // ---------------- INDOOR PLANTS ----------------

    PlantData("Money Plant", "Indoor Foliage Plant", "Moderate", "Bright indirect light", "Well-drained potting soil", "18–30°C", "40–60%", "Liquid indoor plant fertilizer", "Every 4–6 weeks", "Trim long vines and yellow leaves", "8–12 inch pot", "Indoor / Balcony", "1–2 years", "Yellow leaves, root rot, slow growth", "Mealybugs, scale insects, spider mites", "Stem cuttings", "Keep soil slightly moist. Avoid harsh direct sunlight."),

    PlantData("Snake Plant", "Indoor Foliage Plant", "Low", "2–5 hours", "Sandy, well-drained soil", "15–30°C", "30–50%", "Balanced indoor fertilizer", "Every 2–3 months", "Remove damaged leaves", "8–12 inch pot", "Indoor", "1–2 years", "Root rot, soft leaves", "Mealybugs, spider mites", "Leaf cuttings / Division", "Allow soil to dry between watering. Tolerates low light."),

    // ---------------- VEGETABLES ----------------

    PlantData("Tomato", "Vegetable Plant (Thakkali)", "Regular", "6–8 hours", "Rich, well-drained soil", "18–27°C", "50–70%", "NPK fertilizer / Compost", "Every 2–3 weeks", "Remove yellow leaves and weak branches", "12–18 inch pot", "Garden / Balcony", "3–4 months", "Leaf curl, blossom end rot, fungal diseases", "Aphids, whiteflies, fruit worms", "Seeds / Stem cuttings", "Provide plenty of sunlight and regular watering. Support the plant."),

    PlantData("Chilli", "Vegetable Plant (Milagai)", "Regular", "5–7 hours", "Fertile, well-drained soil", "20–30°C", "50–70%", "NPK / Compost", "Every 3–4 weeks", "Remove weak branches", "10–14 inch pot", "Balcony / Garden", "3–5 months", "Leaf curl, flower drop", "Aphids, thrips, mites", "Seeds", "Provide sunlight and consistent moisture."),

    PlantData("Brinjal", "Vegetable Plant (Kathirikai)", "Regular", "6–8 hours", "Rich, well-drained soil", "22–30°C", "50–70%", "NPK / Compost", "Every 3 weeks", "Remove damaged leaves", "14–18 inch pot", "Garden / Balcony", "3–5 months", "Leaf curl, fruit damage", "Aphids, shoot and fruit borer", "Seeds", "Provide sunlight, nutrients and support when needed."),

    PlantData("Lady's Finger", "Vegetable (Vendakkai)", "Moderate", "6–8 hours", "Loamy, well-drained soil", "24–35°C", "40–70%", "Compost + NPK", "Every 4–6 weeks", "Remove old leaves", "14–18 inch pot", "Kitchen Garden", "3–5 months", "Yellow leaves, leaf curl", "Aphids, whiteflies", "Seeds", "Requires warm weather, full sunlight and regular watering."),

    PlantData("Cluster Beans", "Vegetable (Kothavarangai)", "Moderate", "6–8 hours", "Sandy loam, well-drained soil", "22–35°C", "40–70%", "Compost + NPK", "Every 4–6 weeks", "Remove weak branches", "14–18 inch pot", "Kitchen Garden / Farm", "3–4 months", "Leaf spots, poor flowering", "Aphids, caterpillars", "Seeds", "Needs warm weather, full sunlight and moderate watering."),

    PlantData("Moringa", "Vegetable / Tree (Murungai)", "Moderate", "6–8 hours", "Sandy, well-drained soil", "22–35°C", "40–70%", "Compost + NPK", "Every 2–3 months", "Prune regularly to maintain height", LARGE, "Garden / Farm", "1–2 years", "Root rot, leaf spots", "Aphids, caterpillars", "Seeds / Stem cuttings", "Needs full sunlight, good drainage and regular pruning. Also called Drumstick Tree."),

    PlantData("Coriander", "Herb (Kothamalli)", "Regular", "3–5 hours", "Loose, well-drained soil", "15–25°C", "50–70%", "Compost", "Every 4–6 weeks", "Harvest outer leaves regularly", "6–8 inch pot", "Kitchen / Balcony", "1–2 months", "Bolting, yellow leaves", "Aphids, leaf miners", "Seeds", "Keep soil slightly moist and provide morning sunlight."),

    PlantData("Spinach", "Leafy Vegetable", "Regular", "3–5 hours", "Rich, moist soil", "15–25°C", "50–70%", "Compost / Nitrogen fertilizer", "Every 3–4 weeks", "Harvest outer leaves", "8–12 inch pot", "Balcony / Garden", "1–2 months", "Yellow leaves, bolting", "Aphids, leaf miners", "Seeds", "Keep soil moist and provide mild sunlight."),

    PlantData("Amaranth", "Leafy Vegetable (Mulai Keerai)", "Regular", "4–6 hours", "Fertile, well-drained soil", "20–30°C", "50–70%", "Compost / Nitrogen fertilizer", "Every 3–4 weeks", "Harvest outer leaves regularly", "8–12 inch pot", "Balcony / Garden", "1–2 months", "Yellow leaves, leaf spots", "Aphids, caterpillars", "Seeds", "Keep soil moist and provide morning sunlight."),

    PlantData("Fenugreek", "Leafy Vegetable (Vendhayam)", "Moderate", "4–6 hours", "Loose, well-drained loamy soil", "20–30°C", "50–70%", "Compost", "Every 3–4 weeks", "Harvest leaves regularly", "8–10 inch pot", "Kitchen Garden / Balcony", "1–2 months", "Yellow leaves, powdery mildew", "Aphids, leaf miners", "Seeds", "Keep soil slightly moist and provide morning sunlight."),

    PlantData("Ponnanganni", "Leafy Vegetable", "High", "4–6 hours", "Moist, fertile soil", "22–35°C", "60–80%", "Compost", "Every 4 weeks", "Harvest leaves regularly", "8–12 inch pot", "Kitchen Garden / Balcony", "1–2 months", "Yellow leaves, leaf spots", "Aphids, snails", "Stem cuttings", "Keep soil moist and provide partial sunlight."),

    PlantData("Agathi", "Leafy Vegetable / Tree (Agathi Keerai)", "Moderate", "5–7 hours", "Well-drained loamy soil", "22–35°C", "50–70%", "Compost + Organic manure", "Every 2–3 months", "Prune to keep height manageable", LARGE, "Garden / Farm", "6–12 months", "Leaf spots, root rot", "Caterpillars, aphids", "Seeds / Stem cuttings", "Fast growing. Leaves and flowers are used in cooking."),

    PlantData("Malabar Spinach", "Leafy Vegetable Climber (Pasalai Keerai)", "High", "4–6 hours", "Rich, moist, well-drained soil", "22–35°C", "60–80%", "Compost", "Every 3–4 weeks", "Trim vines regularly", "12–16 inch pot", "Balcony / Kitchen Garden", "1–2 months", "Leaf spots, yellowing", "Aphids, caterpillars", "Seeds / Stem cuttings", "Give support to climb and keep soil moist."),

    PlantData("Purslane", "Leafy Vegetable (Paruppu Keerai)", "Low–Moderate", "5–7 hours", "Sandy, well-drained soil", "22–38°C", "40–60%", "Compost", "Every 4–6 weeks", "Harvest tips regularly", "8–10 inch pot", "Balcony / Kitchen Garden", "1–2 months", "Root rot if overwatered", "Aphids, caterpillars", "Seeds / Stem cuttings", "Hardy leafy plant that grows well in warm sunny weather."),

    PlantData("Roselle", "Leafy Vegetable (Pulicha Keerai)", "Moderate", "5–7 hours", "Well-drained loamy soil", "22–35°C", "50–70%", "Compost + NPK", "Every 4 weeks", "Pinch tips for bushy growth", "14–18 inch pot", "Garden / Farm", "4–5 months", "Leaf spots, root rot", "Aphids, mealybugs", "Seeds", "Needs warm weather and sunlight. Leaves have a sour taste."),

    PlantData("Cucumber", "Vegetable (Vellarikai)", "High", "5–7 hours", "Rich, well-drained loamy soil", "20–32°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Remove weak vines", "14–18 inch pot", "Kitchen Garden / Terrace", "2–3 months", "Powdery mildew, yellow leaves", "Aphids, cucumber beetles", "Seeds", "Needs regular watering, sunlight and climbing support."),

    PlantData("Bitter Gourd", "Vegetable Climber (Pavakkai)", "Moderate", "6–8 hours", "Rich, well-drained loamy soil", "22–32°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Train vines on support", "18–24 inch pot", "Kitchen Garden / Terrace", "3–4 months", "Powdery mildew, fruit rot", "Fruit flies, aphids", "Seeds", "Needs strong support, sunlight and regular watering."),

    PlantData("Bottle Gourd", "Vegetable Climber (Suraikkai)", "High", "6–8 hours", "Rich, well-drained soil", "22–32°C", "50–80%", "Compost + NPK", "Every 3–4 weeks", "Train and trim vines", "20–24 inch pot", "Garden / Terrace", "3–4 months", "Powdery mildew, fruit rot", "Fruit flies, aphids", "Seeds", "Provide strong climbing support and regular watering."),

    PlantData("Ridge Gourd", "Vegetable Climber (Peerkangai)", "Moderate", "6–8 hours", "Well-drained loamy soil", "22–32°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Train vines on trellis", "18–24 inch pot", "Garden / Terrace", "3–4 months", "Powdery mildew, leaf spots", "Fruit flies, aphids", "Seeds", "Needs sunlight, support and moderate watering."),

    PlantData("Snake Gourd", "Vegetable Climber (Pudalangai)", "Moderate", "6–8 hours", "Rich, well-drained soil", "22–32°C", "50–80%", "Compost + NPK", "Every 3–4 weeks", "Train vines on support", "18–24 inch pot", "Garden / Terrace", "3–4 months", "Powdery mildew, fruit rot", "Fruit flies, aphids", "Seeds", "Provide strong support, sunlight and regular watering."),

    PlantData("Ash Gourd", "Vegetable Creeper (Neer Poosani)", "Moderate", "6–8 hours", "Rich, well-drained loamy soil", "22–35°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Train and trim vines", "24–30 inch pot or open ground", "Garden / Terrace / Farm", "4–5 months", "Powdery mildew, fruit rot", "Fruit flies, aphids", "Seeds", "Needs plenty of space, sunlight and support for heavy fruits."),

    PlantData("Ivy Gourd", "Vegetable Climber (Kovakkai)", "Moderate", "5–7 hours", "Well-drained loamy soil", "22–35°C", "50–70%", "Compost + NPK", "Every 4–6 weeks", "Trim old vines", "14–18 inch pot", "Garden / Terrace", "2–3 months", "Leaf spots, fruit rot", "Fruit flies, aphids", "Stem cuttings", "Grows easily with support. Cuttings root quickly."),

    PlantData("Broad Beans", "Vegetable Climber (Avarakkai)", "Moderate", "5–7 hours", "Fertile, well-drained soil", "20–30°C", "50–70%", "Compost + NPK", "Every 4 weeks", "Remove weak vines", "14–18 inch pot", "Garden / Terrace", "3–4 months", "Powdery mildew, rust", "Aphids, pod borers", "Seeds", "Give strong support and avoid waterlogging."),

    PlantData("Cowpea", "Vegetable / Pulse Plant (Thattaipayir)", "Moderate", "6–8 hours", "Well-drained sandy loam soil", "22–35°C", "50–70%", "Compost", "Every 4–6 weeks", "Remove weak vines", "12–16 inch pot", "Garden / Farm", "2–3 months", "Leaf spots, rust", "Aphids, pod borers", "Seeds", "Fixes nitrogen in soil and tolerates warm weather."),

    PlantData("Turkey Berry", "Vegetable Shrub (Sundakkai)", "Low–Moderate", "6–8 hours", "Well-drained soil", "22–35°C", "40–70%", "Compost", "Every 2–3 months", "Prune after harvest", "14–18 inch pot", "Garden / Backyard", "6–8 months", "Leaf spots, root rot", "Aphids, mites", "Seeds", "A hardy plant that grows well in warm weather."),

    PlantData("Pumpkin", "Vegetable Creeper (Parangikai)", "Moderate", "6–8 hours", "Rich, well-drained soil", "20–32°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Remove weak vines", "24–30 inch pot or open ground", "Garden / Terrace", "3–5 months", "Powdery mildew, fruit rot", "Aphids, fruit flies", "Seeds", "Needs plenty of sunlight, space and regular watering."),

    PlantData("Radish", "Root Vegetable (Mullangi)", "Moderate", "4–6 hours", "Loose, sandy loam soil", "15–28°C", "40–60%", "Compost", "Every 3–4 weeks", "Thin crowded seedlings", "10–14 inch pot", "Kitchen Garden / Balcony", "1–2 months", "Root cracking, leaf spots", "Aphids, caterpillars", "Seeds", "Use loose soil and maintain even moisture."),

    PlantData("Carrot", "Root Vegetable", "Moderate", "5–7 hours", "Loose sandy loam soil", "15–28°C", "40–60%", "Compost", "Every 4–6 weeks", "Thin seedlings", "12–16 inch deep pot", "Kitchen Garden", "2–3 months", "Root deformity, leaf spots", "Aphids, caterpillars", "Seeds", "Needs deep loose soil and consistent moisture."),

    PlantData("Beetroot", "Root Vegetable", "Moderate", "5–7 hours", "Loose, fertile loamy soil", "15–28°C", "40–60%", "Compost + Balanced fertilizer", "Every 4–6 weeks", "Thin seedlings", "12–16 inch pot", "Kitchen Garden", "2–3 months", "Root cracking, leaf spots", "Aphids, leaf miners", "Seeds", "Keep soil moist but never waterlogged."),

    PlantData("Cabbage", "Leafy Vegetable (Muttaikose)", "Moderate", "5–7 hours", "Rich, well-drained loamy soil", "15–28°C", "50–70%", "Compost + NPK", "Every 4–6 weeks", "Remove damaged outer leaves", "16–20 inch pot", "Kitchen Garden", "3–4 months", "Leaf holes, fungal spots", "Caterpillars, aphids", "Seeds", "Needs cool conditions, sunlight and regular watering."),

    PlantData("Cauliflower", "Vegetable (Pookosu)", "Moderate", "5–7 hours", "Rich, well-drained loamy soil", "15–28°C", "50–70%", "Compost + NPK", "Every 4–6 weeks", "Remove damaged leaves", "16–20 inch pot", "Kitchen Garden", "3–4 months", "Curd discoloration, leaf spots", "Caterpillars, aphids", "Seeds", "Needs cool weather and consistent moisture."),

    PlantData("Onion", "Bulb Vegetable (Vengayam)", "Regular", "5–7 hours", "Loose, well-drained soil", "13–25°C", "50–70%", "Compost / NPK", "Every 3–4 weeks", "Remove damaged leaves", "8–12 inch deep pot", "Balcony / Garden", "3–5 months", "Bulb rot, yellow leaves", "Thrips, onion maggots", "Seeds / Sets", "Use loose soil and avoid excess watering near harvest."),

    PlantData("Shallot", "Bulb Vegetable (Chinna Vengayam)", "Moderate", "5–7 hours", "Loose, well-drained soil", "20–30°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Remove yellow leaves", "8–12 inch pot", "Balcony / Kitchen Garden", "2–3 months", "Purple blotch, bulb rot", "Thrips, onion maggots", "Bulbs", "Use loose soil and avoid waterlogging."),

    PlantData("Garlic", "Spice Crop (Poondu)", "Moderate", "5–7 hours", "Loose, well-drained soil", "12–25°C", "40–60%", "Compost + NPK", "Every 4–6 weeks", "Remove dry leaves", "10–14 inch pot", "Kitchen Garden", "4–6 months", "Bulb rot, leaf spots", "Thrips, mites", "Cloves", "Needs loose soil, sunlight and moderate watering."),

    PlantData("Potato", "Root Vegetable (Urulaikizhangu)", "Moderate", "5–7 hours", "Loose, well-drained sandy loam soil", "15–25°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Remove damaged leaves and weak stems", "14–18 inch deep pot", "Kitchen Garden / Nilgiris hills", "2–4 months", "Late blight, early blight, tuber rot", "Aphids, potato beetles, wireworms", "Tubers / Seed potatoes", "Use loose soil, provide sunlight and avoid waterlogging."),

    PlantData("Green Peas", "Legume Vegetable (Pattani)", "Moderate", "4–6 hours", "Fertile, well-drained loamy soil", "15–25°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Remove weak vines and support climbing stems", "12–16 inch pot", "Kitchen Garden / Hill areas", "2–3 months", "Powdery mildew, yellow leaves, poor pod formation", "Aphids, pea weevils, caterpillars", "Seeds", "Provide cool weather, sunlight and support for climbing vines."),

    PlantData("Corn", "Cereal Crop (Makkacholam)", "High", "6–8 hours", "Fertile, well-drained loamy soil", "18–30°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Remove damaged leaves and weak plants", "18–24 inch pot or open ground", "Garden / Terrace / Farm", "3–4 months", "Leaf spots, stalk rot, poor cob development", "Corn borer, aphids, armyworms", "Seeds", "Needs full sunlight, regular watering and sufficient space."),

    PlantData("Capsicum", "Vegetable Plant", "Moderate", "5–7 hours", "Fertile, well-drained loamy soil", "18–30°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Remove damaged leaves and weak branches", "12–16 inch pot", "Balcony / Garden", "3–5 months", "Leaf curl, blossom end rot, fruit spots", "Aphids, thrips, whiteflies", "Seeds", "Provide sunlight, regular watering and good air circulation."),

    PlantData("Watermelon", "Fruit Creeper (Tharboosani)", "Moderate", "6–8 hours", "Sandy loam, well-drained soil", "24–35°C", "50–70%", "Compost + NPK", "Every 3–4 weeks", "Train vines and remove extra fruits", "Open ground or large grow bag", "Farm / Terrace", "3–4 months", "Fusarium wilt, powdery mildew", "Fruit flies, aphids", "Seeds", "Needs warm weather, plenty of sunlight and space to spread."),

    // ---------------- ROOTS, TUBERS & SPICES ----------------

    PlantData("Tapioca", "Root Crop (Maravalli Kizhangu)", "Moderate", "6–8 hours", "Sandy, well-drained soil", "25–35°C", "50–70%", "Compost / NPK", "Every 6–8 weeks", "Remove damaged leaves", "Open ground or large grow bag", "Garden / Farm", "8–12 months", "Root rot, leaf spots", "Mealybugs, mites", "Stem cuttings", "Needs warm weather, sunlight and well-drained soil."),

    PlantData("Sweet Potato", "Root Vegetable (Sakkaravalli Kizhangu)", "Moderate", "6–8 hours", "Loose, sandy soil", "21–30°C", "50–70%", "Compost", "Every 4–6 weeks", "Trim excessive vines", "18–24 inch pot", "Garden / Terrace", "3–5 months", "Root damage, poor tuber growth", "Weevils, aphids", "Vine cuttings / Slips", "Provide loose soil, warmth and regular but moderate watering."),

    PlantData("Colocasia", "Tuber Vegetable (Seppankizhangu)", "High", "3–6 hours", "Rich, moist, well-drained soil", "20–35°C", "60–90%", "Compost + NPK", "Every 6–8 weeks", "Remove old leaves", "18–24 inch pot", "Garden / Wet Area", "6–10 months", "Tuber rot, leaf blight", "Aphids, caterpillars", "Corms / Tubers", "Needs moist fertile soil and partial to full sunlight."),

    PlantData("Elephant Foot Yam", "Tuber Crop (Senaikizhangu)", "Moderate", "5–7 hours", "Loose, fertile, well-drained soil", "22–35°C", "50–80%", "Compost + NPK", "Every 6–8 weeks", "Remove dry leaves", "24–30 inch pot or open ground", "Garden / Farm", "7–10 months", "Tuber rot, leaf spots", "Scale insects, caterpillars", "Corm pieces", "Needs warm weather, fertile soil and moderate watering."),

    PlantData("Ginger", "Spice Crop (Inji)", "Moderate", "3–5 hours", "Rich, loose, well-drained soil", "20–32°C", "60–80%", "Compost + Organic manure", "Every 6–8 weeks", "Remove dry leaves", "16–20 inch pot", "Garden / Balcony", "7–10 months", "Rhizome rot, leaf spots", "Shoot borer, mites", "Rhizomes", "Prefers warm humid conditions and partial sunlight."),

    PlantData("Turmeric", "Spice Crop (Manjal)", "Moderate", "4–6 hours", "Rich, well-drained loamy soil", "20–35°C", "60–80%", "Compost + Organic manure", "Every 6–8 weeks", "Remove dry leaves", "18–24 inch pot", "Garden / Terrace", "7–10 months", "Rhizome rot, leaf spots", "Shoot borer, mites", "Rhizomes", "Needs warm weather, moisture and good drainage."),

    PlantData("Black Pepper", "Spice Climber (Milagu)", "Moderate", "3–5 hours", "Rich, well-drained loamy soil", "20–35°C", "70–90%", "Compost + NPK", "Every 3 months", "Trim excess vines", "20–24 inch pot or open ground", "Garden / Farm", "2–4 years", "Quick wilt, root rot", "Scale insects, pollu beetle", "Stem cuttings", "Needs warm humid conditions, partial shade and climbing support."),

    PlantData("Betel Vine", "Climbing Leaf Crop (Vetrilai)", "High", "3–5 hours", "Rich, moist, well-drained soil", "20–35°C", "70–90%", "Compost + Organic manure", "Every 4–6 weeks", "Remove old and diseased leaves", "16–20 inch pot", "Garden / Shade House", "6–12 months", "Leaf rot, fungal spots", "Scale insects, mites", "Stem cuttings", "Needs warm humid conditions, partial shade and regular moisture."),

    PlantData("Cardamom", "Spice Crop (Elakkai)", "High", "2–4 hours (shade)", "Rich forest loam, well-drained soil", "15–30°C", "75–90%", "Compost + NPK", "Every 2–3 months", "Remove dry tillers", "Plantation / very large grow bag", "Theni and Kodaikanal hills", "2–3 years", "Rhizome rot, katte virus", "Thrips, shoot borer", "Suckers / Seeds", "Needs shade, high humidity and a cool hill climate."),

    // ---------------- CEREALS, MILLETS & PULSES ----------------

    PlantData("Paddy", "Cereal Crop (Nel)", "High", "6–8 hours", "Clay or clay loam soil that holds water", "22–35°C", "60–85%", "Urea + DAP + Potash as per soil test", "Split doses at planting, tillering and panicle stage", "Not required. Remove weeds regularly", FIELD, "Farm / Paddy field", "3–5 months", "Blast, brown spot, bacterial leaf blight, sheath blight", "Stem borer, leaf folder, brown plant hopper", "Seeds / Seedling transplanting", "Keep a thin layer of standing water during growth and drain before harvest. Weed regularly."),

    PlantData("Ragi", "Millet Crop (Kezhvaragu)", "Low–Moderate", "6–8 hours", "Red loamy or sandy loam well-drained soil", "20–35°C", "40–60%", "Farmyard manure + NPK", "At sowing and 30 days after", "Not required. Thin seedlings", FIELD, "Farm / Dry land", "3–4 months", "Blast disease, leaf spots", "Stem borer, aphids", "Seeds", "Drought tolerant. Needs sunlight and little water."),

    PlantData("Sorghum", "Millet Crop (Cholam)", "Low–Moderate", "6–8 hours", "Well-drained loamy soil", "25–35°C", "40–60%", "Farmyard manure + NPK", "At sowing and 30 days after", "Not required. Thin seedlings", FIELD, "Farm / Dry land", "3–4 months", "Grain mold, leaf blight", "Shoot fly, stem borer", "Seeds", "Drought tolerant crop suited to dry regions of Tamil Nadu."),

    PlantData("Pearl Millet", "Millet Crop (Kambu)", "Low", "6–8 hours", "Sandy, well-drained soil", "25–38°C", "30–60%", "Farmyard manure + NPK", "At sowing and 25–30 days after", "Not required. Thin seedlings", FIELD, "Farm / Dry land", "2.5–3.5 months", "Downy mildew, ergot", "Shoot fly, stem borer", "Seeds", "Very hardy crop that grows well in hot dry weather."),

    PlantData("Foxtail Millet", "Millet Crop (Thinai)", "Low", "6–8 hours", "Well-drained loamy soil", "22–35°C", "40–60%", "Farmyard manure", "At sowing", "Not required. Thin seedlings", FIELD, "Farm / Dry land", "2–3 months", "Leaf blast, rust", "Shoot fly, aphids", "Seeds", "Needs sunlight, low water and well-drained soil."),

    PlantData("Little Millet", "Millet Crop (Samai)", "Low", "6–8 hours", "Well-drained loamy soil", "22–35°C", "40–60%", "Farmyard manure", "At sowing", "Not required. Thin seedlings", FIELD, "Farm / Dry land", "2.5–3 months", "Grain smut, leaf blast", "Shoot fly, stem borer", "Seeds", "Hardy crop that grows with very little water."),

    PlantData("Black Gram", "Pulse Crop (Ulundhu)", "Moderate", "6–8 hours", "Well-drained loamy soil", "25–35°C", "50–70%", "DAP + Farmyard manure", "At sowing and flowering", "Not required. Remove weeds", FIELD, "Farm", "2–3 months", "Yellow mosaic virus, powdery mildew", "Whiteflies, pod borer", "Seeds", "Fixes nitrogen in soil. Avoid waterlogging."),

    PlantData("Green Gram", "Pulse Crop (Pachai Payaru)", "Moderate", "6–8 hours", "Well-drained sandy loam soil", "25–35°C", "50–70%", "DAP + Farmyard manure", "At sowing and flowering", "Not required. Remove weeds", FIELD, "Farm", "2–2.5 months", "Yellow mosaic virus, powdery mildew", "Whiteflies, pod borer", "Seeds", "Short duration crop. Avoid waterlogging."),

    PlantData("Red Gram", "Pulse Crop (Thuvarai)", "Low–Moderate", "6–8 hours", "Well-drained loamy soil", "25–35°C", "50–70%", "DAP + Farmyard manure", "At sowing and flowering", "Pinch tips to encourage branching", FIELD, "Farm", "4–6 months", "Wilt, sterility mosaic", "Pod borer, pod fly", "Seeds", "Needs sunlight and well-drained soil. Avoid waterlogging."),

    PlantData("Horse Gram", "Pulse Crop (Kollu)", "Low", "6–8 hours", "Red sandy loam soil", "25–38°C", "30–60%", "Farmyard manure", "At sowing", "Not required", FIELD, "Farm / Dry land", "3–4 months", "Powdery mildew, rust", "Aphids, pod borer", "Seeds", "Very drought tolerant and grows on poor soils."),

    PlantData("Groundnut", "Oil Seed Crop (Nilakadalai)", "Moderate", "6–8 hours", "Loose, red sandy loam soil", "25–35°C", "50–70%", "Gypsum + Farmyard manure + NPK", "At sowing and gypsum at flowering", "Not required. Weed regularly", FIELD, "Farm", "3.5–4 months", "Leaf spot, rust, collar rot", "Leaf miner, aphids, white grub", "Seeds (kernels)", "Needs loose soil so pegs can enter the ground. Apply gypsum at flowering."),

    PlantData("Sesame", "Oil Seed Crop (Ellu)", "Low–Moderate", "6–8 hours", "Well-drained sandy loam soil", "25–35°C", "40–60%", "Farmyard manure + NPK", "At sowing and 25–30 days after", "Thin seedlings", FIELD, "Farm", "2.5–3.5 months", "Leaf spot, phyllody", "Leaf webber, gall fly", "Seeds", "Avoid waterlogging. Needs warm weather and sunlight."),

    PlantData("Cotton", "Fibre Crop (Paruthi)", "Moderate", "6–8 hours", "Black cotton soil or deep loam", "21–35°C", "50–70%", "NPK + Farmyard manure", "Split doses at 30, 60 and 90 days", "Remove weak side shoots", FIELD, "Farm", "5–6 months", "Leaf curl, wilt, boll rot", "Bollworm, whitefly, aphids", "Seeds", "Needs warm weather and sunlight. Avoid waterlogging."),

    PlantData("Sugarcane", "Cash Crop (Karumbu)", "High", "6–8 hours", "Deep, fertile loamy soil", "20–35°C", "60–85%", "NPK + Farmyard manure", "Split doses at 30, 60 and 90 days", "Remove dry leaves (detrashing)", FIELD, "Farm", "10–12 months", "Red rot, smut, wilt", "Early shoot borer, internode borer, woolly aphid", "Setts (stem cuttings)", "Needs plenty of water and sunlight. Earthing up is important."),

    // ---------------- PLANTATION CROPS ----------------

    PlantData("Tea", "Plantation Crop (Theyilai)", "Moderate", "4–6 hours", "Acidic, well-drained loamy soil", "13–30°C", "70–90%", "NPK + Organic manure", "Every 2–3 months", "Regular plucking and pruning", FIELD, "Nilgiris hills / Tea estates", "3–5 years", "Blister blight, root rot", "Tea mosquito bug, mites", "Cuttings", "Grows in cool hilly regions like the Nilgiris with high rainfall."),

    PlantData("Coffee", "Plantation Crop (Kaapi)", "Moderate", "3–5 hours (shade)", "Acidic, well-drained loamy soil", "15–28°C", "70–90%", "NPK + Organic manure", "2–3 times a year", "Prune after harvest", FIELD, "Yercaud, Kodaikanal and Nilgiris hills", "3–4 years", "Leaf rust, berry disease", "White stem borer, mealybugs", "Seeds / Cuttings", "Needs shade trees, a cool climate and well-drained soil."),

    PlantData("Rubber", "Plantation Tree", "High", "5–7 hours", "Deep, well-drained loamy soil", "22–35°C", "70–90%", "NPK + Organic manure", "Twice a year", "Remove low branches", GROUND, "Kanyakumari plantations", "5–7 years", "Leaf fall disease, root disease", "Mites, scale insects", "Budding / Seeds", "Needs high rainfall and a warm humid climate."),

    // ---------------- FRUIT PLANTS & TREES ----------------

    PlantData("Mango", "Fruit Tree (Maamaram)", "Moderate", "6–8 hours", "Deep, well-drained soil", "24–35°C", "50–70%", "Organic manure / NPK", "Every 2–3 months", "Remove dead branches", GROUND, "Garden / Farm", "3–5+ years", "Leaf spots, poor fruiting", "Mango hopper, mealybugs", "Grafting / Seeds", "Needs warm weather, sunlight and sufficient space."),

    PlantData("Guava", "Fruit Tree (Koyya)", "Moderate", "6–8 hours", "Well-drained fertile soil", "20–32°C", "50–70%", "Compost / NPK", "Every 2–3 months", "Prune crowded branches", LARGE, "Garden / Outdoor", "2–4 years", "Leaf spots, poor fruiting", "Fruit flies, aphids", "Seeds / Air layering", "Provide sunlight and regular watering during fruiting."),

    PlantData("Lemon", "Fruit Tree (Elumichai)", "Moderate", "6–8 hours", "Sandy, well-drained soil", "20–32°C", "50–70%", "Citrus fertilizer / Compost", "Every 4–6 weeks", "Remove dead and crossing branches", "18–24 inch pot", "Balcony / Garden", "2–4 years", "Leaf yellowing, flower drop", "Aphids, scale insects, leaf miners", "Seeds / Grafting", "Needs plenty of sunlight and good drainage."),

    PlantData("Banana", "Fruit Plant (Vazhai)", "High", "6–8 hours", "Rich, well-drained loamy soil", "20–35°C", "60–80%", "Compost + NPK", "Every 4–6 weeks", "Remove dry leaves and extra suckers", LARGE, "Garden / Backyard / Farm", "9–12 months", "Leaf spots, root rot", "Banana aphids, rhizome weevil", "Suckers", "Needs plenty of water, sunlight and rich soil. Protect from strong wind."),

    PlantData("Papaya", "Fruit Plant (Pappali)", "Moderate", "6–8 hours", "Rich, well-drained soil", "22–35°C", "50–80%", "Compost + NPK", "Every 4–6 weeks", "Remove damaged leaves", LARGE, "Garden / Terrace", "8–12 months", "Root rot, mosaic disease", "Whiteflies, aphids", "Seeds", "Needs warm weather, sunlight and excellent drainage."),

    PlantData("Pomegranate", "Fruit Shrub (Madhulai)", "Moderate", "6–8 hours", "Well-drained loamy soil", "20–35°C", "40–60%", "Compost + NPK", "Every 2–3 months", "Remove suckers and weak branches", LARGE, "Garden / Terrace", "2–4 years", "Fruit cracking, leaf spots", "Fruit borer, aphids", "Cuttings / Air layering", "Needs full sunlight and controlled watering."),

    PlantData("Sapota", "Fruit Tree (Chikku)", "Moderate", "6–8 hours", "Deep, well-drained soil", "22–35°C", "50–80%", "Compost + NPK", "Every 3 months", "Remove dry branches", GROUND, "Garden / Farm", "3–5 years", "Fruit drop, leaf spots", "Mealybugs, scale insects", "Grafting", "Provide warm weather, sunlight and good drainage."),

    PlantData("Jackfruit", "Fruit Tree (Palamaram)", "Moderate", "6–8 hours", "Deep, fertile, well-drained soil", "22–35°C", "60–90%", "Compost + Organic manure", "Every 3 months", "Remove dry and low branches", GROUND, "Garden / Farm", "4–7 years", "Root rot, fruit rot", "Mealybugs, fruit flies", "Seeds / Grafting", "Needs plenty of space, sunlight and regular watering."),

    PlantData("Custard Apple", "Fruit Tree (Seetha Pazham)", "Moderate", "6–8 hours", "Sandy loam, well-drained soil", "20–35°C", "40–70%", "Compost + NPK", "Every 2–3 months", "Remove dry branches", GROUND, "Garden / Farm", "2–4 years", "Fruit rot, leaf spots", "Mealybugs, fruit flies", "Seeds / Grafting", "Needs full sunlight and well-drained soil."),

    PlantData("Amla", "Fruit Tree (Nellikkai)", "Moderate", "6–8 hours", "Well-drained loamy soil", "20–35°C", "40–70%", "Compost + NPK", "Every 3 months", "Remove dry and crossing branches", GROUND, "Garden / Farm", "3–5 years", "Fruit drop, leaf spots", "Aphids, scale insects", "Grafting / Seeds", "Needs full sunlight and moderate watering."),

    PlantData("Pineapple", "Fruit Plant (Annasi)", "Moderate", "5–7 hours", "Sandy, well-drained soil", "20–32°C", "60–80%", "Compost + NPK", "Every 6–8 weeks", "Remove dry leaves", "14–18 inch pot", "Garden / Terrace", "12–18 months", "Root rot, leaf spots", "Mealybugs, scale insects", "Slips / Suckers", "Needs warm weather, sunlight and good drainage."),

    PlantData("Tamarind", "Fruit Tree (Puliyamaram)", "Low–Moderate", "6–8 hours", "Deep, well-drained soil", "22–35°C", "40–70%", "Organic manure / NPK", "Every 2–3 months", "Remove dead and crowded branches", GROUND, "Garden / Outdoor", "5–8+ years", "Leaf spots, slow growth", "Scale insects, caterpillars", "Seeds / Grafting", "Needs plenty of sunlight and deep well-drained soil."),

    PlantData("Cashew", "Fruit / Nut Tree (Mundhiri)", "Moderate", "6–8 hours", "Sandy, well-drained soil", "24–35°C", "50–70%", "Organic manure / NPK", "Every 2–3 months", "Remove dead branches", GROUND, "Garden / Outdoor", "3–5 years", "Poor flowering, fungal problems", "Tea mosquito bug, mealybugs", "Grafting / Seeds", "Warm weather and good sunlight are important."),

    PlantData("Jamun", "Fruit Tree (Naval Pazham)", "Moderate", "6–8 hours", "Well-drained loamy soil", "20–35°C", "50–70%", "Organic manure / NPK", "Every 2–3 months", "Remove dead and crowded branches", GROUND, "Garden / Outdoor", "4–7 years", "Leaf spots, poor fruiting", "Fruit flies, scale insects", "Seeds / Grafting", "Provide full sunlight and adequate space for root growth."),

    PlantData("Wood Apple", "Fruit / Medicinal Tree (Vilampazham)", "Low–Moderate", "6–8 hours", "Well-drained soil", "22–35°C", "40–60%", "Organic manure", "Every 2–3 months", "Remove dry branches", GROUND, "Garden / Outdoor", "4–6 years", "Slow growth, leaf spots", "Scale insects, caterpillars", "Seeds / Grafting", "A hardy Indian tree that prefers warm sunny conditions."),

    PlantData("Star Fruit", "Fruit Tree (Thamarathai)", "Moderate", "5–7 hours", "Fertile, well-drained soil", "20–32°C", "60–80%", "Organic manure / NPK", "Every 2–3 months", "Remove crowded branches", LARGE, "Garden / Large Balcony", "2–4 years", "Leaf spots, poor fruiting", "Fruit flies, aphids", "Seeds / Grafting", "Needs warm humid conditions and regular watering."),

    PlantData("Sweet Orange", "Citrus Fruit Tree", "Moderate", "6–8 hours", "Well-drained loamy soil", "20–32°C", "50–70%", "Citrus fertilizer / Compost", "Every 4–6 weeks", "Remove dead and crossing branches", LARGE, "Garden / Balcony", "3–5 years", "Leaf yellowing, fruit drop", "Aphids, scale insects, leaf miners", "Grafting / Seeds", "Needs full sunlight, regular feeding and good drainage."),

    PlantData("Mosambi", "Citrus Fruit Tree", "Moderate", "6–8 hours", "Well-drained fertile soil", "20–32°C", "50–70%", "Citrus fertilizer / Compost", "Every 4–6 weeks", "Remove dead branches", LARGE, "Garden / Balcony", "3–5 years", "Leaf yellowing, poor fruiting", "Aphids, scale insects, leaf miners", "Grafting", "Provide plenty of sunlight and avoid waterlogging."),

    PlantData("Grapes", "Fruit Vine (Thiratchai)", "Moderate", "6–8 hours", "Sandy, well-drained soil", "20–35°C", "40–60%", "Compost / NPK", "Every 4–6 weeks", "Prune vines during the resting period", "20–24 inch pot or open ground", "Terrace / Garden / Theni region", "2–3 years", "Powdery mildew, fruit cracking", "Mealybugs, aphids, fruit flies", "Stem cuttings", "Needs strong sunlight, a strong trellis and good airflow."),

    PlantData("Coconut", "Plantation Crop (Thennai)", "High", "6–8 hours", "Sandy loam, well-drained soil", "22–35°C", "60–90%", "Compost + NPK", "Every 3 months", "Remove dry fronds", GROUND, "Garden / Farm / Coastal areas", "5–7 years", "Root wilt, leaf yellowing", "Rhinoceros beetle, red palm weevil", "Seeds / Nuts", "Needs warm weather, regular watering and plenty of sunlight. Plant in a deep pit with space of about 7–8 metres between trees."),

    PlantData("Arecanut", "Plantation Palm (Pakku)", "High", "5–7 hours", "Deep, fertile, well-drained soil", "20–35°C", "60–90%", "Compost + NPK", "Every 3 months", "Remove dry fronds", GROUND, "Farm / Plantation", "5–7 years", "Yellowing, root problems", "Spindle bug, scale insects", "Seeds", "Needs warm humid conditions, regular watering and drainage."),

    PlantData("Palmyra", "Palm Tree (Panai)", "Low", "6–8 hours", "Sandy, well-drained soil", "25–40°C", "40–60%", "Organic manure (optional)", "Once a year", "Remove dry fronds", GROUND, "Coastal and dry areas / Farm", "10–15 years", "Leaf spots, bud rot", "Rhinoceros beetle, red palm weevil", "Seeds", "State tree of Tamil Nadu. Very hardy and drought tolerant."),

    // ---------------- OTHER TREES ----------------

    PlantData("Indian Almond", "Shade Tree (Nattu Vadhumai)", "Moderate", "6–8 hours", "Well-drained sandy loam", "22–35°C", "50–80%", "Compost", "Every 3–4 months", "Remove dry branches", GROUND, "Garden / Roadside", "4–8 years", "Leaf spots, root problems", "Scale insects, caterpillars", "Seeds", "Needs plenty of space, sunlight and good drainage."),

    PlantData("Teak", "Timber Tree (Thekku)", "Moderate", "6–8 hours", "Deep, well-drained loamy soil", "22–35°C", "40–70%", "Compost + NPK", "Every 3–4 months", "Remove damaged branches", GROUND, "Farm / Plantation", "8–15 years", "Leaf spots, root problems", "Defoliating caterpillars, termites", "Seeds", "Requires full sunlight, space and well-drained soil."),

    PlantData("Bamboo", "Grass / Ornamental Plant (Moongil)", "Moderate", "4–7 hours", "Rich, well-drained soil", "18–35°C", "50–80%", "Compost + NPK", "Every 2–3 months", "Remove old and weak canes", LARGE, "Garden / Outdoor", "2–5 years", "Root rot, leaf spots", "Scale insects, mites", "Rhizome division / Cuttings", "Needs regular watering, sunlight and enough root space."),

    PlantData("Banyan", "Shade Tree (Aalamaram)", "Low–Moderate", "6–8 hours", "Any well-drained soil", "20–40°C", "40–70%", "Compost", "Once a year", "Remove dead branches", GROUND, "Open land / Temple grounds", "10+ years", "Leaf spots, root damage", "Scale insects, caterpillars", "Seeds / Cuttings", "Needs a very large open space because of its spreading aerial roots."),

    PlantData("Peepal", "Sacred Shade Tree (Arasamaram)", "Low–Moderate", "6–8 hours", "Any well-drained soil", "20–40°C", "40–70%", "Compost", "Once a year", "Remove dead branches", GROUND, "Open land / Temple grounds", "10+ years", "Leaf spots, root damage", "Scale insects, caterpillars", "Seeds / Cuttings", "Needs a large open space. Keep away from buildings because roots spread widely."),

    PlantData("Pungai", "Shade Tree (Pungai)", "Low–Moderate", "6–8 hours", "Any well-drained soil, tolerates salty soil", "20–38°C", "40–70%", "Compost", "Once a year", "Remove dead branches", GROUND, "Roadside / Farm / Garden", "4–8 years", "Leaf spots, stem borer attack", "Leaf miners, caterpillars", "Seeds", "Fast growing shade tree. Seeds are used for oil."),

    PlantData("Casuarina", "Tree (Savukku)", "Low", "6–8 hours", "Sandy coastal soil", "22–38°C", "50–80%", "Rarely needed", "Once a year", "Remove lower dead branches", GROUND, "Coastal areas / Farm", "3–5 years", "Root rot, wilt", "Defoliating caterpillars, stem borers", "Seeds / Cuttings", "Fast growing tree suited to sandy coastal soil and strong winds."),

    PlantData("Eucalyptus", "Tree (Thaila Maram)", "Low–Moderate", "6–8 hours", "Well-drained loamy soil", "10–32°C", "40–70%", "Compost", "Once a year", "Remove low branches", GROUND, "Nilgiris hills / Farm", "4–8 years", "Leaf blight, stem borers", "Gall wasps, termites", "Seeds / Seedlings", "Fast growing tree that needs open space and good sunlight."),

    PlantData("Sandalwood", "Valuable Tree (Chandanam)", "Low–Moderate", "5–7 hours", "Red sandy loam, well-drained soil", "20–35°C", "40–60%", "Organic manure", "Every 3–4 months", "Prune low branches", GROUND, "Farm / Protected land", "10–15 years", "Spike disease, root problems", "Scale insects, stem borers", "Seeds", "Semi-parasitic tree that needs a host plant nearby, such as Casuarina or Cassia.")
)