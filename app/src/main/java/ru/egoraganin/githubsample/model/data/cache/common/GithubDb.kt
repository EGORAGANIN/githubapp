package ru.egoraganin.githubsample.model.data.cache.common

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.egoraganin.githubsample.entity.repo.RepoModel
import ru.egoraganin.githubsample.model.data.cache.repo.IUserReposDao

@Database(
    entities = [RepoModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(InstantConverter::class)
abstract class GithubDb: RoomDatabase() {

    abstract fun getUserReposDb(): IUserReposDao
}