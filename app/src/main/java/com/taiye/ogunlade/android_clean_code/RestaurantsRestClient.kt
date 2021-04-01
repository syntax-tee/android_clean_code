package com.taiye.ogunlade.android_clean_code

import com.taiye.ogunlade.android_clean_code.mocks.MockCreator
import java.util.concurrent.TimeUnit

class RestaurantsRestClient {
    fun getRestaurants(userId: Int) =
        MockCreator.getRestaurantsResponseMock().delay(1000, TimeUnit.MILLISECONDS)
}