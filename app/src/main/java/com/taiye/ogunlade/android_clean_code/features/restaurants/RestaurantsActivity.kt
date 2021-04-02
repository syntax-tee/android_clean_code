package com.taiye.ogunlade.android_clean_code.features.restaurants

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.taiye.ogunlade.android_clean_code.R
import com.taiye.ogunlade.android_clean_code.Restaurant
import com.taiye.ogunlade.android_clean_code.data.restaurants.RestaurantParser
import com.taiye.ogunlade.android_clean_code.api.restaurants.RestaurantsRestClient
import com.taiye.ogunlade.android_clean_code.business.restaurants.RestaurantsRules
import kotlinx.android.synthetic.main.activity_restaurants.*
import java.util.*

class RestaurantsActivity : AppCompatActivity() {

    private val restaurantClient = RestaurantsRestClient()
    private var restaurantsAdapter: RestaurantsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)
        restaurantsAdapter = RestaurantsAdapter()
        recyclerViewRestaurants.apply {
            layoutManager = LinearLayoutManager(
                context!!,
                LinearLayoutManager.VERTICAL,
                false
            )
            this.adapter = restaurantsAdapter
        }
        showRestaurants()
    }

    override fun onDestroy() {
        super.onDestroy()
        restaurantClient.stopStream()
    }


    private fun showRestaurants() {
        restaurantClient.getRestaurants { response ->
            //Parsing, Filtering, Displaying
            val restaurantParser = RestaurantParser();
            val restaurantsRules = RestaurantsRules()
            val parsedRestaurants = restaurantParser.parseRestaurants(response)
            val filteredRestaurants = restaurantsRules.filterRestaurants(parsedRestaurants)
            displayRestaurants(filteredRestaurants)
        }
    }

    private fun displayRestaurants(restaurants: ArrayList<Restaurant>) {
        val viewModel = RestaurantsViewModel()
        restaurantsAdapter!!.restaurants = viewModel.getDisplayedRestaurants(restaurants)
        restaurantsAdapter!!.clickListener =
            object : RestaurantsAdapter.RestaurantClickListener {
                override fun onRestaurantClicked(restaurantId: Int) {
                    Toast.makeText(
                        this@RestaurantsActivity,
                        "Pressed a restaurant!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}