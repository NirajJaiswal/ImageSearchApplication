package com.example.imagesearchapplication.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearchapplication.R
import com.example.imagesearchapplication.databinding.ImageItemLayoutBinding
import com.example.imagesearchapplication.listener.ImageListener
import com.example.imagesearchapplication.model.Hit

class MainAdapter(
    private val imagesList: ArrayList<Hit>,
    private val listener: ImageListener
) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {


    class MainViewHolder(private val view: ImageItemLayoutBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(hit: Hit, listener: ImageListener) {
            view.apply {
                Glide.with(imageView.context)
                    .load(hit.webformatURL)
                    .placeholder(R.drawable.loading)
                    .into(imageView)
            }
            view.imageView.setOnClickListener {
                listener.onImageClick(hit)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val imageItemLayoutBinding: ImageItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.image_item_layout,
            parent,
            false
        )
        return MainViewHolder(imageItemLayoutBinding)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(imagesList[position], listener)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    fun addUsers(imagesList: List<Hit>) {
        this.imagesList.apply {
            clear()
            addAll(imagesList)
        }

    }
}
