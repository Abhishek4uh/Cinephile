package com.example.dump.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dump.callback.AnyitemClickCallback
import com.example.dump.callback.LongPressToLike
import com.example.dump.databinding.ChildRecyclerviewItemBinding
import com.example.dump.model.Movies
import com.example.dump.offline.MovieOfflineModel
import com.example.dump.util.Constants.BASE_IMAGE_URL


class MovieChildAdapter(private val list :List<Movies?>?,
                        private val anyitemClickCallback: AnyitemClickCallback,
                        private val longPressToLike: LongPressToLike): RecyclerView.Adapter<MovieChildAdapter.MovieChildViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieChildViewHolder {
        context=parent.context
        val itemBinding = ChildRecyclerviewItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return MovieChildViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovieChildViewHolder, position: Int) {
        val item = list?.get(position)

        //Setting the views
        holder.itemBinding.tvMovieName.text= item?.originalTitle.toString()

        if (item?.posterPath!=null)
        Glide.with(context!!).load(BASE_IMAGE_URL+ item.posterPath).into(holder.itemBinding.ivMoviePoster)

        else{
            Glide.with(context!!).load(BASE_IMAGE_URL+ item?.backdropPath).into(holder.itemBinding.ivMoviePoster)
        }

        holder.itemView.setOnClickListener {
            anyitemClickCallback.itemClick(item?.id!!)
        }

        holder.itemView.setOnLongClickListener {
            val movieOfflineModel= MovieOfflineModel(item?.id,item?.title,item?.overview,item?.posterPath )
            longPressToLike.longPressToLikeItem(movieOfflineModel)
            true
        }
    }

    override fun getItemCount(): Int {
       return list!!.size
    }

    inner class MovieChildViewHolder(val itemBinding: ChildRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

}