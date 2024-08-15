package com.studiob4.fox_social_mdl

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MdlAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val total: Int = 3
    private val data: ArrayList<MDLPageFragment> = ArrayList(total)

    init {
        for (i in 0 until total) {
            val fragment = MDLPageFragment()
            val bundle = Bundle()
            bundle.putInt("position", i)
            fragment.arguments = bundle
            data.add(fragment)
        }
    }

    override fun getItem(position: Int): Fragment {
        return data[position]
    }

    override fun getCount(): Int {
        return total
    }
}
