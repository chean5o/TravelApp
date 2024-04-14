package com.example.my_application_retro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import android.widget.Button

// Retrofit 인터페이스 정의
interface JsonPlaceHolderApi {
    // 예시로 @GET 어노테이션과 메소드 추가
    @GET("/") // API의 엔드포인트에 맞게 수정해야 함
    fun getPosts(): Call<List<JsonObject>>
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/") // 에뮬레이터에서 로컬 서버에 접근하는 주소
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        findViewById<Button>(R.id.button_fetch_data).setOnClickListener {
            fetchPosts(jsonPlaceHolderApi)
        }
    }

    private fun fetchPosts(jsonPlaceHolderApi: JsonPlaceHolderApi) {
        jsonPlaceHolderApi.getPosts().enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful) {
                    val posts = response.body()?.joinToString { it.toString() }
                    println(posts)
                } else {
                    // 요청 실패 처리
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                // 네트워크 요청 실패 처리
                t.printStackTrace()
            }
        })
    }
}

