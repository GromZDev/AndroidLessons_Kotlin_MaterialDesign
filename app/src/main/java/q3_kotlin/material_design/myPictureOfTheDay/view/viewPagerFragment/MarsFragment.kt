package q3_kotlin.material_design.myPictureOfTheDay.view.viewPagerFragment

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import q3_kotlin.material_design.myPictureOfTheDay.R
import q3_kotlin.material_design.myPictureOfTheDay.databinding.FragmentMarsBinding
import q3_kotlin.material_design.myPictureOfTheDay.model.MarsRoverPhotosData
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.MarsRoverAppState
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel.MarsRoverViewModel
import java.time.LocalDate
import java.util.*

class MarsFragment : Fragment(R.layout.fragment_mars) {

    private var _binding: FragmentMarsBinding? = null
    private val binding get() = _binding!!

    private val marsViewModel: MarsRoverViewModel by lazy {
        ViewModelProvider(this).get(MarsRoverViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//==================== Для отображения фото передаём вчерашнюю дату! ==================
        marsViewModel.getData(getYesterdayData().toString()).observe(viewLifecycleOwner,
            Observer { renderMarsData(it) })
//=====================================================================================
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getYesterdayData(): LocalDate? {
        val date = LocalDate.now()
        return date.minusDays(1)
    }

    private fun renderMarsData(data: MarsRoverAppState) {
        when (data) {
            is MarsRoverAppState.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.photos[1]?.img_src
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {

                    binding.marsFragmentImageView.visibility = View.VISIBLE
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                    showSuccess(url)

                    setMarsRoverDataToTextView(serverResponseData)

                }
            }
            is MarsRoverAppState.Loading -> {
                showLoading()
            }
            is MarsRoverAppState.Error -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                toast("Error in loading img or URL")
            }
        }
    }

    private fun setMarsRoverDataToTextView(serverResponseData: MarsRoverPhotosData) {
        binding.twTitleFragmentMars.text =
            serverResponseData.photos[1]?.earth_date
        binding.twDescriptionFragmentMars.text =
            serverResponseData.photos[1]?.rover?.name.plus(": ")
                .plus(serverResponseData.photos[1]?.rover?.status)
    }

    private fun showSuccess(url: String?) {
        binding.marsFragmentImageView.load(url) {
            lifecycle(this@MarsFragment)
            error(R.drawable.ic_launcher_background)
        }
    }

    private fun showLoading() {
        binding.marsFragmentImageView.visibility = View.GONE
        binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

}