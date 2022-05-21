package com.example.imagesearchapplication.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.imagesearchapplication.R
import com.example.imagesearchapplication.databinding.ImageDetailScreenActivityBinding
import com.example.imagesearchapplication.model.Hit

class ImageDetailScreenActivity : AppCompatActivity() {
    private lateinit var imageDetailScreenActivityBinding: ImageDetailScreenActivityBinding
    private var imageDetail: Hit? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageDetailScreenActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.image_detail_screen_activity)
        setToolBar()
        val intent = intent
        if (intent?.getSerializableExtra("detailImage") != null) {
            imageDetail = intent.getSerializableExtra("detailImage") as Hit
            imageDetailScreenActivityBinding.image = imageDetail
            imageDetailScreenActivityBinding.apply {
                Glide.with(ivLarge.context)
                    .load(imageDetail!!.webformatURL)
                    .placeholder(R.drawable.loading)
                    .into(ivLarge)
            }
            val resolution: String =
                imageDetail!!.imageWidth.toString() + " X " + imageDetail!!.imageHeight.toString()
            imageDetailScreenActivityBinding.tvImageResolution.text = resolution
        }
    }

    private fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Detail Image"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}