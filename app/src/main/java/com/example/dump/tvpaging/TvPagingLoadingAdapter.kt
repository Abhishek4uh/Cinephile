package com.example.dump.tvpaging

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dump.databinding.LoaderItemBinding


class TvPagingLoadingAdapter: LoadStateAdapter<TvPagingLoadingAdapter.LoadViewHolder>() {

    private var context: Context? = null

    override fun onBindViewHolder(holder: TvPagingLoadingAdapter.LoadViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadViewHolder(private val itemBinding: LoaderItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        fun bind(loadState: LoadState){
            itemBinding.progressBar.isVisible=loadState is LoadState.Loading
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): TvPagingLoadingAdapter.LoadViewHolder {
        context=parent.context
        val itemBinding = LoaderItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return LoadViewHolder(itemBinding)
    }

}