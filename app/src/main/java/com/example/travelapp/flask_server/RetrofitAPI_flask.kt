package com.example.travelapp.flask_server

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
//data class DataModel(var result:String? = null)
interface RetrofitAPI_flask {
    @GET("/") // 서버에 GET 요청을 할 주소 입력
    fun getRequest():Call<String> // json 파일을 가져오는 메소드
}