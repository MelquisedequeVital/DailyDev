package com.pdm.dailydev.repository

import com.pdm.dailydev.data.remote.GithubApi
import com.pdm.dailydev.data.remote.RetrofitInstance
import com.pdm.dailydev.model.GithubRepo
import retrofit2.HttpException
import java.io.IOException


class GithubRepository (
    private val api: GithubApi = RetrofitInstance.api
) {
    suspend fun searchRepositories(
        query: String
    ): Result<List<GithubRepo>> {
        return try {
            val response = api.searchRepositories(query)

            Result.success(response.items)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}