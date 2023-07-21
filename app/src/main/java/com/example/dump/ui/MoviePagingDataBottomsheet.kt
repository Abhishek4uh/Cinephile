package com.example.dump.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dump.R
import com.example.dump.callback.AnyitemClickCallback
import com.example.dump.databinding.FragmentPagingLoadMoreDataBottomsheetBinding
import com.example.dump.moviepaging.*
import com.example.dump.network.MovieEndpoints
import com.example.dump.network.RetrofitHelper
import com.example.dump.network.TvShowEndPoints
import com.example.dump.tvpaging.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MoviePagingDataBottomsheet(viewMorePos:Int, tag:String): BottomSheetDialogFragment(), AnyitemClickCallback {

    private var _binding : FragmentPagingLoadMoreDataBottomsheetBinding?=null
    private val mBinding get() =_binding!!

    lateinit var viewModelMovie: MoviePagingViewModel
    lateinit var viewModelTv: TvPagingViewModel

    private val moviePagingAdapter = MoviePagingAdapter(this)
    private val tvPagingAdapter =TvPagingAdapter(this)

    private val adapterPos=viewMorePos
    private val tags=tag

    //default params
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        _binding=FragmentPagingLoadMoreDataBottomsheetBinding.inflate(inflater,container,false)

        val dialog = dialog as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        mBinding.rv.layoutManager = GridLayoutManager(context,2)


        if(tags=="movie"){
            val movieServices= RetrofitHelper.getInstance(context).create(MovieEndpoints::class.java)
            val mainRepository = MoviePagingRepo(movieServices)
            mBinding.rv.adapter =moviePagingAdapter.withLoadStateFooter(footer = MoviePagingLoadinAdapter())
            viewModelMovie = ViewModelProvider(this, MoviePagingViewModelFactory(mainRepository))[MoviePagingViewModel::class.java]
        }
        else{
            val tvServices= RetrofitHelper.getInstance(context).create(TvShowEndPoints::class.java)
            val mainRepository = TvPagingRepo(tvServices)
            mBinding.rv.adapter =tvPagingAdapter.withLoadStateFooter(footer = TvPagingLoadingAdapter())
            viewModelTv = ViewModelProvider(this, TvPagingViewModelFactory(mainRepository))[TvPagingViewModel::class.java]
        }


        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()

        if (tags=="movie"){
            movieInitObserver()
        }
        else {
            tvInitObserver()
        }

    }

    private fun tvInitObserver() {

        viewModelTv.errorMessage.observe(this) {
            Toast.makeText( requireContext(),it, Toast.LENGTH_SHORT).show()
        }

        tvPagingAdapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading)
                mBinding.progressBar.isVisible = true
            else {
                mBinding.progressBar.isVisible = false
                //If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_LONG).show()
                }

            }
        }


        lifecycleScope.launch{
            viewModelTv.getTvList(adapterPos).observe(viewLifecycleOwner, Observer {
                it?.let {
                    tvPagingAdapter.submitData(lifecycle, it)
                    tvPagingAdapter.notifyDataSetChanged()
                }
            })
        }
    }


    private fun movieInitObserver() {

        viewModelMovie.errorMessage.observe(this) {
            Toast.makeText( requireContext(),it, Toast.LENGTH_SHORT).show()
        }

        moviePagingAdapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading)
                mBinding.progressBar.isVisible = true
            else {
                mBinding.progressBar.isVisible = false
                //If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }


        lifecycleScope.launch{
            viewModelMovie.getMovieList(adapterPos).observe(viewLifecycleOwner, Observer {
                it?.let {
                    moviePagingAdapter.submitData(lifecycle, it)
                    moviePagingAdapter.notifyDataSetChanged()
                }
            })
        }
    }


    private fun initClickListener() {
        mBinding.ivCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun itemClick(id: Long) {
        if(tags=="movie") {

            Log.d("DEBUG",id.toString())

            val moviedetailFragment = MoviedetailFragment(id)
            moviedetailFragment.show(requireActivity().supportFragmentManager, MoviedetailFragment::class.java.name)
        }
        else{
            Log.d("DEBUG",id.toString())

           val tvShowDetailsFragment = TvShowDetailsFragment(id)
            tvShowDetailsFragment.show(requireActivity().supportFragmentManager,TvShowDetailsFragment::class.java.name)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}