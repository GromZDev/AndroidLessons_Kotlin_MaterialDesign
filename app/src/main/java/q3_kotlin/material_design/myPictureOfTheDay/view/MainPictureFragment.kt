package q3_kotlin.material_design.myPictureOfTheDay.view

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import q3_kotlin.material_design.myPictureOfTheDay.R
import q3_kotlin.material_design.myPictureOfTheDay.databinding.FragmentMainPictureBinding
import q3_kotlin.material_design.myPictureOfTheDay.model.PictureServerResponseData


import q3_kotlin.material_design.myPictureOfTheDay.view.notesFragment.NotesFragment
import q3_kotlin.material_design.myPictureOfTheDay.view.viewPagerFragment.ViewPagerFragment
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.appState.PODAppState
import q3_kotlin.material_design.myPictureOfTheDay.viewModel.mainViewModel.PictureOfTheDayViewModel


class MainPictureFragment : Fragment() {

    private var _binding: FragmentMainPictureBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    // ============== Ищем и сетим нижнее меню Bottom Bar ===========================
// ============== Обязательно вставляем его в лэйаут фрагмента!!! ===============
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.bottom_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notes_fav -> goToNotesFragment()
            R.id.app_bar_fav -> goToViewPagerFragment()
            R.id.app_bar_settings -> goToSettingsFragment()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.includedBottomBarLayout.bottomBar)
        setHasOptionsMenu(true)
    }
