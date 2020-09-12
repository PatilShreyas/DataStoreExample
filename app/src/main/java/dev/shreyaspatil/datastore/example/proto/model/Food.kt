package dev.shreyaspatil.datastore.example.proto.model

data class Food(
    val name: String,
    val type: FoodType,
    val taste: FoodTaste
)

enum class FoodTaste {
    SWEET, SPICY
}

enum class FoodType {
    VEG, NON_VEG
}