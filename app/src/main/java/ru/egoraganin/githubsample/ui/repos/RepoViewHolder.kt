package ru.egoraganin.githubsample.ui.repos

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.egoraganin.githubsample.R
import ru.egoraganin.githubsample.entity.repo.RepoModel
import ru.egoraganin.githubsample.extention.getTimeAgo
import ru.egoraganin.githubsample.extention.isVisibleGone

class RepoViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val forkedLabel: TextView = this.itemView.findViewById(R.id.label_forked)
    private val privateLabel: TextView = this.itemView.findViewById(R.id.label_private)

    private val nameLabel: TextView = this.itemView.findViewById(R.id.label_name)
    private val descriptionLabel: TextView = this.itemView.findViewById(R.id.label_description)

    private val startCountLabel: TextView = this.itemView.findViewById(R.id.label_star_count)
    private val forkCountLabel: TextView = this.itemView.findViewById(R.id.label_fork_count)
    private val updatedDateLabel: TextView = this.itemView.findViewById(R.id.label_updated_date)
    private val reposSizeLabel: TextView = this.itemView.findViewById(R.id.label_repo_size)
    private val languageLabel: TextView = this.itemView.findViewById(R.id.label_language)

    fun bind(model: RepoModel) {
        forkedLabel.isVisibleGone(model.isFork)
        privateLabel.isVisibleGone(model.isPrivate)

        nameLabel.text = model.name

        descriptionLabel.isVisibleGone(!model.description.isNullOrEmpty())
        descriptionLabel.text = model.description

        startCountLabel.text = model.stargazersCount.toString()
        forkCountLabel.text = model.forksCount.toString()

        updatedDateLabel.text = model.updatedDate.getTimeAgo()

        val repoSize = Formatter.formatFileSize(itemView.context, model.sizeInKb * 1024L)
        reposSizeLabel.text = repoSize

        languageLabel.text = model.language
    }

    companion object {
        fun newInstance(parent: ViewGroup): RepoViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_repo_item, parent, false)
            return RepoViewHolder(itemView)
        }
    }
}