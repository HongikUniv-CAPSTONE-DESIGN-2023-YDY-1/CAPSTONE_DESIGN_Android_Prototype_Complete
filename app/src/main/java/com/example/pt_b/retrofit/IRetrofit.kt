package com.example.pt_b.retrofit


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IRetrofit {
    @GET("/konbini/items")
    fun searchItems(
        @Query("name") name: String,
        @Query("strength") strength: String

    ): Call<ResultSearchKeyword>
}