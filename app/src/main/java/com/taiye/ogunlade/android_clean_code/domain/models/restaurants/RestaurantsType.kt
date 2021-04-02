package com.taiye.ogunlade.android_clean_code.domain.models.restaurants

import com.taiye.ogunlade.android_clean_code.R

enum class RestaurantsType(val text:String,val drawableId:Int,val textColorId:Int) {

    TAKE_AWAY("Take Away", R.drawable.take_away, R.color.orange),
    EAT_IN("Eat In", R.drawable.eat_in, R.color.brown),
    DRIVE_THROUGH("Drive T", R.drawable.drive_through, R.color.colorAccent)
}