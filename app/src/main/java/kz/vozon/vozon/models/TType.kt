package kz.vozon.vozon.models

import com.google.gson.annotations.Expose
import java.io.Serializable

class TType : kz.vozon.vozon.models.InfoTmp() {
    @Expose var icon: String? = null
}