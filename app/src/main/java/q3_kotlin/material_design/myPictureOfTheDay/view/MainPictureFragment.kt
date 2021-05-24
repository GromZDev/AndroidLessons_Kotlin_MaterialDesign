package q3_kotlin.material_design.myPictureOfTheDay.view

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import q3_kotlin.material_design.myPictureOfTheDay.R
import q3_kotlin.material_design.myPictureOfTheDay.databinding.FragmentMainPictureBinding
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.PODAppState
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel.PictureOfTheDayViewModel


class MainPictureFragment : Fragment() {

    private var _binding: FragmentMainPictureBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainPictureBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner,
            Observer { renderData(it) })

// == Юзер сможет по нажатию на иконку переходить в браузер и открывать запрос в «Википедии»: ==
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
// =============================================================================================
    }

    private fun renderData(data: PODAppState) {
        when (data) {
            is PODAppState.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    binding.mainImageView.visibility = View.VISIBLE
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                    showSuccess(url)

                    binding.inputEditText.setTextColor(resources.getColor(R.color.white))
                }
            }
            is PODAppState.Loading -> {
                showLoading()
            }
            is PODAppState.Error -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                toast("Error in loading img or URL")
            }
        }
    }

    private fun showSuccess(url: String?) {
//Coil в работе: достаточно вызвать у нашего ImageView нужную extension-функцию и передать
// ссылку и заглушки для placeholder
        binding.mainImageView.load(url) {
            lifecycle(this@MainPictureFragment)
            error(R.drawable.ic_launcher_background)
          //  placeholder(R.drawable.ic_no_photo_img)
        }
    }

    private fun showLoading() {
        binding.mainImageView.visibility = View.GONE
        binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
    }


    companion object {
        fun newInstance() = MainPictureFragment()
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }
}