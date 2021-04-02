package com.taiye.ogunlade.android_clean_code.business.restaurants

import android.location.Location
import com.taiye.ogunlade.android_clean_code.Restaurant
import com.taiye.ogunlade.android_clean_code.RestaurantDistanceSorter
import com.taiye.ogunlade.android_clean_code.mocks.MockCreator
import java.util.*

class RestaurantsRules {


     fun filterRestaurants(restaurants: List<Restaurant>): ArrayList<Restaurant> {
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

}