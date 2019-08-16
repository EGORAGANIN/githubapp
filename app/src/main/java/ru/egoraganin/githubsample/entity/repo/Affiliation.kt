package ru.egoraganin.githubsample.entity.repo

import com.google.gson.annotations.SerializedName

enum class Affiliation(private val value: String) {
    @SerializedName("owner")
    OWNER("owner"),
    @SerializedName("collaborator")
    COLLABORATOR("collaborator"),
    @SerializedName("organization_member")
    ORGANIZATION_MEMBER("organization_member");

    override fun toString() = value
}