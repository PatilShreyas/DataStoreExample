package dev.shreyaspatil.datastore.example.proto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.shreyaspatil.datastore.example.R
import dev.shreyaspatil.datastore.example.proto.data.getFoodList
import dev.shreyaspatil.datastore.example.proto.model.Food
import dev.shreyaspatil.datastore.example.proto.model.FoodTaste
import dev.shreyaspatil.datastore.example.proto.model.FoodType
import kotlinx.android.synthetic.main.activity_datastore_proto.*
import kotlinx.coroutines.launch

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class ProtoDatastoreActivity : AppCompatActivity() {

    private lateinit var foodPreferenceManager: FoodPreferenceManager

    private val foodListAdapter by lazy { FoodListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datastore_proto)

        foodPreferenceManager = FoodPreferenceManager(applicationContext)

        observePreferences()
        initFoodList()
        initViews()

        loadData()
    }

    private fun initFoodList() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProtoDatastoreActivity)
            adapter = foodListAdapter
        }
    }

    private fun loadData() {
        foodListAdapter.submitList(getFoodList())
    }

    private fun observePreferences() {
        foodPreferenceManager.userFoodPreference.asLiveData().observe(this) {
            filterFoodList(it.type, it.taste)
        }
    }

    private fun filterFoodList(type: FoodType?, taste: FoodTaste?) {
        var filteredList = getFoodList()
        type?.let { foodType ->
            filteredList = filteredList.filter { it.type == foodType }
        }
        taste?.let { foodTaste ->
            filteredList = filteredList.filter { it.taste == foodTaste }
        }

        foodListAdapter.submitList(filteredList)

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No results!", Toast.LENGTH_SHORT).show()
        }

        updateViews(type, taste)
    }

    private fun initViews() {
        foodTaste.setOnCheckedChangeListener { group, checkedId ->
            val taste = when (checkedId) {
                R.id.sweet -> FoodTaste.SWEET
                R.id.spicy -> FoodTaste.SPICY
                else -> null
            }

            lifecycleScope.launch { foodPreferenceManager.updateUserFoodTastePreference(taste) }
        }

        foodType.setOnCheckedChangeListener { group, checkedId ->
            val type = when (checkedId) {
                R.id.veg -> FoodType.VEG
                R.id.nonVeg -> FoodType.NON_VEG
                else -> null
            }

            lifecycleScope.launch { foodPreferenceManager.updateUserFoodTypePreference(type) }
        }
    }


    private fun updateViews(type: FoodType?, taste: FoodTaste?) {
        when (type) {
            FoodType.VEG -> veg.isChecked = true
            FoodType.NON_VEG -> nonVeg.isChecked = true
        }

        when (taste) {
            FoodTaste.SWEET -> sweet.isChecked = true
            FoodTaste.SPICY -> spicy.isChecked = true
        }
    }
}

class FoodListAdapter : ListAdapter<Food, FoodListAdapter.FoodItemViewHolder>(DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        return FoodItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FoodItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(food: Food) {
            itemView.findViewById<TextView>(R.id.textFoodName).text = food.name
            itemView.findViewById<TextView>(R.id.textFoodType).run {
                text = food.type.name
                setTextColor(
                    ContextCompat.getColor(
                        itemView.context, when (food.type) {
                            FoodType.NON_VEG -> android.R.color.holo_red_dark
                            FoodType.VEG -> android.R.color.holo_green_dark
                        }
                    )
                )
            }

            itemView.findViewById<TextView>(R.id.textFoodTaste).run {
                text = food.taste.name
                setTextColor(
                    ContextCompat.getColor(
                        itemView.context, when (food.taste) {
                            FoodTaste.SWEET -> android.R.color.holo_blue_light
                            FoodTaste.SPICY -> android.R.color.holo_orange_dark
                        }
                    )
                )
            }
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Food>() {
            override fun areItemsTheSame(oldItem: Food, newItem: Food) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Food, newItem: Food) =
                oldItem.name == newItem.name
        }
    }
}