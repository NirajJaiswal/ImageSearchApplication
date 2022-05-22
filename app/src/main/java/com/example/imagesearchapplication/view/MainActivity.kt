package com.example.imagesearchapplication.view

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagesearchapplication.R
import com.example.imagesearchapplication.databinding.ActivityMainBinding
import com.example.imagesearchapplication.listener.ImageListener
import com.example.imagesearchapplication.model.Hit
import com.example.imagesearchapplication.service.ApiHelper
import com.example.imagesearchapplication.service.RetrofitBuilder
import com.example.imagesearchapplication.utils.Status
import com.example.imagesearchapplication.utils.Util
import com.example.imagesearchapplication.viewmodel.MainViewModel
import com.example.imagesearchapplication.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity(), ImageListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private lateinit var activityMainBinding: ActivityMainBinding
    private var broadcastReceiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupViewModel()
        setToolBar()
        checkNetwork()
        activityMainBinding.searchButton.setOnClickListener {
            Util.hideKeyboard(this, it)
            val inputText = activityMainBinding.editTextSearch.text
            viewModel.getValue(inputText.toString())
            activityMainBinding.tvNoInternet.visibility = View.GONE
            setupObservers()
            activityMainBinding.recyclerView.scrollToPosition(0)

        }
    }

    private fun checkNetwork() {
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    try {
                        if (!Util.isOnline(context)) {
                            activityMainBinding.searchButton.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.image_search_hint_color
                                )
                            )
                            activityMainBinding.searchButton.isEnabled = false
                            activityMainBinding.tvNoInternet.visibility = View.VISIBLE
                            //     Log.e("Niraj", "Online Connect Intenet ")
                        } else {
                            activityMainBinding.searchButton.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.myColour
                                )
                            )
                            activityMainBinding.searchButton.isEnabled = true
                            activityMainBinding.tvNoInternet.visibility = View.GONE
                            //     Log.e("Niraj", "Conectivity Failure !!! ")
                        }
                        setupUI()
                        setupObservers()
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }.also {
                broadcastReceiver = it
            }, filter
        )
    }

    private fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = "Image Search"
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }


    private fun setupUI() {
        adapter = MainAdapter(arrayListOf(), this)
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            activityMainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            activityMainBinding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        }
        activityMainBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        activityMainBinding.recyclerView.setHasFixedSize(true)
        activityMainBinding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.data.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        activityMainBinding.recyclerView.visibility = View.VISIBLE
                        activityMainBinding.progressBar.visibility = View.GONE
                        resource.data?.let { image -> retrieveList(image.hits) }
                    }
                    Status.ERROR -> {
                        activityMainBinding.recyclerView.visibility = View.VISIBLE
                        activityMainBinding.progressBar.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        activityMainBinding.progressBar.visibility = View.VISIBLE
                        activityMainBinding.recyclerView.visibility = View.GONE
                    }
                }
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun retrieveList(imagesList: List<Hit>) {
        adapter.apply {
            addUsers(imagesList)
            notifyDataSetChanged()
        }
        // Log.i("Niraj", hits.toString())
    }

    override fun onImageClick(image: Hit) {
        val intent = Intent(this, ImageDetailScreenActivity::class.java)
        intent.putExtra("detailImage", image)
        startActivity(intent)
    }


}