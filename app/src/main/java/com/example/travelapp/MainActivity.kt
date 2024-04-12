package com.example.travelapp

import android.content.Context
import android.widget.*;
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
import com.example.travelapp.flask_server.DataModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var textView: TextView;
    private lateinit var progressBar: ProgressBar
    private lateinit var button1: Button

    lateinit var mRetrofit : Retrofit // 사용할 레트로핏 객체
    lateinit var mRetrofitAPI: RetrofitAPI // 레트로핏 api 객체
    lateinit var mCallTodoList : retrofit2.Call<JsonObject> // Json 형식의 데이터를 요청하는 객체
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
        setRetrofit()

        binding.button1.setOnClickListener {
            binding.button1.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            callTodoList()
        }

    }
    private fun callTodoList(){
        mCallTodoList = mRetrofitAPI.getTodoList() // RetrofitAPI 에서 JSON 객체를 요청해서 반환하는 메소드 호출
        mCallTodoList.enqueue(mRetrofitCallback) // 응답을 큐에 넣어 대기 시켜놓음. 즉, 응답이 생기면 뱉어낸다.
    }

    private val mRetrofitCallback = (object : retrofit2.Callback<JsonObject>{
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            val result = response.body()
            Log.d("testt", "결과는 ${result}")

            var gson = Gson()
            val dataParsed1 = gson.fromJson(result, DataModel.TodoInfo1::class.java)
            val dataParsed2 = gson.fromJson(result, DataModel.TodoInfo2::class.java)
            val dataParsed3 = gson.fromJson(result, DataModel.TodoInfo3::class.java)

            textView.text = "해야할 일\n" + dataParsed1.todo1.task+"\n"+dataParsed2.todo2.task +"\n"+dataParsed3.todo3.task

            progressBar.visibility = View.GONE
            button1.visibility = View.VISIBLE
        }

        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            t.printStackTrace()
            Log.d("testt", "에러입니다. ${t.message}")
            binding.textView.text = "에러입니다. ${t.message}"

            binding.progressBar.visibility = View.GONE
            binding.button1.visibility = View.VISIBLE
        }
    })
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
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
    fun setRetrofit(context: Context) {
        //레트로핏으로 가져올 url설정하고 세팅
        mRetrofit = Retrofit
            .Builder()
            .baseUrl(context.getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //인터페이스로 만든 레트로핏 api요청 받는 것 변수로 등록
        mRetrofitAPI = mRetrofit.create(RetrofitAPI::class.java)
    }
}