package com.example.dump.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dump.adapter.MovieParentAdapter
import com.example.dump.callback.AnyitemClickCallback
import com.example.dump.callback.LongPressToLike
import com.example.dump.callback.ViewMoreClick
import com.example.dump.databinding.FragmentMovieBinding
import com.example.dump.model.CustomMovieData
import com.example.dump.network.MovieEndpoints
import com.example.dump.network.RetrofitHelper
import com.example.dump.offline.MovieOfflineModel
import com.example.dump.offline.OfflineViewModel
import com.example.dump.repository.MovieRepository
import com.example.dump.viewmodel.MovieViewModel
import com.example.dump.viewmodel.MovieViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.VibrationEffect
import android.os.Vibrator


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MovieFragment : Fragment(), AnyitemClickCallback,ViewMoreClick, LongPressToLike {

    private var _binding : FragmentMovieBinding ?=null
    private val mBinding get() =_binding!!
    private lateinit var movieParentAdapter : MovieParentAdapter
    private lateinit var viewModel: MovieViewModel
    private val itemViewModel: OfflineViewModel by activityViewModels()
    private val dataFeedToADApter = ArrayList<CustomMovieData>()


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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val quoteServices= RetrofitHelper.getInstance(context).create(MovieEndpoints::class.java)
        val repository= MovieRepository(quoteServices)
        viewModel = ViewModelProvider(this,MovieViewModelFactory(repository))[MovieViewModel::class.java]

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        initrecyclerviews()


        viewModel.movieLiveData.observe(viewLifecycleOwner, Observer {
            dataFeedToADApter.clear()
            dataFeedToADApter.add(CustomMovieData(1,"Now Playing",it[0]))
            dataFeedToADApter.add(CustomMovieData(2,"Upcoming",it[1]))
            dataFeedToADApter.add(CustomMovieData(3,"Top Rated",it[2]))
            dataFeedToADApter.add(CustomMovieData(4,"Popular",it[3]))

            if(::movieParentAdapter.isInitialized){
                movieParentAdapter.notifyItemRangeChanged(0, dataFeedToADApter.size)
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
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
        mBinding.parentRecyclerView.layoutManager = layoutManager
        movieParentAdapter = MovieParentAdapter(dataFeedToADApter,this,this,this)
        mBinding.parentRecyclerView.adapter=movieParentAdapter

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieFragment().apply {
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
        val moviedetailFragment= MoviedetailFragment(id)
        moviedetailFragment.show(childFragmentManager,MoviedetailFragment::class.java.name)
    }

    override fun viewMoreClick(viewMorePos:Int) {
        val pagingLoadMoreDataBottomsheet =  MoviePagingDataBottomsheet(viewMorePos,"movie")
        pagingLoadMoreDataBottomsheet.show(childFragmentManager,MoviePagingDataBottomsheet::class.java.name)
    }

    override fun longPressToLikeItem(content: MovieOfflineModel) {
        added(content)
    }

    private fun added(content: MovieOfflineModel) {
        lifecycleScope.launch(Dispatchers.Main){
           val alreadyPresentCheck=itemViewModel.longPressCheck(content.contentId!!)

            if (alreadyPresentCheck){
                Toast.makeText(context,"Already Present",Toast.LENGTH_SHORT).show()
            }
            else{
                itemViewModel.addItem(content)
                vibrateDevice(requireContext(),500)
                Toast.makeText(context,"Added in WatchList",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun vibrateDevice(context: Context, milliseconds: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
            }
            else {
                vibrator.vibrate(milliseconds)
            }
        }
    }
}