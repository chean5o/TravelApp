package com.example.travelapp.ui.makecourse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.travelapp.R
import com.example.travelapp.VoteResult
import com.example.travelapp.VoteService
import com.example.travelapp.databinding.FragmentMakecourseBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import android.os.Bundle
//import android.view.View
//import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import androidx.core.content.ContextCompat
import android.widget.ArrayAdapter
import android.widget.Spinner


class MakeCourseFragment : Fragment() {

    private var voteResults = mutableMapOf<Int, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트 레이아웃을 인플레이트합니다.
        return inflater.inflate(R.layout.fragment_makecourse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 슬라이더 인스턴스를 찾음
//        val slider = view.findViewById<Slider>(R.id.budgetSlider)

//        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
//        radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            when (checkedId) {
//                R.id.radioMale -> {
//                    // 남성이 선택되었을 때의 로직
//                }
//                R.id.radioFemale -> {
//                    // 여성이 선택되었을 때의 로직
//                }
//            }
//        }

        // Retrofit 인스턴스 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/") // 본인의 서버 URL로 변경하세요
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val voteService = retrofit.create(VoteService::class.java)

        // 투표 결과를 저장할 MutableMap 생성
        val voteResults = mutableMapOf<Int, String>()

        // 버튼 클릭 리스너 설정
        val buttonIds = listOf(R.id.yesButton1, R.id.noButton1, R.id.yesButton2, R.id.noButton2, R.id.yesButton3, R.id.noButton3, R.id.yesButton4, R.id.noButton4)
        buttonIds.forEach { id ->
            view.findViewById<Button>(id).setOnClickListener { buttonView ->
                val answer = if (buttonView.tag.toString().startsWith("yes")) "Y" else "N"
                val questionId = buttonView.tag.toString().filter { it.isDigit() }.toInt()
                Log.d("MakeCourseFragment", "Button clicked: Question ID $questionId, Answer $answer")
                voteResults[questionId] = answer

                Toast.makeText(context, "질문 $questionId: $answer 선택됨", Toast.LENGTH_SHORT).show()

                // 모든 질문에 대한 응답이 완료되었는지 확인
                if (voteResults.size == buttonIds.size / 2) { // 4개의 질문에 대한 답변이 모두 있어야 함
                    sendVoteResults(voteResults, voteService)
                }
            }
        }
    }

    private fun sendVoteResults(voteResults: MutableMap<Int, String>, voteService: VoteService) {
        val voteResultsList = voteResults.map { VoteResult(it.key, it.value) }

        val call = voteService.sendVoteResults(voteResultsList)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "투표 결과가 성공적으로 전송되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "투표 결과 전송에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "투표 결과 전송 중 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}