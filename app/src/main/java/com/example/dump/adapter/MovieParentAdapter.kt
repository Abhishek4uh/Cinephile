package com.example.dump.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dump.callback.AnyitemClickCallback
import com.example.dump.callback.LongPressToLike
import com.example.dump.callback.ViewMoreClick
import com.example.dump.databinding.ParentRecyclerviewItemBinding
import com.example.dump.model.CustomMovieData


class MovieParentAdapter(private val list:ArrayList<CustomMovieData>,
                         private val anyitemClickCallback: AnyitemClickCallback,
                         private val viewMoreClick: ViewMoreClick,
                         private val longPressToLike:LongPressToLike):RecyclerView.Adapter<MovieParentAdapter.MovieParentViewHolder>(){


    private var context: Context? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieParentViewHolder {

        context=parent.context
        val itemBinding = ParentRecyclerviewItemBinding.inflate(LayoutInflater.from(context), parent,false)
        return MovieParentViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: MovieParentViewHolder, position: Int) {
        val currentItem = list[holder.adapterPosition]
        holder.itemBinding.tvMovieCategory.text=currentItem.category.toString()

        //For Horizontal recyclerview initialization....
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.itemBinding.childRV.layoutManager=layoutManager

        val childAdapter=MovieChildAdapter(currentItem.movielist?.results,anyitemClickCallback,longPressToLike)
        holder.itemBinding.childRV.adapter=childAdapter

        holder.itemBinding.tvViewMore.setOnClickListener {
            viewMoreClick.viewMoreClick(currentItem.categoryID!!)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MovieParentViewHolder(val itemBinding: ParentRecyclerviewItemBinding):RecyclerView.ViewHolder(itemBinding.root)

}