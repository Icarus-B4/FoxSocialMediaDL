package com.studiob4.fox_social_mdl

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.studiob4.fox_social_mdl.R

class ImagePagerAdapter : RecyclerView.Adapter<ImagePagerAdapter.ViewHolder>() {

    private val images = arrayOf(R.drawable.aliexpress_slider, R.drawable.artwork, R.drawable.nike)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return ViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}