package ru.egoraganin.githubsample.model.data.server

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.egoraganin.githubsample.entity.repo.*

interface IUserRestService {

    @GET("user/repos")
    suspend fun getRepos(
        @Query("visibility") visibility: Visibility,
        @Query("affiliation", encoded = true) affiliation: SequenceParameters<Affiliation>,
        @Query("sort") sort: Sort,
        @Query("direction") direction: Direction,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = DEFAULT_PER_PAGE
    ): Response<List<RepoModel>>

    companion object {

        private const val DEFAULT_PER_PAGE = 20
    }
}