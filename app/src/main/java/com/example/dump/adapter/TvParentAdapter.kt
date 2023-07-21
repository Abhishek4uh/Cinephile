package com.example.dump.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dump.callback.AnyitemClickCallback
import com.example.dump.callback.ViewMoreClick
import com.example.dump.databinding.ParentRecyclerviewItemBinding
import com.example.dump.model.CustomTVData


class TvParentAdapter(private val list:ArrayList<CustomTVData>,
                      private val anyitemClickCallback: AnyitemClickCallback,
                      private val viewMoreClick: ViewMoreClick):RecyclerView.Adapter<TvParentAdapter.TvParentViewHolder>() {

    private var context: Context? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvParentViewHolder {
        context=parent.context
        val itemBinding = ParentRecyclerviewItemBinding.inflate(LayoutInflater.from(context), parent,false)
        return TvParentViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TvParentViewHolder, position: Int) {

        val currentItem = list[holder.adapterPosition]
        holder.itemBinding.tvMovieCategory.text=currentItem.category.toString()

        //For Horizontal recyclerview initialization....
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.itemBinding.childRV.layoutManager=layoutManager

        val childAdapter=TvChildAdapter(currentItem.tvShowList?.results,anyitemClickCallback)

        holder.itemBinding.childRV.adapter=childAdapter

        holder.itemBinding.tvViewMore.setOnClickListener {
            viewMoreClick.viewMoreClick(currentItem.categoryID!!)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TvParentViewHolder(val itemBinding: ParentRecyclerviewItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
}