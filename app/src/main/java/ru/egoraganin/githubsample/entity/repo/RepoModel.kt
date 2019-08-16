package ru.egoraganin.githubsample.entity.repo

import androidx.room.*
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import ru.egoraganin.githubsample.entity.repo.RepoModel.Companion.TABLE_NAME
import ru.egoraganin.githubsample.model.data.cache.common.InstantConverter

@Entity(tableName = TABLE_NAME,
    indices = [Index(value = ["id"], unique = true)]
)
data class RepoModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_db")
    val idDb: Int = 0,
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("private")
    @ColumnInfo(name = "private")
    val isPrivate: Boolean,
    @SerializedName("fork")
    @ColumnInfo(name = "fork")
    val isFork: Boolean,
    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    @TypeConverters(InstantConverter::class)
    val updatedDate: Instant,
    @SerializedName("size")
    @ColumnInfo(name = "size")
    val sizeInKb: Long,
    @SerializedName("forks_count")
    @ColumnInfo(name = "forks_count")
    val forksCount: Long,
    @SerializedName("language")
    val language: String?,
    @SerializedName("stargazers_count")
    @ColumnInfo(name = "stargazers_count")
    val stargazersCount: Long
) {
    companion object {
        const val TABLE_NAME = "user_repos"
    }
}