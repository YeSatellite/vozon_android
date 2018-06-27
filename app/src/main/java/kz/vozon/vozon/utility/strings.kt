package kz.vozon.vozon.utility

import android.app.Activity
import kz.vozon.vozon.R

fun Activity.yesOrNo(boolean: Boolean?): String {
    return if (boolean == true) getString(R.string.yes)
    else getString(R.string.no)
}