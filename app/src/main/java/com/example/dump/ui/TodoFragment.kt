package com.example.dump.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.dump.databinding.FragmentTodoBinding
import com.example.dump.offline.*


class TodoFragment : Fragment(),LongPressToDelete {

    private var _binding: FragmentTodoBinding? = null
    private val mBinding get() = _binding!!
    private val itemViewModel:OfflineViewModel by activityViewModels()
    private val adapterClass= OfflineAdapterClass(this)


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentTodoBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Start..........

        mBinding.watchListRv.adapter=adapterClass
        itemViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            Log.d("ABHISHEK_3",it.toString())

            if (it.isNotEmpty()) {
                adapterClass.updateList(it)
                mBinding.noWatchList.visibility = View.GONE
                mBinding.watchListRv.visibility=View.VISIBLE
                mBinding.tvLongPress.visibility=View.VISIBLE
            }
            else{
                mBinding.noWatchList.visibility=View.VISIBLE
                mBinding.watchListRv.visibility=View.GONE
                mBinding.tvLongPress.visibility=View.GONE
            }
        })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDelete(contentId: Long) {
        Log.d("ABHISHEK_2", "$contentId $itemViewModel")
        itemViewModel.deleteItem(contentId)
    }

}