package com.pdm.dailydev.model

import com.google.gson.annotations.SerializedName

data class GithubRepo(
    val name: String,

    @SerializedName("full_name")
    val fullName: String,

    val description: String?,

    @SerializedName("html_url")
    val htmlUrl: String,

    @SerializedName("stargazers_count")
    val stars: Int,

    val language: String?
)
