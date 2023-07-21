package com.example.dump.ui


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dump.R
import com.example.dump.databinding.FragmentMoviedetailsBinding
import com.example.dump.model.moviedetailsModel.Genre
import com.example.dump.model.moviedetailsModel.MoviesDetailsData
import com.example.dump.network.DetailsEndPoint
import com.example.dump.network.RetrofitHelper
import com.example.dump.offline.MovieOfflineModel
import com.example.dump.offline.OfflineViewModel
import com.example.dump.util.Constants
import com.example.dump.viewmodel.DetailsMovieViewModel
import com.example.dump.viewmodel.DetailsMovieViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.properties.Delegates


class MoviedetailFragment(private val contentId: Long) : BottomSheetDialogFragment() {

    private var _binding : FragmentMoviedetailsBinding?=null
    private val mBinding get() =_binding!!

    private lateinit var viewModel: DetailsMovieViewModel
    private val itemViewModel:OfflineViewModel by viewModels()
    private lateinit var data: MoviesDetailsData


    //To Store in DB.. these params
    private lateinit var movieTitle:String
    private var moviePlot:String?=null
    private lateinit var posterUrl:String
    private var movieId by Delegates.notNull<Long>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment

        _binding=FragmentMoviedetailsBinding.inflate(inflater,container,false)
        val services= RetrofitHelper.getInstance(context).create(DetailsEndPoint::class.java)
        viewModel = ViewModelProvider(this, DetailsMovieViewModelFactory(services))[DetailsMovieViewModel::class.java]
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Starting..
        viewModel.fetchData(contentId)
        initClickListener()
        initObserber()
    }

    private fun alreadyLiked(){
        itemViewModel.contentExistsLiveData.observe(viewLifecycleOwner, Observer {res->
            val checkBox = mBinding.ivLiking
            checkBox.isChecked = res == true
            val drawableId = if (checkBox.isChecked) {
                R.drawable.heart_red
            } else {
                R.drawable.heart_grey
            }
            checkBox.buttonDrawable = ContextCompat.getDrawable(requireContext(), drawableId)
        })
    }

    private fun initObserber() {
        viewModel.progress.observe(viewLifecycleOwner, Observer {
            if(it){
                mBinding.progressBar.visibility=View.VISIBLE
            }
            else{
                mBinding.progressBar.visibility=View.GONE
            }
        })

        viewModel.movieDetailsLiveData.observe(viewLifecycleOwner, Observer { res->
            if(res!=null){
                data=res
                setViews(data)
                mBinding.clMain.visibility=View.VISIBLE
                mBinding.ivLiking.visibility=View.VISIBLE
                itemViewModel.searching(contentId)
                alreadyLiked()
            }
        })
    }

    private fun setViews(data: MoviesDetailsData) {
        mBinding.tvTitle.text=data.originalTitle.toString()

        //DB
        movieTitle=data.originalTitle.toString()
        movieId=contentId
        moviePlot=if(data.overview=="") "Not Available" else data.overview

        //Overview
        if (data.overview==""){
            mBinding.tvPlot.setText(R.string.no_overview)
        }
        else{
            mBinding.tvPlot.text=data.overview.toString()
        }

        //Poster
        if(data.posterPath!=null) {
            Glide.with(requireContext()).load(Constants.BASE_IMAGE_URL + data.posterPath).into(mBinding.ivMoviePoster)
            posterUrl=data.posterPath!!
        }
        else if (data.backdropPath!=null){
            Glide.with(requireContext()).load(Constants.BASE_IMAGE_URL + data.backdropPath).into(mBinding.ivMoviePoster)
            posterUrl=data.backdropPath!!
        }
        else{
            Glide.with(requireContext()).load(R.drawable.no_preview_image).into(mBinding.ivMoviePoster)
        }

        //Ratings
        if(data.voteAverage!=null){
            mBinding.tvRating.text=String.format("%.1f", data.voteAverage)
        }
        else{
            mBinding.tvRating.setText(R.string.not_available)
        }

        //Vote Count
        if (data.voteCount!=null && data.voteCount != 0L){
            mBinding.tvVoteCount.text=data.voteCount.toString()
        }
        else{
            mBinding.tvVoteCount.setText(R.string.not_available)
        }

        //Release Data
        if (data.releaseDate!=null){
            val date= dateFormatting(data.releaseDate!!)
            mBinding.tvRelease.text=date
        }
        else{
            mBinding.tvRelease.setText(R.string.not_available)
        }

        //Genres
        if (data.genres!=null){
            val genres= movieGenres(data.genres)
            val result = genres.replace("[\\[\\]]".toRegex(), "")
            mBinding.tvGenres.text=result
        }
        else{
            mBinding.tvGenres.setText(R.string.not_available)
        }

        //Budget
        if (data.budget!=null && data.budget !=0L){
            mBinding.tvBudget.text="$ "+data.budget
        }
        else{
            mBinding.tvBudget.setText(R.string.not_available)
        }

        //Revenue
        if (data.budget!=null && data.budget !=0L ){
            mBinding.tvRevenue.text="$ "+data.revenue
        }
        else{
            mBinding.tvRevenue.setText(R.string.not_available)
        }

        //Release
        if (data.status=="Released"){
            mBinding.tvReleaseBoolean.text="Released"
        }
        else{
            mBinding.tvReleaseBoolean.text="Yet to Release"
            mBinding.tvReleaseBoolean.setTextColor(android.graphics.Color.parseColor("#FFD32F2F"))
        }

        //WatchTime
        if (data.runtime!=null){
            val minutes = data.runtime!!.toInt()
            val hours = minutes / 60
            val remainingMinutes = minutes % 60
            val duration= "$hours h$remainingMinutes m"
            mBinding.tvWatchTime.text=duration
        }
        else{
            mBinding.tvWatchTime.setText(R.string.not_available)
        }

    }


    private fun initClickListener() {
        val checkBox = mBinding.ivLiking

        checkBox.setOnCheckedChangeListener { _,isChecked ->
            if (isChecked) {
                if (itemViewModel.contentExistsLiveData.value != true) {
                    val offlineModel = MovieOfflineModel(movieId, movieTitle, moviePlot, posterUrl)
                    itemViewModel.addItem(offlineModel)
                    Toast.makeText(context, "Added in Watchlist", Toast.LENGTH_SHORT).show()
                }
            } else {
                itemViewModel.deleteItem(contentId)

            }
        }
    }



    private fun movieGenres(genres: List<Genre?>?): String {
        val names = mutableListOf<String>()
        if (genres != null) {
            if (genres.size >= 3) {
                for (genre in genres.take(3)) {
                    val name = genre?.name
                    names.add(name!!)
                }
            }
            else{
                for (genre in genres) {
                    val name = genre?.name
                    names.add(name!!)
                }
            }
        }
        return names.toString()
    }

    private fun dateFormatting(date: String): String {
        val parts = date.split("-")
        return "${parts[2]}-${parts[1]}-${parts[0].substring(0)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}



