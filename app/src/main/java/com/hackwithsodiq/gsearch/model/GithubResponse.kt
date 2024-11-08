package com.hackwithsodiq.gsearch.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GithubResponse<T>(
    @SerializedName("total_count")
    @Expose
    val totalCount: Int,

    @SerializedName("incomplete_results")
    @Expose
    val incompleteResults: Boolean,

    @SerializedName("items")
    @Expose
    val items: List<T>,
)

data class GithubUser(
    @SerializedName("id")
    val id: Int,

    @SerializedName("login")
    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("location")
    val location: String?,

    @SerializedName("bio")
    val bio: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("blog")
    val blog: String?,

    @SerializedName("followers")
    val followers: Int,

    @SerializedName("following")
    val following: Int,

    @SerializedName("public_repos")
    val publicRepos: Int,
)

data class Owner(
    @SerializedName("login")
    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,
)

data class RepositoryItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("stargazers_count")
    val stargazersCount: Int,

    @SerializedName("language")
    val language: String?,

    @SerializedName("owner")
    val owner: Owner
)
