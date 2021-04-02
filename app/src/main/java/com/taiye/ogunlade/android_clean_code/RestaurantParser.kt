package com.taiye.ogunlade.android_clean_code

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