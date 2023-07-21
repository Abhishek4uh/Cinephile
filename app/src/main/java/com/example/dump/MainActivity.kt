package com.example.dump


import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.dump.databinding.ActivityMainBinding
import com.example.dump.ui.MovieFragment
import com.example.dump.ui.TodoFragment
import com.example.dump.ui.TvShowFragment


class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null
    private val movieFragment=MovieFragment()
    private val tvShowFragment=TvShowFragment()
    private val todoFragment=TodoFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding?.root)

        //Start
        mainfunction()

        mBinding!!.navView.setOnNavigationItemSelectedListener {
            when(it.itemId){

                R.id.navigation_movie ->{
                    if(isNetworkConnected(this)){
                        mBinding?.clNoInternet?.visibility = View.GONE
                        setCurrentFragment(movieFragment)
                    }
                    else{
                        Glide.with(this).load(R.raw.tvf_roadies).into(mBinding!!.serverDown)
                        mBinding?.clNoInternet?.visibility = View.VISIBLE
                        Toast.makeText(this, R.string.connect_to_internet, Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.navigation_tv->{
                    if(isNetworkConnected(this)){
                        mBinding?.clNoInternet?.visibility = View.GONE
                        setCurrentFragment(tvShowFragment)
                    }
                    else{
                        Glide.with(this).load(R.raw.tvf_roadies).into(mBinding!!.serverDown)
                        mBinding?.clNoInternet?.visibility = View.VISIBLE
                    }
                }
                R.id.watchlist->{
                    mBinding?.clNoInternet?.visibility = View.GONE
                    setCurrentFragment(todoFragment)
                }
            }
            true
        }
    }

    private fun mainfunction() {
        if (isNetworkConnected(this)) {
            mBinding?.clNoInternet?.visibility = View.GONE
            setCurrentFragment(movieFragment)
        } else {
            Glide.with(this).load(R.raw.tvf_roadies).into(mBinding!!.serverDown)
            mBinding?.clNoInternet?.visibility = View.VISIBLE
            Toast.makeText(this, R.string.connect_to_internet, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,fragment)
            commit()
        }


    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun retry(view: View) {
        mainfunction()
    }
}
