package com.hackwithsodiq.gsearch.api

import com.hackwithsodiq.gsearch.BuildConfig
import com.hackwithsodiq.gsearch.model.GithubResponse
import com.hackwithsodiq.gsearch.model.GithubUser
import com.hackwithsodiq.gsearch.model.RepositoryItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GitHubAPI {
    @GET("/search/repositories")
    suspend fun searchRepos(@Query("q") q: String): Response<GithubResponse<RepositoryItem>>

    @GET("/search/users")
    suspend fun searchUsers(@Query("q") q: String): Response<GithubResponse<GithubUser>>

    @GET("/users/{username}")
    suspend fun fetchAUser(@Path("username") username: String): Response<GithubUser>

    @GET("https://api.github.com/users/{login}/repos")
    suspend fun fetchUserRepositories(@Path("login") username: String): Response<List<RepositoryItem>>
}

class ApiService {
    inline fun <reified T> create(baseUrl:String): T {
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(100, TimeUnit.SECONDS)
        httpClient.callTimeout(100, TimeUnit.SECONDS)
        httpClient.readTimeout(100, TimeUnit.SECONDS)
        httpClient.writeTimeout(100, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val restInterceptor = HttpLoggingInterceptor()
            restInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(restInterceptor)
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(T::class.java)
    }
}