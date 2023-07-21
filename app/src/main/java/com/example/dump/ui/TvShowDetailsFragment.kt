package com.example.dump.ui


import android.os.Bundle
import android.util.Log
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
import com.example.dump.databinding.FragmentTvShowDetailsBinding
import com.example.dump.model.tvdetailsModel.Genre
import com.example.dump.model.tvdetailsModel.TvDetailsData
import com.example.dump.network.DetailsEndPoint
import com.example.dump.network.RetrofitHelper
import com.example.dump.offline.MovieOfflineModel
import com.example.dump.offline.OfflineViewModel
import com.example.dump.util.Constants
import com.example.dump.viewmodel.DetailsTvViewModel
import com.example.dump.viewmodel.DetailsTvViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.properties.Delegates


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TvShowDetailsFragment(private val contentId: Long) : BottomSheetDialogFragment() {

    private var _binding : FragmentTvShowDetailsBinding?=null
    private val mBinding get() =_binding!!
    private lateinit var viewModel: DetailsTvViewModel
    private val itemViewModel: OfflineViewModel by viewModels()
    private lateinit var data: TvDetailsData

    //DB
    private lateinit var tvShowTitle:String
    private var tvShowPlot:String?=null
    private lateinit var tvShowPosterUrl:String
    private var tvShowId by Delegates.notNull<Long>()


    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding= FragmentTvShowDetailsBinding.inflate(inflater,container,false)
        val services= RetrofitHelper.getInstance(context).create(DetailsEndPoint::class.java)
        viewModel = ViewModelProvider(this, DetailsTvViewModelFactory(services))[DetailsTvViewModel::class.java]
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Starting...
        viewModel.fetchData(contentId)
        initClickListener()
        initObserber()

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

        viewModel.tvDetailsLiveData.observe(viewLifecycleOwner, Observer { res->
            if(res !=null){
                data=res
                setViews(data)
                mBinding.clMain.visibility=View.VISIBLE
                mBinding.ivLiking.visibility=View.VISIBLE
                itemViewModel.searching(contentId)
                alreadyLiked()
            }
        })
    }

    private fun setViews(data: TvDetailsData) {
        mBinding.tvTitle.text=data.originalName.toString()

        //DB
        tvShowTitle=data.originalName.toString()
        tvShowId=contentId
        tvShowPlot=data.overview.toString()

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
            tvShowPosterUrl=data.posterPath!!
        }
        else if (data.backdropPath!=null){
            Glide.with(requireContext()).load(Constants.BASE_IMAGE_URL + data.backdropPath).into(mBinding.ivMoviePoster)
            tvShowPosterUrl=data.backdropPath!!
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
        if (data.voteCount!=null){
            mBinding.tvVoteCount.text=data.voteCount.toString()
        }
        else{
            mBinding.tvVoteCount.setText(R.string.not_available)
        }

        //Release Data
        if (data.firstAirDate==""){
            mBinding.tvRelease.setText(R.string.not_available)
        }
        else{
            val date=dateFormatting(data.firstAirDate!!)
            mBinding.tvRelease.text=date
        }

        //Genres
        if (data.genres!=null){
            val genres= movieGenres(data.genres)
            val result = genres.replace("[\\[\\]]".toRegex(), "")
            Log.d("GENRES",result)
            mBinding.tvGenres.text=result
        }
        else{
            mBinding.tvGenres.setText(R.string.not_available)
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

    private fun initClickListener() {
        val checkBox = mBinding.ivLiking

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (itemViewModel.contentExistsLiveData.value != true) {
                    val offlineModel = MovieOfflineModel(tvShowId, tvShowTitle, tvShowPlot?:"Not available", tvShowPosterUrl)
                    itemViewModel.addItem(offlineModel)
                    Toast.makeText(context, "Added in Watchlist", Toast.LENGTH_SHORT).show()
                }
            } else {
                itemViewModel.deleteItem(contentId)
            }
        }
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


    private fun dateFormatting(date: String): String {
        val parts = date.split("-")
        return "${parts[2]}-${parts[1]}-${parts[0].substring(0)}"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}