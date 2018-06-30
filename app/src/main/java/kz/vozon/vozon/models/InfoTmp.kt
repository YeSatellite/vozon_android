package kz.vozon.vozon.models

import com.google.gson.annotations.Expose
import java.io.Serializable

open class InfoTmp : Serializable {
    constructor()
    constructor(id: Long? = null, name: String? = null) {
        this.id = id
        this.name = name
    }

    @Expose var id: Long? = null
    @Expose var name: String? = null
}

class MultiInfo : InfoTmp() {
    var isChecked = false

    companion object {
        fun toMultiInfo(infoTmp: InfoTmp): MultiInfo {
            val multiInfo = MultiInfo()
            multiInfo.id = infoTmp.id
            multiInfo.name = infoTmp.name
            return multiInfo
        }
    }
}