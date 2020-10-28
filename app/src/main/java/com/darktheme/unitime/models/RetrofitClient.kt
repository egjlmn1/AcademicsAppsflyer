package com.darktheme.unitime.models

import android.content.Context
import com.darktheme.unitime.AppInfo
import com.darktheme.unitime.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStream
import java.security.*
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


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
