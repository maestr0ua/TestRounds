package com.example.testrounds.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteimage.RemoteImage
import com.example.testrounds.R
import com.example.testrounds.databinding.ViewItemImageBinding
import com.example.testrounds.domain.ImageEntity

class ImageAdapter(private val items: List<ImageEntity>) :
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewItemImageBinding.inflate(inflater, parent, false)
        return ImageHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val item = items[position]

        holder.binding.tvTitle.text = item.id
        RemoteImage.Builder(holder.binding.root.context)
            .addLoading(R.drawable.placeholder_loading)
            .addError(R.drawable.placeholder_error)
            .setSize(800, 400)
            .setUrl(item.imageUrl)
            .build(holder.binding.ivImage)
    }

    class ImageHolder(val binding: ViewItemImageBinding) : RecyclerView.ViewHolder(binding.root)
}