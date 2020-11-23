/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.raywenderlich.android.w00tze.app


import com.raywenderlich.android.w00tze.BuildConfig
import com.raywenderlich.android.w00tze.model.AuthenticationPrefs
import com.raywenderlich.android.w00tze.repository.AuthApi
import com.raywenderlich.android.w00tze.repository.GitHupApi
import com.raywenderlich.android.w00tze.repository.RemoteRepository
import com.raywenderlich.android.w00tze.repository.Repository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Injection {
  fun provideRepository(): Repository = RemoteRepository

  private fun provideRetrofit(): Retrofit {

    return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideHttpClient()).build()
  }

  private  fun provideLoggingInterceptor(): HttpLoggingInterceptor{
    val logging = HttpLoggingInterceptor()
    logging.level = if(BuildConfig.DEBUG){
      HttpLoggingInterceptor.Level.BODY
    }else{
      HttpLoggingInterceptor.Level.NONE
    }
    return logging
  }

  private fun provideHttpClient() : OkHttpClient{
    val httpClient = OkHttpClient.Builder()
    httpClient.addInterceptor(provideLoggingInterceptor())

    httpClient.addInterceptor{chain -> val request = chain
            .request()
            .newBuilder()
            .addHeader("Authorization","token ${AuthenticationPrefs.getAuthToken()}").build()
      chain.proceed(request)
    }
    return httpClient.build()
  }

  fun provideGitHubApi() : GitHupApi {
    return  provideRetrofit().create(GitHupApi::class.java)
  }

  private fun provideAuthRetrofit():Retrofit {
    return Retrofit.Builder()
            .baseUrl("https://github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
  }

  fun provideAuthApi():AuthApi{
    return provideAuthRetrofit().create(AuthApi::class.java)
  }





}