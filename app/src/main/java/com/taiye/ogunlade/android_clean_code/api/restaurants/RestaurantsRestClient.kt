package com.taiye.ogunlade.android_clean_code.api.restaurants

import com.taiye.ogunlade.android_clean_code.mocks.MockCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class  RestaurantsRestClient {

    private val disposable = CompositeDisposable()

    fun getRestaurants(completionHandler: (response: RestaurantListResponse) -> Unit) {

        val userId = MockCreator.getUserId()
        disposable.add(
            getMockRestaurants(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    completionHandler.invoke(response)
                }, {})
        )
    }

    fun getMockRestaurants(userId: Int) =  MockCreator.getRestaurantsResponseMock().delay(1000, TimeUnit.MILLISECONDS)


    fun stopStream(){
        disposable.dispose()
    }
}