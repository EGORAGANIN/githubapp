package ru.egoraganin.githubsample.entity.repo

import com.google.gson.annotations.SerializedName

enum class Direction(private val value: String) {
    @SerializedName("asc")
    ASC("asc"),
    @SerializedName("desc")
    DESC("desc");

    override fun toString() = value
}
