package q3_kotlin.material_design.myPictureOfTheDay.view.viewPagerFragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.*
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
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

    private var isExpanded = false // для анимашки увеличения фото
    private var toRightAnimation = false // для анимации движения текстполя


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

// ========================= Сетим увеличение фото ========================
        setImageAnimationExpand()
// ========================================================================

// ======================== Сетим увеличение текста =======================
        setTextViewAnimation()
// ========================================================================

    }

    private fun setTextViewAnimation() {
        binding.twDescriptionFragmentNasaGallery2.setOnClickListener {
            val changeBounds = ChangeBounds()
            changeBounds.pathMotion = ArcMotion()
            changeBounds.duration = 800
            TransitionManager.beginDelayedTransition(
                binding.twDescriptionFragmentNasaGallery,
                changeBounds
            )
            toRightAnimation = !toRightAnimation
            val size = binding.twDescriptionFragmentNasaGallery2 as TextView
            val params =
                binding.twDescriptionFragmentNasaGallery2.layoutParams as FrameLayout.LayoutParams

            if (toRightAnimation) {
                params.gravity = Gravity.TOP
                size.setTextColor(Color.GREEN)
            } else {
                params.gravity = Gravity.CENTER
                size.setTextColor(Color.DKGRAY)
            }


            binding.twDescriptionFragmentNasaGallery2.layoutParams = params
        }
    }

    private fun setImageAnimationExpand() {
        val imageView = binding.nasaGalleryImageView
        imageView.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                binding.container, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )
            val params: ViewGroup.LayoutParams = imageView.layoutParams
            params.height =
                if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else
                    ViewGroup.LayoutParams.WRAP_CONTENT
            imageView.layoutParams = params
            imageView.scaleType =
                if (isExpanded) ImageView.ScaleType.CENTER_CROP else
                    ImageView.ScaleType.FIT_CENTER
        }
    }

    private fun renderNasaGalleryData(data: NasaGalleryAppState?) {
        when (data) {
            is NasaGalleryAppState.Success -> {
                val serverResponseData = data.serverResponseData

                binding.groupFragmentNasaGallery.visibility = View.VISIBLE
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
        binding.groupFragmentNasaGallery.visibility = View.GONE
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