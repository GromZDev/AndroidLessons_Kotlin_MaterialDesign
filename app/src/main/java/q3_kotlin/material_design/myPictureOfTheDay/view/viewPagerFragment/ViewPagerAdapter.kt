package q3_kotlin.material_design.myPictureOfTheDay.view.viewPagerFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(private val fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager){

    private val fragments: LinkedHashMap<String, Fragment> = linkedMapOf(
        "Earth" to EpicFragment(), "Mars" to MarsFragment(), "Nasa" to NasaGalleryFragment()
    )

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments["Earth"]!!
            1 -> fragments["Mars"]!!
            2 -> fragments["Nasa"]!!
            else -> fragments["Earth"]!!
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Earth"
            1 -> "Mars"
            2 -> "Nasa"
            else -> "Earth"
        }

    }
}