package com.test.storyappsubmission1.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.storyappsubmission1.data.ListStoryItem
import com.test.storyappsubmission1.databinding.ItemStoryBinding
import com.test.storyappsubmission1.ui.detailstory.DetailStoryActivity

class StoryAdapter(private val listReview: List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.MyViewHolderStory>() {

//    private lateinit var onItemClickCallback: OnItemClickCallback

//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//    interface OnItemClickCallback {
//        fun onItemClicked(story:ListStoryItem)
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderStory {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolderStory(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolderStory, position: Int) {

        val data = listReview[position]
        holder.bind(data)
    }
    override fun getItemCount() = listReview.size

    class MyViewHolderStory(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            Glide.with(binding.root.context)
                .load(data.photoUrl)
                .into(binding.imgItemPhoto)

            binding.tvName.text = data.name
            binding.tvCreated.text = data.createdAt
            binding.tvDescription.text = data.description
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.NAME, data.name)
                intent.putExtra(DetailStoryActivity.CREATE_AT, data.createdAt)
                intent.putExtra(DetailStoryActivity.DESCRIPTION, data.description)
                intent.putExtra(DetailStoryActivity.PHOTO_URL, data.photoUrl)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair(binding.imgItemPhoto, "photo"),
                        androidx.core.util.Pair(binding.tvName, "name"),
                        androidx.core.util.Pair(binding.tvCreated, "createdate"),
                        androidx.core.util.Pair(binding.tvDescription, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}