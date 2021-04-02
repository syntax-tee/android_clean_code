package com.taiye.ogunlade.android_clean_code

class RestaurantsViewModel {

    fun getDisplayedRestaurants(restaurants:List<Restaurant>):List<RestaurantDisplayItem>{

     return  restaurants.map { restaurant ->
         return@map RestaurantDisplayItem(
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
     }
    }
}