package com.example.dump.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dump.callback.AnyitemClickCallback
import com.example.dump.databinding.ChildRecyclerviewItemBinding
import com.example.dump.model.TvShow
import com.example.dump.util.Constants


class TvChildAdapter(private val list :List<TvShow?>?,
                     private val anyitemClickCallback: AnyitemClickCallback):RecyclerView.Adapter<TvChildAdapter.TvChildViewHolder>(){

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvChildViewHolder {
        context=parent.context
        val itemBinding = ChildRecyclerviewItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return TvChildViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TvChildViewHolder, position: Int) {

        val item = list?.get(position)
        holder.itemBinding.tvMovieName.text= item?.name.toString()


        if (item?.posterPath!=null) {
            Glide.with(context!!).load(Constants.BASE_IMAGE_URL + item.posterPath)
                .into(holder.itemBinding.ivMoviePoster)
        }
        else{
            Glide.with(context!!).load(Constants.BASE_IMAGE_URL + item?.backdropPath)
                .into(holder.itemBinding.ivMoviePoster)
        }

        holder.itemView.setOnClickListener {
            item?.id?.let { it1 -> anyitemClickCallback.itemClick(it1) }
        }

    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class TvChildViewHolder(val itemBinding: ChildRecyclerviewItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
}