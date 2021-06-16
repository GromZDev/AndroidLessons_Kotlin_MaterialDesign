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
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import q3_kotlin.material_design.myPictureOfTheDay.BuildConfig
import q3_kotlin.material_design.myPictureOfTheDay.R
import q3_kotlin.material_design.myPictureOfTheDay.databinding.FragmentEpicBinding
import q3_kotlin.material_design.myPictureOfTheDay.model.EpicPhotosData
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.EpicAppState
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel.EpicViewModel
import java.time.LocalDate

class EpicFragment : Fragment(R.layout.fragment_epic) {

    private var _binding: FragmentEpicBinding? = null
    private val binding get() = _binding!!

    private val epicViewModel: EpicViewModel by lazy {
        ViewModelProvider(this).get(EpicViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpicBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        epicViewModel.getData(getYesterdayData().toString()).observe(viewLifecycleOwner,
            { renderEpicData(it) })

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

    private fun renderEpicData(data: EpicAppState) {
        when (data) {
            is EpicAppState.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData[0].date
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {

                    binding.groupFragmentEpic.visibility = View.VISIBLE
                    binding.epicIncludedLoadingLayout.loadingLayout.visibility = View.GONE
                    showSuccess(serverResponseData[0])

                    setEpicDataToTextView(serverResponseData)

                }
            }
            is EpicAppState.Loading -> {
                showLoading()
            }
            is EpicAppState.Error -> {
                binding.epicIncludedLoadingLayout.loadingLayout.visibility = View.GONE
                toast("Error in loading img or URL")
            }
        }
    }

    private fun setEpicDataToTextView(serverResponseData: List<EpicPhotosData>) {

        binding.twTitleFragmentEpic.text = serverResponseData[0].date

        binding.twDescriptionFragmentEpic.text = serverResponseData[0].caption

    }

    private fun showSuccess(url: EpicPhotosData) {
        val year = url.date.substring(0, 4)
        val month = url.date.substring(5, 7)
        val day = url.date.substring(8, 10)
        val image = url.image
        val newUrl =
            "https://api.nasa.gov/EPIC/archive/natural/$year/$month/$day/png/$image" + ".png?api_key=" + BuildConfig.NASA_API_KEY
        binding.epicFragmentImageView.load(newUrl) {
            lifecycle(this@EpicFragment)
            error(R.drawable.ic_launcher_background)
        }
    }

    private fun showLoading() {
        binding.groupFragmentEpic.visibility = View.GONE
        binding.epicIncludedLoadingLayout.loadingLayout.visibility = View.VISIBLE
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }


}