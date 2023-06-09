package com.example.pt_b.retrofit


import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface IRetrofit {
    @GET("/konbini/items")
    fun searchItems(
        @Query("name") name: String,
        @Query("strength") strength: String

    ): Call<ResultSearchKeyword>

    @Multipart
    @POST("/konbini/items/image")
    fun postImage(
        @Part image: MultipartBody.Part
    ): Call<ResultSearchKeyword>


}