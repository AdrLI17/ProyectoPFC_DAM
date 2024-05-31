package com.ali.appjuegos.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url
import okhttp3.ResponseBody

interface PicsumService {
    @GET
    fun getImage(@Url url: String): Call<ResponseBody>
}

