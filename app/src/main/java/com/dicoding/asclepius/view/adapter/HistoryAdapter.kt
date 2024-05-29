package com.dicoding.asclepius.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dicoding.asclepius.data.local.ResultEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.util.parseTimestamp
import java.text.NumberFormat

class HistoryAdapter(
    private val clickListener: (ResultEntity) -> Unit
) : ListAdapter<ResultEntity, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {


    class HistoryViewHolder(private val view: ItemHistoryBinding) : ViewHolder(view.root) {
        fun onBind(entity: ResultEntity, clickListener: (ResultEntity) -> Unit) {
            val uri = Uri.parse(entity.imageUri)
            view.ivImage.setImageURI(uri)
            view.tvLabel.text = entity.label
            view.tvScore.text = NumberFormat.getPercentInstance().format(entity.score)
            view.tvTimestamp.text = parseTimestamp(entity.savedAt)
            view.root.setOnClickListener {
                clickListener(entity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemBinding =
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(itemBinding)
    }


    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it, clickListener) }
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<ResultEntity>() {
    override fun areItemsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
        return oldItem == newItem
    }


}