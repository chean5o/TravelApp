package com.example.travelapp.flask_server

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
data class ResponseDC(var result:String? = null)
interface RetrofitAPI {
    /*node*/
    @GET("/")
    fun getRequest(@Query("name") name: String): Call<ResponseDC>

    @FormUrlEncoded
    @POST("/")
    fun postRequest(@Field("id")id: String,
                    @Field("password")password: String):Call<ResponseDC>

    @FormUrlEncoded
    @PUT("/{id}")
    fun putRequest(@Path("id")id: String,
                   @Field("content")content: String): Call<ResponseDC>

    @DELETE("/{id}")
    fun deleteRequest(@Path("id")id: String): Call<ResponseDC>
}