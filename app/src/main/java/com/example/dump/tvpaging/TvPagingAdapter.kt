package com.example.dump.tvpaging


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dump.R
import com.example.dump.callback.AnyitemClickCallback
import com.example.dump.databinding.MovieItemBinding
import com.example.dump.model.TvShow
import com.example.dump.util.Constants


class TvPagingAdapter(private val anyitemClickCallback: AnyitemClickCallback): PagingDataAdapter<TvShow, TvPagingAdapter.TvPageAdapterViewHolder>(COMPARATOR) {

    private var context: Context? = null

    inner class TvPageAdapterViewHolder(val itemBinding: MovieItemBinding) : RecyclerView.ViewHolder(itemBinding.root)


    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<TvShow>() {
            override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: TvPageAdapterViewHolder, position: Int) {
        val item=getItem(position)

        if (item!=null){
            holder.itemBinding.tvMovieName.text=item.name.toString()
            if (item.posterPath !=null)
                Glide.with(context!!).load(Constants.BASE_IMAGE_URL + item.posterPath).into(holder.itemBinding.ivMoviePoster)

            else if (item.backdropPath !=null){
                Glide.with(context!!).load(Constants.BASE_IMAGE_URL + item.backdropPath).into(holder.itemBinding.ivMoviePoster)
            }
            else{
                Glide.with(context!!).load(R.drawable.no_preview_image).into(holder.itemBinding.ivMoviePoster)
            }
        }

        holder.itemView.setOnClickListener {
            anyitemClickCallback.itemClick(item?.id!!)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvPageAdapterViewHolder {
        context=parent.context
        val itemBinding = MovieItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TvPageAdapterViewHolder(itemBinding)
    }

}