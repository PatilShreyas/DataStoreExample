package dev.shreyaspatil.datastore.example.proto.data

import dev.shreyaspatil.datastore.example.proto.model.Food
import dev.shreyaspatil.datastore.example.proto.model.FoodTaste
import dev.shreyaspatil.datastore.example.proto.model.FoodType

fun getFoodList() = listOf(
    Food("Paneer Masala", FoodType.VEG, FoodTaste.SPICY),
    Food("Gulab Jamoon", FoodType.VEG, FoodTaste.SWEET),
    Food("Chicken Lollipop", FoodType.NON_VEG, FoodTaste.SPICY),
    Food("Fish Fry", FoodType.NON_VEG, FoodTaste.SPICY),
    Food("Veg Mix", FoodType.VEG, FoodTaste.SPICY),
    Food("Aloo Sabji", FoodType.VEG, FoodTaste.SWEET),
    Food("Papad", FoodType.VEG, FoodTaste.SPICY),
    Food("Rasgulla", FoodType.VEG, FoodTaste.SWEET),
    Food("Shev Bhaji", FoodType.VEG, FoodTaste.SPICY),
    Food("Puran Poli", FoodType.VEG, FoodTaste.SWEET),
    Food("Pani Puri", FoodType.VEG, FoodTaste.SPICY),
    Food("Veg Manchurian", FoodType.VEG, FoodTaste.SPICY),
    Food("Chicken Manchurian", FoodType.NON_VEG, FoodTaste.SPICY),
    Food("Chhole", FoodType.VEG, FoodTaste.SPICY),
    Food("Biryani", FoodType.NON_VEG, FoodTaste.SPICY),
)