package com.example.dump.network


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.dump.util.Constants.BASEURL_TMDB
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitHelper{

    fun getInstance(context: Context?):Retrofit {

        val mHttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        //chucker
        val interceptor = ChuckerInterceptor.Builder(context!!).alwaysReadResponseBody(true).build()


        val mOkHttpClient = OkHttpClient.Builder()
                            .connectTimeout(30,TimeUnit.SECONDS)
                            .readTimeout(30,TimeUnit.SECONDS)
                            .writeTimeout(30,TimeUnit.SECONDS)
                            .addInterceptor(mHttpLoggingInterceptor)
                            .addInterceptor(interceptor)
                            .build()

        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASEURL_TMDB)
                .client(mOkHttpClient)
                .build()
    }

}
