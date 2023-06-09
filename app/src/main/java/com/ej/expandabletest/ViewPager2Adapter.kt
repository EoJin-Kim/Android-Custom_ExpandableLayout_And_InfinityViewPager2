package com.ej.expandabletest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ej.expandabletest.databinding.ItemViewpagerBinding

class ViewPagerAdapter(private val listData: ArrayList<DataPage>) : RecyclerView.Adapter<ViewHolderPage>() {
    lateinit var binding: ItemViewpagerBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPage {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_viewpager, parent, false)
        return ViewHolderPage(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderPage, position: Int) {
        val viewHolder: ViewHolderPage = holder
        viewHolder.onBind(listData[position % listData.size])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
//    override fun getItemCount(): Int = listData.size



}

class ViewHolderPage(val binding: ItemViewpagerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: DataPage) {
        binding.tvTitle.text = data.title
        binding.rlLayout.setBackgroundResource(data.color)
    }
}