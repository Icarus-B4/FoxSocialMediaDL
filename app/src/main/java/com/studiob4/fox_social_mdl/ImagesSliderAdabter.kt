package com.studiob4.fox_social_mdl

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

class ImageSliderAdapter(
    private val context: Context,
    private val imageUrls: List<String>,
    private val links: List<String>
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image_slider, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        val link = links[position]

        Glide.with(context)
            .load(imageUrl)
            .into(holder.imageView)

        holder.buttonOpenLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = imageUrls.size

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ShapeableImageView = view.findViewById(R.id.imageViewSlider)
        val buttonOpenLink: MaterialButton = view.findViewById(R.id.buttonOpenLinkSlider)
    }
}
