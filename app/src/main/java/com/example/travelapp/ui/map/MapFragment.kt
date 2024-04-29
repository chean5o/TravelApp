package com.example.travelapp.ui.map

// import java.util.jar.Manifest
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.travelapp.R
import com.example.travelapp.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment : Fragment() {

    private var addedView: View? = null
    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*        val textView: TextView = binding.textDashboard
                mapViewModel.text.observe(viewLifecycleOwner) {
                    textView.text = it
                }*/

        // 위치 권한이 있는지 확인
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없으면 요청
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // 권한이 있으면 현재 위치 가져오기
            fetchCurrentLocation()
        }

        // 지도 초기화
        mapView = MapView(requireContext())
        binding.mapView.addView(mapView)

        // initializeKakaoMap()

        mapView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val x = event.x
                val y = event.y

                // 이전에 추가된 뷰가 있다면 삭제
                addedView?.let {
                    binding.mapView.removeView(it)
                    addedView = null
                }

                // 클릭 지점에 새로운 뷰 추가
                addViewToMap(x, y)
            }
            true
        }

        return root
    }

    private fun fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 위치 권한이 허용된 경우 현재 위치 가져오기
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                // 위치 정보 가져오기 성공
                location?.let {
                    // 지도에 현재 위치 표시
                    mapView.setMapCenterPointAndZoomLevel(
                        MapPoint.mapPointWithGeoCoord(
                            it.latitude,
                            it.longitude
                        ),
                        DEFAULT_ZOOM_LEVEL.toInt(),
                        true
                    )
                }
            }.addOnFailureListener { e ->
                // 위치 정보 가져오기 실패
                Toast.makeText(requireContext(), "Failed to get location: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 위치 권한이 거부된 경우 사용자에게 다시 권한 요청
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }


    /*    private fun initializeKakaoMap() {
            val mapView = MapView(activity)
            val mapViewContainer = binding.mapView as ViewGroup
            mapViewContainer.addView(mapView)
        }*/

    private fun addViewToMap(x: Float, y: Float) {
        // 모서리 둥근 사각형의 뷰 생성
        val view = View(requireContext())
        view.setBackgroundResource(R.drawable.rounded_rectangle_shape) // 모서리 둥근 사각형의 배경 설정
        val layoutParams = FrameLayout.LayoutParams(800, 600) // 사각형의 크기 설정
        layoutParams.leftMargin = x.toInt() - 100 // 중심 위치 설정
        layoutParams.topMargin = y.toInt() - 50
        binding.mapView.addView(view, layoutParams)

        // 추가된 뷰를 추적하는 변수 업데이트
        addedView = view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 3.0
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}