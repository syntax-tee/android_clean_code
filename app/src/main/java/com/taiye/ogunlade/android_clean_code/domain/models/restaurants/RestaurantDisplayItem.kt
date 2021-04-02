package com.taiye.ogunlade.android_clean_code.domain.models.restaurants

import com.taiye.ogunlade.android_clean_code.domain.models.restaurants.RestaurantsType

data class RestaurantDisplayItem(
    val id: Int,
    val displayName: String,
    val displayDistance: String,
    val imageUrl: String,
    var type: RestaurantsType
)