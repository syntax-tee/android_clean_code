package com.taiye.ogunlade.android_clean_code

import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.taiye.ogunlade.android_clean_code.mocks.MockCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_restaurants.*
import java.util.*

class RestaurantsActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()
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
        disposable.dispose()
    }

    private fun getRestaurants(completionHandler: (response: RestaurantListResponse) -> Unit) {

        val client = RestaurantsRestClient()
        val userId = MockCreator.getUserId()
        disposable.add(
            client.getRestaurants(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    completionHandler.invoke(response)
                }, {})
        )
    }

    private fun showRestaurants() {
        getRestaurants { response ->
            val parsedRestaurants = parseRestaurants(response)
            val filteredRestaurants = filterRestaurants(parsedRestaurants)
            displayRestaurants(filteredRestaurants)
        }
    }

    private fun displayRestaurants(filteredRestaurants: ArrayList<Restaurant>) {
        val displayRestaurants = arrayListOf<RestaurantDisplayItem>()
        filteredRestaurants.forEach { restaurant ->
            displayRestaurants.add(
                RestaurantDisplayItem(
                    id = restaurant.id,
                    displayName = "Restaurant ${restaurant.name}",
                    displayDistance = "at ${restaurant.distance} KM distance",
                    imageUrl = restaurant.imageUrl,
                    type = when (restaurant.type) {
                        "EAT_IN" -> RestaurantsType.EAT_IN
                        "TAKE_AWAY" -> RestaurantsType.TAKE_AWAY
                        else -> RestaurantsType.DRIVE_THROUGH
                    }
                )
            )
        }

        val adapter = restaurantsAdapter
        if (adapter != null) {
            adapter.restaurants = displayRestaurants
            adapter.clickListener =
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

    private fun filterRestaurants(restaurants: List<Restaurant>): ArrayList<Restaurant> {
        val filteredRestaurants = arrayListOf<Restaurant>()
        for (parsedRestaurant in restaurants) {
            if (parsedRestaurant.closingHour < 6)
                filteredRestaurants.add(parsedRestaurant)
        }

        restaurants.filter { restaurant ->
            restaurant.closingHour < 6
        }.map { restaurants ->
            val userLat = MockCreator.getUserLatitude()
            val userLong = MockCreator.getUserLongitude()

            val distance = FloatArray(2)

            Location.distanceBetween(
                userLat,
                userLong,
                restaurants.location.latitude,
                restaurants.location.longitude,
                distance
            )

            val distanceResult = distance[0].toInt() / 1000
            restaurants.distance = distanceResult
            return@map restaurants
        }.sortedBy { restaurant -> restaurant.distance }

        // val latitude = MockCreator.getUserLatitude()
        for (filteredRestaurant in filteredRestaurants) {

        }
        Collections.sort(filteredRestaurants, RestaurantDistanceSorter())
        return filteredRestaurants
    }

    private fun parseRestaurants(response: RestaurantListResponse): List<Restaurant> {
        return response.restaurants?.filter { restaurantResponse ->
            restaurantResponse.name != null && restaurantResponse.imageUrl != null
        }?.map { restaurantResponse ->
            val location = SimpleLocation(
                restaurantResponse.locationLatitude,
                restaurantResponse.locationLatitude
            )
            return@map Restaurant(
                id = restaurantResponse.id,
                name = restaurantResponse.name!!,
                imageUrl = restaurantResponse.imageUrl!!,
                closingHour = restaurantResponse.closingHour,
                location = location,
                type = restaurantResponse.type
            )

        }.orEmpty()
    }
}