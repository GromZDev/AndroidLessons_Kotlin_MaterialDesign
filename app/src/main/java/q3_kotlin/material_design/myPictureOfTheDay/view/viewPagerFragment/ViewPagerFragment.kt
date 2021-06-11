package q3_kotlin.material_design.myPictureOfTheDay.view.viewPagerFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.relex.circleindicator.CircleIndicator
import q3_kotlin.material_design.myPictureOfTheDay.R
import q3_kotlin.material_design.myPictureOfTheDay.databinding.FragmentViewPagerBinding
import q3_kotlin.material_design.myPictureOfTheDay.utils.ZoomOutPageTransformer


class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)

// ===================== Сетим адаптер, заголовок и индикатор =======================
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager) // Сетим адаптер

        binding.tabLayout.setupWithViewPager(binding.viewPager)// Сетим заголовок

        val indicator : CircleIndicator = binding.indicator // Сетим индикатор (точки)
        indicator.setViewPager(binding.viewPager)
// =================================================================================

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.setPageTransformer(true, ZoomOutPageTransformer())
        setIconsInTabLayout()
        setCustomTabs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ViewPagerFragment()
    }

    private fun setIconsInTabLayout() {
        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_earth)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_mars)
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_system)
    }

    @SuppressLint("InflateParams")
    private fun setCustomTabs() {
        val layoutInflater = LayoutInflater.from(context)
        binding.tabLayout.getTabAt(0)?.customView =
            layoutInflater.inflate(R.layout.tab_image_layout_view_pager_earth, null)
        binding.tabLayout.getTabAt(1)?.customView =
            layoutInflater.inflate(R.layout.tab_image_layout_view_pager_mars, null)
        binding.tabLayout.getTabAt(2)?.customView =
            layoutInflater.inflate(
                R.layout.tab_image_layout_view_pager_nasa_gallery,
                null
            )
    }


}