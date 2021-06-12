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
import q3_kotlin.material_design.myPictureOfTheDay.databinding.FragmentNasaGalleryBinding
import q3_kotlin.material_design.myPictureOfTheDay.model.NasaImageGalleryData
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.NasaGalleryAppState
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel.NasaGalleryViewModel


class NasaGalleryFragment : Fragment(R.layout.fragment_nasa_gallery) {

    private var _binding: FragmentNasaGalleryBinding? = null
    private val binding get() = _binding!!

    private val nasaGalleryViewModel: NasaGalleryViewModel by lazy {
        ViewModelProvider(this).get(NasaGalleryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNasaGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        nasaGalleryViewModel.getData().observe(viewLifecycleOwner,
            { renderNasaGalleryData(it) })

    }

    private fun renderNasaGalleryData(data: NasaGalleryAppState?) {
        when (data) {
            is NasaGalleryAppState.Success -> {
                val serverResponseData = data.serverResponseData

                binding.nasaGalleryImageView.visibility = View.VISIBLE
                binding.twDescriptionFragmentNasaGallery.visibility = View.VISIBLE
                binding.includedLoadingLayoutNasaGallery.loadingLayout.visibility = View.GONE

                showSuccessNasaGallery(serverResponseData)

                setNasaGalleryDataToTextView()

            }
            is NasaGalleryAppState.Loading -> {
                showLoadingNasaGallery()
            }
            is NasaGalleryAppState.Error -> {
                binding.includedLoadingLayoutNasaGallery.loadingLayout.visibility = View.GONE
                toast("Error in loading img or URL")
            }
        }
    }

    private fun showSuccessNasaGallery(serverResponseData: NasaImageGalleryData) {
        val text = serverResponseData.rrr?.items?.get(2)?.href.toString()
        val formattedText = text.substring(4)
        binding.nasaGalleryImageView.load("https".plus(formattedText)) {
            lifecycle(this@NasaGalleryFragment)
            error(R.drawable.ic_launcher_background)
        }
    }

    private fun showLoadingNasaGallery() {
        binding.nasaGalleryImageView.visibility = View.GONE
        binding.includedLoadingLayoutNasaGallery.loadingLayout.visibility = View.VISIBLE
    }

    private fun setNasaGalleryDataToTextView() {
        val text = "Nasa Gallery"
        binding.twTitleFragmentNasaGallery.text = text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }
}