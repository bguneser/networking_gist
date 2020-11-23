package com.raywenderlich.android.w00tze.viewmodel

import android.app.Application
import android.app.FragmentHostCallback
import android.arch.lifecycle.AndroidViewModel
import android.net.Uri
import com.raywenderlich.android.w00tze.BuildConfig
import com.raywenderlich.android.w00tze.BuildConfig.CLIENT_ID
import com.raywenderlich.android.w00tze.BuildConfig.CLIENT_SECRET
import com.raywenderlich.android.w00tze.app.Injection
import com.raywenderlich.android.w00tze.model.AccessToken
import com.raywenderlich.android.w00tze.model.AuthenticationPrefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val authApi = Injection.provideAuthApi()
    fun isAuthenticated() = AuthenticationPrefs.isAuthenticated()
    fun getAccessToken(uri: Uri, callback: () -> Unit) {
        val accessCode = uri.getQueryParameter("code")

        authApi.getAccessToken(BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET, accessCode)
                .enqueue(object : Callback<AccessToken> {

                    override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                        val accessToken = response?.body()?.accessToken
                        val tokenType = response?.body()?.tokenType
                        if(accessToken !=null && tokenType !=null){
                            AuthenticationPrefs.saveAuthToken(accessToken)
                            AuthenticationPrefs.saveTokenType(tokenType)
                        }
                    }

                    override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                       println("ERROR GETTING TOKEN")
                    }

                })
    }

    fun logout() {
        AuthenticationPrefs.saveAuthToken("")
        AuthenticationPrefs.clearUsername()
    }

}