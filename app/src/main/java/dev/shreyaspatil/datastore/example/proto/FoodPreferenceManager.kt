package dev.shreyaspatil.datastore.example.proto

import android.content.Context
import android.util.Log
import androidx.datastore.CorruptionException
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import dev.shreyaspatil.datastore.example.proto.model.FoodTaste
import dev.shreyaspatil.datastore.example.proto.model.FoodType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

data class UserFoodPreference(
    val type: FoodType?,
    val taste: FoodTaste?
)

class FoodPreferenceManager(context: Context) {
    private val dataStore: DataStore<FoodPreferences> =
        context.createDataStore(
            fileName = "food_prefs.pb",
            serializer = FoodPreferenceSerializer
        )

    val userFoodPreference = dataStore.data.catch {
        if (it is IOException) {
            Log.e(TAG, "Error reading sort order preferences.", it)
            emit(FoodPreferences.getDefaultInstance())
        } else {
            throw it
        }
    }.map {
        val type = when (it.type) {
            FoodPreferences.FoodType.TYPE_VEG -> FoodType.VEG
            FoodPreferences.FoodType.TYPE_NON_VEG -> FoodType.NON_VEG
            else -> null
        }
        val taste = when (it.taste) {
            FoodPreferences.FoodTaste.TASTE_SWEET -> FoodTaste.SWEET
            FoodPreferences.FoodTaste.TASTE_SPICY -> FoodTaste.SPICY
            else -> null
        }

        UserFoodPreference(type, taste)
    }

    suspend fun updateUserFoodTypePreference(type: FoodType?) {
        val foodType = when (type) {
            FoodType.VEG -> FoodPreferences.FoodType.TYPE_VEG
            FoodType.NON_VEG -> FoodPreferences.FoodType.TYPE_NON_VEG
            null -> FoodPreferences.FoodType.TYPE_UNSPECIFIED
        }

        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setType(foodType)
                .build()
        }
    }

    suspend fun updateUserFoodTastePreference(taste: FoodTaste?) {
        val foodTaste = when (taste) {
            FoodTaste.SWEET -> FoodPreferences.FoodTaste.TASTE_SWEET
            FoodTaste.SPICY -> FoodPreferences.FoodTaste.TASTE_SPICY
            null -> FoodPreferences.FoodTaste.TASTE_UNSPECIFIED
        }

        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setTaste(foodTaste)
                .build()
        }
    }
    suspend fun updateUserFoodPreference(type: FoodType?, taste: FoodTaste?) {
        val foodType = when (type) {
            FoodType.VEG -> FoodPreferences.FoodType.TYPE_VEG
            FoodType.NON_VEG -> FoodPreferences.FoodType.TYPE_NON_VEG
            null -> FoodPreferences.FoodType.TYPE_UNSPECIFIED
        }

        val foodTaste = when (taste) {
            FoodTaste.SWEET -> FoodPreferences.FoodTaste.TASTE_SWEET
            FoodTaste.SPICY -> FoodPreferences.FoodTaste.TASTE_SPICY
            null -> FoodPreferences.FoodTaste.TASTE_UNSPECIFIED
        }

        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setType(foodType)
                .setTaste(foodTaste)
                .build()
        }
    }

    companion object {
        const val TAG = "FoodPreferenceManager"
    }
}

object FoodPreferenceSerializer : Serializer<FoodPreferences> {
    override fun readFrom(input: InputStream): FoodPreferences {
        try {
            return FoodPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: FoodPreferences, output: OutputStream) = t.writeTo(output)
}