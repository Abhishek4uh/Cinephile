package com.example.dump.moviepaging


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dump.databinding.LoaderItemBinding


class MoviePagingLoadinAdapter:LoadStateAdapter<MoviePagingLoadinAdapter.LoadViewHolder>(){

    private var context: Context? = null

    override fun onBindViewHolder(holder: LoadViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadViewHolder {
        context=parent.context
        val itemBinding = LoaderItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return LoadViewHolder(itemBinding)
    }

    inner class LoadViewHolder(private val itemBinding:LoaderItemBinding):RecyclerView.ViewHolder(itemBinding.root){
        fun bind(loadState: LoadState){
            itemBinding.progressBar.isVisible=loadState is LoadState.Loading
        }
    }
}