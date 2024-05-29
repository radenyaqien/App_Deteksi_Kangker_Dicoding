package com.dicoding.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.dicoding.asclepius.data.remote.Article
import com.dicoding.asclepius.databinding.ItemArticleBinding

class ArticleAdapter(
    private val clickListener: (Article) -> Unit
) : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback()) {


    class ArticleViewHolder(private val view: ItemArticleBinding) : ViewHolder(view.root) {
        fun onBind(entity: Article, clickListener: (Article) -> Unit) {
            view.imgNews.load(entity.urlToImage)
            view.tvTitle.text = entity.title
            view.tvDesc.text = entity.description
            view.tvLink.setOnClickListener {
                clickListener(entity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemBinding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(itemBinding)
    }


    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it, clickListener) }
    }
}

class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }


}