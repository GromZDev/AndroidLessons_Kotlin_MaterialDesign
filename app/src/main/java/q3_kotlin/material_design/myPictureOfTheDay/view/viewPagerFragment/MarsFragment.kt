package q3_kotlin.material_design.myPictureOfTheDay.view.viewPagerFragment

import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import q3_kotlin.material_design.myPictureOfTheDay.R
import q3_kotlin.material_design.myPictureOfTheDay.databinding.FragmentMarsStartBinding
import q3_kotlin.material_design.myPictureOfTheDay.model.MarsRoverPhotosData
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.MarsRoverAppState
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel.MarsRoverViewModel
import java.time.LocalDate
import java.util.*

class MarsFragment : Fragment(R.layout.fragment_mars_start) {

    private var _binding: FragmentMarsStartBinding? = null
    private val binding get() = _binding!!

    private val marsViewModel: MarsRoverViewModel by lazy {
        ViewModelProvider(this).get(MarsRoverViewModel::class.java)
    }

    private var show = false // флаг для анимации

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsStartBinding.inflate(inflater, container, false)

        binding.marsFragmentImageView.setOnClickListener {
            if (show) hideComponents() else
                showComponents()
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//==================== Для отображения фото передаём вчерашнюю дату! ==================
        marsViewModel.getData(getYesterdayData().toString()).observe(viewLifecycleOwner,
            { renderMarsData(it) })
//=====================================================================================
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //============================ Для отображения Анимации! ==========================
    private fun showComponents() {
        show = true
        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.fragment_mars)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200
        TransitionManager.beginDelayedTransition(binding.constraintContainer,
            transition)
        constraintSet.applyTo(binding.constraintContainer)
    }

    private fun hideComponents() {
        show = false
        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.fragment_mars_start)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200
        TransitionManager.beginDelayedTransition(binding.constraintContainer,
            transition)
        constraintSet.applyTo(binding.constraintContainer)
    }
//=====================================================================================

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

                    binding.groupFragmentMars.visibility = View.VISIBLE
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
        binding.groupFragmentMars.visibility = View.GONE
        binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

}