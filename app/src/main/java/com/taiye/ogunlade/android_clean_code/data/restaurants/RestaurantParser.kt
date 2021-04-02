package com.taiye.ogunlade.android_clean_code.data.restaurants

import com.taiye.ogunlade.android_clean_code.Restaurant
import com.taiye.ogunlade.android_clean_code.SimpleLocation
import com.taiye.ogunlade.android_clean_code.api.restaurants.RestaurantListResponse

class RestaurantParser {

     fun parseRestaurants(response: RestaurantListResponse): List<Restaurant> {
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