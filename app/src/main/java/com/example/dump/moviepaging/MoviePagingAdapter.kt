package com.example.dump.moviepaging


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dump.R
import com.example.dump.callback.AnyitemClickCallback
import com.example.dump.databinding.MovieItemBinding
import com.example.dump.model.Movies
import com.example.dump.util.Constants


class MoviePagingAdapter(private val anyitemClickCallback: AnyitemClickCallback): PagingDataAdapter<Movies,MoviePagingAdapter.MoreDataViewHolder>(MovieComparator) {

    private var context: Context? = null


    inner class MoreDataViewHolder(val itemBinding: MovieItemBinding) : RecyclerView.ViewHolder(itemBinding.root)


    object MovieComparator: DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: MoreDataViewHolder, position: Int) {
        val item=getItem(position)

        if (item!=null){

            Log.d("DEBUG",item.toString())

            holder.itemBinding.tvMovieName.text=item.title.toString()

            if (item.posterPath !=null)
                Glide.with(context!!).load(Constants.BASE_IMAGE_URL + item.posterPath).into(holder.itemBinding.ivMoviePoster)

            else if(item.backdropPath !=null){
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreDataViewHolder {
        context=parent.context
        val itemBinding = MovieItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MoreDataViewHolder(itemBinding)
    }

}