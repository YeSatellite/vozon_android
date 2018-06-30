package kz.vozon.vozon.utility

import android.app.Activity
import android.content.SharedPreferences
import kz.vozon.vozon.R
import kz.vozon.vozon.models.InfoTmp
import retrofit2.Call

object Shared {

    const val action = "action"
    object Action {
        const val acceptOffer = "accept_offer"
        const val done = "done"
        const val newOrder= "new_order"
        const val responseOrder= "response_order"
    }

    const val user = "user"
    const val title = "title"

    const val type = "type"
    object Type {
        const val country = "country"
        const val region = "region"
        const val city = "city"
    }

    const val posted = "posted"
    const val waiting = "waiting"
    const val active = "active"

    var call: Call<List<kz.vozon.vozon.models.InfoTmp>>? = null
    var list: List<kz.vozon.vozon.models.MultiInfo> = java.util.ArrayList()

    object Filter{
        private val country = arrayListOf<InfoTmp>()
        private val region = arrayListOf<InfoTmp>()
        private val city = arrayListOf<InfoTmp>()

        fun list() = arrayListOf<InfoTmp>().apply {
            this.addAll(country)
            this.addAll(region)
            this.addAll(city)
        }

        fun remove(location: InfoTmp){
            country.remove(location)
            region.remove(location)
            city.remove(location)
        }

        fun add(location: InfoTmp,type: String){
            when (type){
                Type.country -> country.add(location)
                Type.region -> region.add(location)
                Type.city -> city.add(location)
            }
        }

        fun countryList() = country.joinToString(separator = ",",transform = {it.id.toString()})
        fun regionList() = region.joinToString(separator = ",",transform = {it.id.toString()})
        fun cityList() = city.joinToString(separator = ",",transform = {it.id.toString()})
    }

    var preferences: SharedPreferences? = null
        private set
    var currentUser = kz.vozon.vozon.models.User()
        set(value) {
            field = value

            val editor = preferences!!.edit()
            editor.putString(user, field.toJson())
            editor.apply()
        }

    fun init(preferences: SharedPreferences) {
        Shared.preferences = preferences
        currentUser = kz.vozon.vozon.models.User.fromJson(preferences.getString(user, "{}"))
        token = preferences.getString("token", "")
    }

    var theme: Int = R.style.AppTheme
    var themeNo: Int = R.style.AppTheme_NoActionBar

    fun setTheme(){
        when (currentUser.type){
            kz.vozon.vozon.models.User.CLIENT -> {
                Shared.theme = R.style.AppTheme
                Shared.themeNo = R.style.AppTheme_NoActionBar
            }
            kz.vozon.vozon.models.User.COURIER -> {
                Shared.theme = R.style.AppThemeDark
                Shared.themeNo = R.style.AppThemeDark_NoActionBar
            }
        }
    }

    var token: String = ""
        set(value) {
            field = value

            val editor = preferences!!.edit()
            editor.putString("token", field)
            editor.apply()
        }

    fun Activity.currencies(): ArrayList<kz.vozon.vozon.models.Country> {
        val n = resources.getStringArray(R.array.currency_n)
        val s = resources.getStringArray(R.array.currency_s)
        val list = ArrayList<kz.vozon.vozon.models.Country>()
        for (i in n.indices){
            list.add(kz.vozon.vozon.models.Country(n[i], s[i]))
        }
        return list
    }

}

