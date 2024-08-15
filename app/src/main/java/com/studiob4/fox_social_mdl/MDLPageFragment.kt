package com.studiob4.fox_social_mdl

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cuberto.liquid_swipe.LiquidPager

class MDLPageFragment : Fragment() {

    private lateinit var liquidPager: LiquidPager

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val position = arguments?.getInt("position") ?: -1
        val layoutId = when (position) {
            0 -> R.layout.second_page
            1 -> R.layout.first_page
            else -> R.layout.first_page
        }

        // Inflate the layout first
        val view = inflater.inflate(layoutId, container, false)

        // Find the ImageView and load the GIF with Glide
        val imageView = view.findViewById<ImageView>(R.id.imageViewGif)
        val gifResource = when (position) {
            0 -> R.drawable.worldnews // Replace with your first GIF
            1 -> R.drawable.battery_light_f_smdl // Replace with your second GIF
            else -> R.drawable.battery_light_f_smdl // Fallback GIF
        }

        Glide.with(this)
            .asGif()
            .load(gifResource)
            .into(imageView)

        liquidPager = activity?.findViewById(R.id.pager3)
            ?: throw IllegalStateException("LiquidPager not found in activity")

        liquidPager.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    liquidPager.parent.requestDisallowInterceptTouchEvent(true)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    liquidPager.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt("position") ?: 0

        when (position) {
            0 -> return
            1 -> return
            2 -> view.setBackgroundColor(
                Color.rgb(
                    255, // Red
                    135,   // Green
                    0    // Blue
                ) // Dark orange
            )
            3 -> view.setBackgroundColor(
                Color.rgb(
                    255, // Red
                    165, // Green
                    0    // Blue
                ) // Orange
            )
            4 -> view.setBackgroundColor(
                Color.rgb(
                    0,   // Red
                    128, // Green
                    0    // Blue
                ) // Green
            )
            5 -> view.setBackgroundColor(
                Color.rgb(
                    0,   // Red
                    0,   // Green
                    0    // Blue
                ) // Black
            )
            else -> view.setBackgroundColor(
                Color.rgb(
                    (Math.random() * 255).toInt(),
                    (Math.random() * 255).toInt(),
                    (Math.random() * 255).toInt()
                )
            )
        }

    }
}
