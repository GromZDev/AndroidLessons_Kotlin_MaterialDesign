package q3_kotlin.material_design.myPictureOfTheDay.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import q3_kotlin.material_design.myPictureOfTheDay.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.fragment_container,
                            MainPictureFragment.newInstance()
                    )
                    .commit()
        }

    }
}