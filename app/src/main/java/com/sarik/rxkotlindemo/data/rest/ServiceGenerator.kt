package com.sarik.rxkotlindemo.data.rest

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Mehedi Hasan on 10/20/2020.
 */
class ServiceGenerator {
    companion object {
        val BASE_URL = "https://jsonplaceholder.typicode.com"

        private val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        private val retrofit = retrofitBuilder.build()

        private val requestApi = retrofit.create(RequestApi::class.java)

        public fun getRequestApi(): RequestApi {
            return requestApi
        }
    }
}