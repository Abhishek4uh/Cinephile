package com.example.dump.offline

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.dump.R
import com.example.dump.databinding.WatchlistItemBinding
import com.example.dump.util.Constants


class OfflineAdapterClass(private val onItemDeleteListener: LongPressToDelete) : RecyclerView.Adapter<OfflineAdapterClass.MyViewHolder>()  {

    private var cxt: Context?=null
    private val data = ArrayList<MovieOfflineModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        cxt=parent.context
        return MyViewHolder(WatchlistItemBinding.inflate(LayoutInflater.from(cxt),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentData= data[position]

        if(currentData!=null) {
            holder.mBinding.tvTitle.text = currentData.title
            holder.mBinding.tvPlot.text = currentData.plot

            Glide.with(cxt!!)
                .load(Constants.BASE_IMAGE_URL + currentData.poster)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.no_preview_image)
                .into(holder.mBinding.ivMoviePoster)

            holder.itemView.setOnLongClickListener {
                Log.d("ABHISHEK_1","AdapterPos "+holder.absoluteAdapterPosition.toString() +"----"+ "ContentId "+currentData.contentId)
                onItemDeleteListener.onDelete(currentData.contentId!!)
                data.removeAt(holder.absoluteAdapterPosition)
                notifyItemRemoved(holder.absoluteAdapterPosition)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(val mBinding: WatchlistItemBinding) :RecyclerView.ViewHolder(mBinding.root){}


    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<MovieOfflineModel>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()

    }

}