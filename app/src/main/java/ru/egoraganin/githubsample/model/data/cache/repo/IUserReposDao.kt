package ru.egoraganin.githubsample.model.data.cache.repo

import androidx.paging.DataSource
import androidx.room.*
import ru.egoraganin.githubsample.entity.repo.RepoModel

@Dao
interface IUserReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repos: List<RepoModel>)

    @Query("SELECT * FROM ${RepoModel.TABLE_NAME}")
    fun getReposDataSourceFactory(): DataSource.Factory<Int, RepoModel>

    @Query("SELECT * FROM ${RepoModel.TABLE_NAME}")
    fun getReposData(): List<RepoModel>

    @Query("DELETE FROM ${RepoModel.TABLE_NAME}")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM ${RepoModel.TABLE_NAME}")
    fun itemsCount(): Int

    @Transaction
    fun deleteAllAndInsert(repos: List<RepoModel>) {
        deleteAll()
        insert(repos)
    }
}