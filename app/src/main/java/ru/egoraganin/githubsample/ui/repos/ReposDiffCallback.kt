package ru.egoraganin.githubsample.ui.repos

import androidx.recyclerview.widget.DiffUtil
import ru.egoraganin.githubsample.entity.repo.RepoModel

class ReposDiffCallback: DiffUtil.ItemCallback<RepoModel>() {

    override fun areItemsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
        return  oldItem.id == newItem.id
                && oldItem.name == newItem.name
                && oldItem.description == newItem.description
                && oldItem.isPrivate == newItem.isPrivate
                && oldItem.isFork == newItem.isFork
                && oldItem.updatedDate == newItem.updatedDate
                && oldItem.sizeInKb == newItem.sizeInKb
                && oldItem.forksCount == newItem.forksCount
                && oldItem.language == newItem.language
                && oldItem.stargazersCount == newItem.stargazersCount
    }
}