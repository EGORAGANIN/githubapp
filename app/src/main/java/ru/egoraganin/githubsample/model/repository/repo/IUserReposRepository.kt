package ru.egoraganin.githubsample.model.repository.repo

import ru.egoraganin.githubsample.entity.repo.RepoModel

interface IUserReposRepository {

    fun getUserRepos(pageSize: Int): Listing<RepoModel>
}