package kz.vozon.vozon.ui.info

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.Api
import kz.vozon.vozon.utility.Shared

class LocationActivity : AppCompatActivity() {
    companion object {
        const val EXTRA = "extra"
    }

    var state = 0

    val i = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Shared.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        next(0)
    }

    fun next(id: Long) {
        val selectedFragment = when (state) {
            0 -> kz.vozon.vozon.ui.info.LocationFragment(Api.infoService.country())
            1 -> kz.vozon.vozon.ui.info.LocationFragment(Api.infoService.region(id))
            2 -> kz.vozon.vozon.ui.info.CityFragment(Api.infoService.city(id))
            3 -> {
                if (intent.getStringExtra(EXTRA) == EXTRA)
                    kz.vozon.vozon.ui.info.LocationExtraFragment()
                else {
                    setResult(Activity.RESULT_OK, i)
                    finish()
                    return
                }
            }
            4 -> {
                setResult(Activity.RESULT_OK, i)
                finish()
                return
            }
            else -> return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.v_container, selectedFragment)
        transaction.commit()
        state++
    }
}
