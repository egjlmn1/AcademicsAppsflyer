package com.darktheme.unitime.models.Retrofit

import android.os.Environment

class CheckForSDCard {
    //Check If SD Card is present or not method
    val isSDCardPresent: Boolean
        get() = if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED
            )
        ) {
            true
        } else false
}