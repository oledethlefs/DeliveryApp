package com.example.deliveryapp.core.data.model

import com.example.deliveryapp.R

val fritzKolaSingle05 = Product(
    id = 3,
    name = "fritz-kola",
    brand = "fritz",
    price = 199,
    reducedPrice = 199,
    deposit = 15,
    quantity = "0,5l (Mehrweg)",
    quantityPrice = "(1 l = 3,98€)",
    description = "Koffeinhaltige Limonade",
    ingredients = "Wasser, Zucker, Kohlensäure, Farbstoff (Ammonsulfit-Zuckerkulör), Säuerungsmittel (Phosphorsäure), Koffein, natürliches Aroma.",
    information = "Erhöhter Koffeingehalt. Für Kinder und schwangere oder stillende Frauen nicht empfohlen (25 mg/100 ml)",
    imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.fritz_0_5,
    calories = 40,
    carbs = 9.9f,
    sugar = 9.9f,
    protein = 0f,
    fat = 0f,
    saturatedFat = 0f,
    sodium = 0f,
    otherQuantities = listOf(),
    nutriScore = "E"
)

val fritzKolaBox033 = Product(
    id = 1,
    name = "fritz-kola",
    brand = "fritz",
    price = 2799,
    reducedPrice = 2799,
    deposit = 342,
    quantity = "24 x 0,33l (Mehrweg)",
    quantityPrice = "(1 l = 3,53€)",
    description = "Koffeinhaltige Limonade",
    ingredients = "Wasser, Zucker, Kohlensäure, Farbstoff (Ammonsulfit-Zuckerkulör), Säuerungsmittel (Phosphorsäure), Koffein, natürliches Aroma.",
    information = "Erhöhter Koffeingehalt. Für Kinder und schwangere oder stillende Frauen nicht empfohlen (25 mg/100 ml)",
    imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.fritz_kasten_0_33,
    calories = 40,
    carbs = 9.9f,
    sugar = 9.9f,
    protein = 0f,
    fat = 0f,
    saturatedFat = 0f,
    sodium = 0f,
    otherQuantities = listOf(),
    nutriScore = "E"
)

val fritzKolaBox05 = Product(
    id = 2,
    name = "fritz-kola",
    brand = "fritz",
    price = 1479,
    reducedPrice = 1179,
    deposit = 299,
    quantity = "10 x 0,5l (Mehrweg)",
    quantityPrice = "(1 l = 2,36€)",
    description = "Koffeinhaltige Limonade",
    ingredients = "Wasser, Zucker, Kohlensäure, Farbstoff (Ammonsulfit-Zuckerkulör), Säuerungsmittel (Phosphorsäure), Koffein, natürliches Aroma.",
    information = "Erhöhter Koffeingehalt. Für Kinder und schwangere oder stillende Frauen nicht empfohlen (25 mg/100 ml)",
    imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.fritz_kasten_0_5,
    calories = 40,
    carbs = 9.9f,
    sugar = 9.9f,
    protein = 0f,
    fat = 0f,
    saturatedFat = 0f,
    sodium = 0f,
    otherQuantities = listOf(),
    nutriScore = "E"
)
val fritzKolaSingle033 = Product(
    id = 0,
    name = "fritz-kola",
    brand = "fritz",
    price = 149,
    reducedPrice = 149,
    deposit = 8,
    quantity = "0,33l (Mehrweg)",
    quantityPrice = "(1 l = 4,52€)",
    description = "Koffeinhaltige Limonade",
    ingredients = "Wasser, Zucker, Kohlensäure, Farbstoff (Ammonsulfit-Zuckerkulör), Säuerungsmittel (Phosphorsäure), Koffein, natürliches Aroma.",
    information = "Erhöhter Koffeingehalt. Für Kinder und schwangere oder stillende Frauen nicht empfohlen (25 mg/100 ml)",
    imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.fritz_0_33,
    calories = 40,
    carbs = 9.9f,
    sugar = 9.9f,
    protein = 0f,
    fat = 0f,
    saturatedFat = 0f,
    sodium = 0f,
    otherQuantities = listOf(fritzKolaSingle05, fritzKolaBox033, fritzKolaBox05),
    nutriScore = "E"
)

val steakRecipe = Recipe(
    id = 1,
    name = "Rinder-Steaks mit Kartoffeln und Bohnen",
    preparationTime = 35,
    preparationDescription = listOf(
        "In einem Topf ca. 2,5 l Salzwasser zugedeckt aufkochen. Steaks waschen, trocken tupfen und zum Temperieren beiseitelegen. Kartoffeln waschen und schälen",
        "Wenn das Salzwasser kocht, die Kartoffeln darin ca. 8 Min. köcheln. Anschließend mit einem Schaumlöffel entnehmen, in einem Sieb kalt abschrecken und auf Küchenkrepp abtropfen lassen. Das Salzwasser erneut aufkochen. "
    ),
    difficulty = 1,
    popularity = 4.5f,
    servings = 1,
    ingredients = listOf(
        Ingredient("Minutensteaks", "g", 250),
        Ingredient("Kartoffeln, vorwieg. festkochend", "g", 200),
        Ingredient("Bohnen", "g", 200),
        Ingredient("Öl", "EL", 2),
        Ingredient("Salz", "Prise", 1)
    ),
    imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.recipe_example,
    calories = 748,
    carbs = 65,
    protein = 58,
    fat = 27,
    fiber = 10
)
val spaghettiRecipe = Recipe(
    id = 2,
    name = "Spaghetti Bolognese",
    preparationTime = 20,
    preparationDescription = listOf(
    ),
    difficulty = 0,
    popularity = 4.4f,
    servings = 2,
    ingredients = emptyList(),
    imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.recipe_example2,
    calories = 450,
    carbs = 65,
    protein = 12,
    fat = 8,
    fiber = 4
)