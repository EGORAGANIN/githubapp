package ru.egoraganin.githubsample.entity.repo

import com.google.gson.annotations.SerializedName

enum class Sort(private val value: String) {
    @SerializedName("created")
    CREATED("created"),
    @SerializedName("updated")
    UPDATED("updated"),
    @SerializedName("pushed")
    PUSHED("pushed"),
    @SerializedName("full_name")
    FULL_NAME("full_name");

    override fun toString() = value
}