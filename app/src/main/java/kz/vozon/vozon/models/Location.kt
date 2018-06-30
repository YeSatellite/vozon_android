package kz.vozon.vozon.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

open class Location : InfoTmp() {

    @SerializedName("region_name")
    @Expose
    var regionName: String? = null

    @SerializedName("country_name")
    @Expose
    var countryName: String? = null

    fun getShortName(): String {
        return "$name, $countryName"
    }

    fun getShortName(startDetail: String): String {
        return "$name, $countryName, $startDetail"
    }

    fun getShortXName(startDetail: String): String {
        return "$name, $startDetail"
    }

    operator fun minus(other: Location) = when {
        this.countryName != other.countryName -> "Международный"
        this.regionName != other.regionName -> "Междурегионный"
        this.name != other.name -> "Междугородный"
        else -> "Внутри Города"
    }

    override fun toString(): String {
        return """
            |User(id=$id,
            |   name=$name,
            |   regionName=$regionName,
            |   countryName=$countryName,
            |)""".trimMargin()
    }


}