// ==============================================================================

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
            { renderData(it) })

        setBottomSheetBehavior(binding.includedBottomSheetLayout.bottomSheetContainer)

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        setBottomAppBar()

        setFabButtonToShowDetails()

        setAndActivateChips()

        setSwipeBehaviourInTextInputLayout() // Вьюха должна быть внутри Coordinator Layout!!!

    }

    private fun setSwipeBehaviourInTextInputLayout() {
        val swipe: SwipeDismissBehavior<TextInputLayout?> = SwipeDismissBehavior<TextInputLayout?>()
        swipe.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_END_TO_START)

        val textView = binding.inputLayout
        val coordinatorParams: CoordinatorLayout.LayoutParams =
            textView.layoutParams as CoordinatorLayout.LayoutParams
        coordinatorParams.behavior = swipe

        swipe.listener = object : SwipeDismissBehavior.OnDismissListener {
            override fun onDismiss(view: View?) {
                toast("My View Swiped !!! =)")

            }
            override fun onDragStateChanged(state: Int) {
                toast("My View Dragged !!! =)")
            }
        }
    }

    private fun setAndActivateChips() {
        binding.chipHdPhoto.setOnCheckedChangeListener { _, _ ->
            if (binding.chipHdPhoto.isChecked) {
                binding.chipHdPhoto.let {
                    viewModel.getData().observe(viewLifecycleOwner,
                        { showHdPhoto(it) })
                }
            }
        }
        binding.chipSimplePhoto.setOnCheckedChangeListener { _, _ ->
            if (binding.chipSimplePhoto.isChecked) {
                binding.chipSimplePhoto.let {
                    viewModel.getData().observe(viewLifecycleOwner,
                        { renderData(it) })
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showHdPhoto(data: PODAppState) {
        when (data) {
            is PODAppState.Success -> {
                val serverResponseData = data.serverResponseData
                val hdUrl = serverResponseData.hdurl
                if (hdUrl.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    if (hdUrl.contains("youtube.com") || hdUrl.contains("player.vimeo.com")) {

                        // Если у нас видео приходит, то предлагаем открыть Ютуб!
                        startActivity(Intent(Intent.ACTION_VIEW).apply {
                            setData(Uri.parse(hdUrl))
                        })
                        binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                        setDataToBottomSheet(serverResponseData)

                    } else {
                        binding.mainImageView.visibility = View.VISIBLE
                        binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                        binding.mainImageView.load(hdUrl) {
                            lifecycle(this@MainPictureFragment)
                            error(R.drawable.ic_launcher_background)

                        }
                        binding.inputLayout.visibility = View.GONE

                    }
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


    private fun renderData(data: PODAppState) {
        when (data) {
            is PODAppState.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    if (url.contains("youtube.com") || url.contains("player.vimeo.com")) {

                        // Если у нас видео приходит, то предлагаем открыть Ютуб!
                        startActivity(Intent(Intent.ACTION_VIEW).apply {
                            setData(Uri.parse(url))
                        })
                        binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                        binding.mainImageView.visibility = View.GONE
                        setDataToBottomSheet(serverResponseData)

                    } else {
                        binding.mainImageView.visibility = View.VISIBLE
                        binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                        binding.inputLayout.visibility = View.VISIBLE
                        showSuccess(url)

                        binding.inputEditText.setTextColor(resources.getColor(R.color.white))

                        setDataToBottomSheet(serverResponseData)
                    }
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
        private var isMain = true
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    // ===================== Метод инициации bottomSheet и его поведения =====================
    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // Изначально положение скрыто!

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> toast("Bottom sheet DRAGGING")
                    BottomSheetBehavior.STATE_COLLAPSED -> toast("Bottom sheet COLLAPSED")
                    BottomSheetBehavior.STATE_EXPANDED -> toast("Bottom sheet EXPANDED")
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> toast("Bottom sheet HALF_EXPANDED")
                    BottomSheetBehavior.STATE_HIDDEN -> toast("Bottom sheet HIDDEN")
                    BottomSheetBehavior.STATE_SETTLING -> toast("Bottom sheet SETTLING")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })

    }
// ========================================================================================

    private fun setDataToBottomSheet(serverResponseData: PictureServerResponseData) {

        binding.includedBottomSheetLayout.bottomSheetDescription.text =
            serverResponseData.explanation

// ================================ сетим текст через span ================================
        val spannableTextTitle = SpannableString(serverResponseData.title)
        spannableTextTitle.setSpan(
            ForegroundColorSpan(Color.BLUE),
            0,
            1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        ).let {
            spannableTextTitle.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.bottom_bar_bg_color)),
                spannableTextTitle.length-3,
                spannableTextTitle.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.includedBottomSheetLayout.bottomSheetDescriptionHeader.text = spannableTextTitle

// ========================================================================================
    }

    private fun setFabButtonToShowDetails() {
        val showDetailsButton = binding.includedBottomBarLayout.bottomBarFab
        showDetailsButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            val bottomBar = binding.includedBottomBarLayout.bottomBar
            if (isMain) {
                isMain = false
                changeBottomBarMenuWhileClickOnMainScreen(bottomBar, showDetailsButton)
            } else {
                isMain = true
                changeBottomBarMenuWhileClickOnNOTMainScreen(bottomBar, showDetailsButton)
            }

        }
    }

    private fun changeBottomBarMenuWhileClickOnNOTMainScreen(
        bottomBar: BottomAppBar,
        showDetailsButton: FloatingActionButton
    ) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomBar.navigationIcon =
            context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1,
                    R.drawable.ic_baseline_menu
                )
            }
        bottomBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        showDetailsButton.setImageDrawable(context?.let { it1 ->
            ContextCompat.getDrawable(
                it1,
                R.drawable.ic_baseline_read_more
            )
        })
        bottomBar.replaceMenu(R.menu.bottom_bar_menu)
    }

    private fun changeBottomBarMenuWhileClickOnMainScreen(
        bottomBar: BottomAppBar,
        showDetailsButton: FloatingActionButton
    ) {
        bottomBar.navigationIcon = null
        bottomBar.fabAlignmentMode =
            BottomAppBar.FAB_ALIGNMENT_MODE_END
        showDetailsButton.setImageDrawable(
            context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1,
                    R.drawable.ic_baseline_arrow_back
                )
            })
        bottomBar.replaceMenu(R.menu.bottom_bar_menu_not_main)
    }

    private fun goToSettingsFragment() {
        val manager = activity?.supportFragmentManager
        manager?.let {
            manager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    SettingsFragment.newInstance()
                )
                .addToBackStack("")
                .commit()
        }
    }

    private fun goToViewPagerFragment() {
        val manager = activity?.supportFragmentManager
        manager?.let {
            manager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    ViewPagerFragment.newInstance()
                )
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    private fun goToNotesFragment() {
        val manager = activity?.supportFragmentManager
        manager?.let {
            manager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    NotesFragment.newInstance()
                )
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

}