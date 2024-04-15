package com.example.travelapp.ui.makecourse

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.travelapp.R
import com.example.travelapp.databinding.FragmentMakecourseBinding
import com.example.travelapp.flask_server.DataModel
import com.example.travelapp.flask_server.RetrofitAPI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MakeCourseFragment : Fragment() {

    private var _binding: FragmentMakecourseBinding? = null
    private lateinit var textView: TextView;
    private lateinit var progressBar: ProgressBar
    private lateinit var button1: Button

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var mRetrofit : Retrofit // 사용할 레트로핏 객체
    lateinit var mRetrofitAPI: RetrofitAPI // 레트로핏 api 객체
    lateinit var mCallTodoList : retrofit2.Call<JsonObject> // Json 형식의 데이터를 요청하는 객체
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val makeCourseViewModel =
//            ViewModelProvider(this).get(MakeCourseViewModel::class.java)
//
//        _binding = FragmentMakecourseBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textNotifications
//        makeCourseViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        setRetrofit()
//
//        binding.button1.setOnClickListener {
//            binding.button1.visibility = View.INVISIBLE
//            binding.progressBar.visibility = View.VISIBLE
//            callTodoList()
//        }
//        return root
//    }
//
//        private val mRetrofitCallback = (object : retrofit2.Callback<JsonObject>{
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                val result = response.body()
//                Log.d("testt", "결과는 ${result}")
//
//                var gson = Gson()
//                val dataParsed1 = gson.fromJson(result, DataModel.TodoInfo1::class.java)
//                val dataParsed2 = gson.fromJson(result, DataModel.TodoInfo2::class.java)
//                val dataParsed3 = gson.fromJson(result, DataModel.TodoInfo3::class.java)
//
//                textView.text = "해야할 일\n" + dataParsed1.todo1.task+"\n"+dataParsed2.todo2.task +"\n"+dataParsed3.todo3.task
//
//                progressBar.visibility = View.GONE
//                button1.visibility = View.VISIBLE
//            }
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                t.printStackTrace()
//                Log.d("testt", "에러입니다. ${t.message}")
//                binding.textView.text = "에러입니다. ${t.message}"
//
//                binding.progressBar.visibility = View.GONE
//                binding.button1.visibility = View.VISIBLE
//            }
//        })
//
//
//    private fun callTodoList(){
//        mCallTodoList = mRetrofitAPI.getTodoList() // RetrofitAPI 에서 JSON 객체를 요청해서 반환하는 메소드 호출
//        mCallTodoList.enqueue(mRetrofitCallback) // 응답을 큐에 넣어 대기 시켜놓음. 즉, 응답이 생기면 뱉어낸다.
//    }
//    private fun setRetrofit(){
//        //레트로핏으로 가져올 url설정하고 세팅
//        mRetrofit = Retrofit
//            .Builder()
//            .baseUrl(getString(R.string.baseUrl))
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        //인터페이스로 만든 레트로핏 api요청 받는 것 변수로 등록
//        mRetrofitAPI = mRetrofit.create(RetrofitAPI::class.java)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    val makeCourseViewModel =
        ViewModelProvider(this).get(MakeCourseViewModel::class.java)

    _binding = FragmentMakecourseBinding.inflate(inflater, container, false)
    val root: View = binding.root

    // TextView 초기화
    textView = binding.textNotifications

    makeCourseViewModel.text.observe(viewLifecycleOwner) {
        textView.text = it
    }

    // 나머지 코드
    setRetrofit()

    binding.button1.setOnClickListener {
        binding.button1.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
        callTodoList()
    }

    return root
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

    private fun callTodoList(){
        mCallTodoList = mRetrofitAPI.getTodoList() // RetrofitAPI 에서 JSON 객체를 요청해서 반환하는 메소드 호출
        mCallTodoList.enqueue(mRetrofitCallback) // 응답을 큐에 넣어 대기 시켜놓음. 즉, 응답이 생기면 뱉어낸다.
    }

    private fun setRetrofit(){
        //레트로핏으로 가져올 url설정하고 세팅
        mRetrofit = Retrofit
            .Builder()
            .baseUrl(getString(R.string.baseUrl))//ip 변경시 바꿔줘야함
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //인터페이스로 만든 레트로핏 api요청 받는 것 변수로 등록
        mRetrofitAPI = mRetrofit.create(RetrofitAPI::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}