package com.pdm.dailydev.data.remote

import com.pdm.dailydev.model.RepoSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String
    ): RepoSearchResponse
}