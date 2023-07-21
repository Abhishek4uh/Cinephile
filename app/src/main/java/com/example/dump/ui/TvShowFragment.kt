package com.example.dump.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dump.adapter.TvParentAdapter
import com.example.dump.callback.AnyitemClickCallback
import com.example.dump.callback.ViewMoreClick
import com.example.dump.databinding.FragmentTvShowBinding
import com.example.dump.model.CustomTVData
import com.example.dump.network.RetrofitHelper
import com.example.dump.network.TvShowEndPoints
import com.example.dump.repository.TvShowRepository
import com.example.dump.viewmodel.TvViewModel
import com.example.dump.viewmodel.TvViewModelFactory


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TvShowFragment : Fragment(), AnyitemClickCallback, ViewMoreClick {

    private var _binding : FragmentTvShowBinding?=null
    private val mBinding get() =_binding!!
    private lateinit var tvParentAdapter: TvParentAdapter
    private lateinit var viewModel: TvViewModel
    private val dataFeedToADApter = ArrayList<CustomTVData>()

    //default param
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        _binding=FragmentTvShowBinding.inflate(inflater,container,false)
        val services= RetrofitHelper.getInstance(context).create(TvShowEndPoints::class.java)
        val repository= TvShowRepository(services)
        viewModel = ViewModelProvider(this, TvViewModelFactory(repository))[TvViewModel::class.java]

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initrecyclerviews()
        initclicklistners()

        viewModel.tvShowLiveData.observe(viewLifecycleOwner, Observer {
            Log.d("APPDATA",it.toString())
            dataFeedToADApter.clear()
            dataFeedToADApter.add(CustomTVData(1, "Airing Today",it[0]))
            dataFeedToADApter.add(CustomTVData(2, "On The Air",it[1]))
            dataFeedToADApter.add(CustomTVData(3, "Top Rated",it[2]))
            dataFeedToADApter.add(CustomTVData(4, "Popular",it[3]))

            if(::tvParentAdapter.isInitialized){
                tvParentAdapter.notifyItemRangeChanged(0, dataFeedToADApter.size)
            }

        })

        viewModel.progress.observe(viewLifecycleOwner, Observer {
            if(it){
                mBinding.progressBar.visibility=View.VISIBLE
            }
            else{
                mBinding.progressBar.visibility=View.GONE
            }
        })


    }

    private fun initrecyclerviews() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.parentRecyclerView.layoutManager = layoutManager

        tvParentAdapter = TvParentAdapter(dataFeedToADApter,this,this)
        mBinding.parentRecyclerView.adapter=tvParentAdapter
    }


    private fun initclicklistners() {
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TvShowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun itemClick(id: Long) {
        val moviedetailsFragment= TvShowDetailsFragment(id)
        moviedetailsFragment.show(childFragmentManager,TvShowDetailsFragment::class.java.name)
    }

    override fun viewMoreClick(viewMorePos: Int) {
        val pagingLoadMoreDataBottomsheet =  MoviePagingDataBottomsheet(viewMorePos,"tv")
        pagingLoadMoreDataBottomsheet.show(childFragmentManager,MoviePagingDataBottomsheet::class.java.name)
    }

}