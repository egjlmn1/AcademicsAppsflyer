package com.darktheme.unitime.models.Retrofit

import com.darktheme.unitime.AppInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient() {

    companion object {
        var retrofit : Retrofit? = null

        fun getInstance() : Retrofit? {
            if (retrofit == null) {
                val builder: Retrofit.Builder = Retrofit.Builder()
                    .baseUrl(AppInfo.serverUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                retrofit = builder.build()
            }
            return retrofit
        }
    }
}
