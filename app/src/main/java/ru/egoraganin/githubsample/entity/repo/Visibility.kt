package ru.egoraganin.githubsample.entity.repo

import com.google.gson.annotations.SerializedName

enum class Visibility(private val value: String) {
    @SerializedName("all")
    ALL("all"),
    @SerializedName("public")
    PUBLIC("public"),
    @SerializedName("private")
    PRIVATE("private");

    override fun toString() = value
}