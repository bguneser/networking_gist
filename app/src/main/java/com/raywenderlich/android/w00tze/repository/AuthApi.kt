package com.raywenderlich.android.w00tze.repository

import com.raywenderlich.android.w00tze.model.AccessToken
import retrofit2.Call
import retrofit2.http.*

interface AuthApi {

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    fun getAccessToken(@Field("client_id") clientId:String,
    @Field("client_secret") clientSecret : String,
    @Field("code") accessCode : String) : Call<AccessToken>
}