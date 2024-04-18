package com.example.travelapp

import android.content.Context
import android.widget.*
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.travelapp.flask_server.RetrofitAPI
import com.example.travelapp.databinding.ActivityMainBinding
//import com.example.travelapp.flask_server.DataModel
import com.example.travelapp.flask_server.ResponseDC
import com.example.travelapp.flask_server.RetrofitAPI_flask
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    //---node.js 연동---
    val url = "http://172.30.82.94:3000"
    val url_flask ="http://172.30.82.94:5000"
    val retrofit = Retrofit.Builder()
//        .baseUrl(getString(R.string.nodeUrl))//ip 변경시 바꿔줘야함
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var server = retrofit.create(RetrofitAPI::class.java)
    val retrofit2 = Retrofit.Builder()
//        .baseUrl(getString(R.string.nodeUrl))//ip 변경시 바꿔줘야함
        .baseUrl(url_flask)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var server2 = retrofit2.create(RetrofitAPI_flask::class.java)
    ///---여기까지
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_map, R.id.navigation_home, R.id.navigation_makecourse
            )
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        print(server2.getRequest())
//node.js 내용 호출
        // Post 요청
        server.postRequest("id", "password").enqueue(object:Callback<ResponseDC> {
            override fun onFailure(call: Call<ResponseDC>?, t: Throwable) {
                // 실패 처리
            }

            override fun onResponse(call: Call<ResponseDC>?, response: Response<ResponseDC>?) {
                // 성공적으로 응답을 받았을 때 로그로 확인
                if (response != null) {
                    Log.d("response : ", response.body().toString())
                }
            }
        })

// Update 요청
        server.putRequest("board", "내용").enqueue(object : Callback<ResponseDC> {
            override fun onFailure(call: Call<ResponseDC>, t: Throwable) {
                // 실패 처리
            }

            override fun onResponse(call: Call<ResponseDC>, response: Response<ResponseDC>) {
                // 성공적으로 응답을 받았을 때 로그로 확인
                Log.d("response : ", response.body().toString())
            }
        })

// Delete 요청
        server.deleteRequest("board").enqueue(object : Callback<ResponseDC> {
            override fun onFailure(call: Call<ResponseDC>, t: Throwable) {
                // 실패 처리
            }

            override fun onResponse(call: Call<ResponseDC>, response: Response<ResponseDC>) {
                // 성공적으로 응답을 받았을 때 로그로 확인
                Log.d("response : ", response.body().toString())
            }
        })

        server2.getRequest().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val serverResponse = response.body()
                    // 서버로부터 받은 문자열을 사용하여 원하는 작업을 수행합니다.
                    Log.d("Server Response", serverResponse ?: "Response is null")
                } else {
                    Log.e("Server Response", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Server Response", "Failed to connect to server: ${t.message}", t)
            }
        })
    }




//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_map, R.id.navigation_home, R.id.navigation_makecourse
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//    }

}