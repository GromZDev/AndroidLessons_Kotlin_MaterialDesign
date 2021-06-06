package q3_kotlin.material_design.myPictureOfTheDay.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import q3_kotlin.material_design.myPictureOfTheDay.R
import q3_kotlin.material_design.myPictureOfTheDay.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.bottom_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> toast("Favourite")
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
        context.setSupportActionBar(binding.includedBottomBarLayoutSettings.bottomBarSettings)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomAppBar()

        changeTheme()
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

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private fun changeTheme() {
        binding.buttonOne.setOnClickListener {
            activity?.setTheme(R.style.MyPictureOfTheDay_New)
            Toast.makeText(context, "You selected MyPictureOfTheDay_New Theme!", Toast.LENGTH_SHORT)
                .show()
        }
        binding.buttonTwo.setOnClickListener {
            activity?.setTheme(R.style.Theme_MyPictureOfTheDay)
            Toast.makeText(context, "You selected Theme_MyPictureOfTheDay!", Toast.LENGTH_SHORT)
                .show()
        }
        binding.buttonChangeBgToImage.setOnClickListener {
            activity?.setTheme(R.style.Image_BG_Theme)
            Toast.makeText(context, "You selected Image_BG_Theme!", Toast.LENGTH_SHORT).show()
        }
    }
}