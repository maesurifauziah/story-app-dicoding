package com.test.storyappsubmission2.ui.detailstory

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.marginTop
import com.bumptech.glide.Glide
import com.test.storyappsubmission2.R
import com.test.storyappsubmission2.databinding.ActivityDetailStoryBinding
import com.test.storyappsubmission2.utils.withDateFormat
import java.io.IOException
import java.util.*


class DetailStoryActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityDetailStoryBinding
    companion object {
        const val NAME = "name"
        const val CREATE_AT = "create_at"
        const val DESCRIPTION = "description"
        const val PHOTO_URL = "photoUrl"
        const val LONGITUDE = "lon"
        const val LATITUDE = "lat"
    }
    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val photoUrl = intent.getStringExtra(PHOTO_URL)
        val name = intent.getStringExtra(NAME)
        val create_at = intent.getStringExtra(CREATE_AT)
        val description = intent.getStringExtra(DESCRIPTION)
        val lon = intent.getStringExtra(LONGITUDE)!!.toDouble()
        val lat = intent.getStringExtra(LATITUDE)!!.toDouble()
        val location = getAddressName(lat, lon)

        Glide.with(binding.root.context)
            .load(photoUrl)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = name
        binding.tvDetailCreatedTime.text = create_at?.withDateFormat()
        binding.tvDetailDescription.text = description
        binding.tvDetailLocation.text = location

        if (lon == 0.0 && lat == 0.0) {
            binding.avatar2.visibility = View.INVISIBLE
        } else {
            binding.avatar2.visibility = View.VISIBLE
        }
    }

    fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@DetailStoryActivity)
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d(ContentValues.TAG, "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }
//    fun getAddressName(context: Context, lat: Double, lon: Double): String {
//        val geocoder = Geocoder(context)
//        val geoLocation =
//            geocoder.getFromLocation(lat, lon, 1)
//        return if (geoLocation.size > 0) {
//            val location = geoLocation[0]
//            val fullAddress = location.getAddressLine(0)
//            StringBuilder("📌 ")
//                .append(fullAddress).toString()
//        } else {
//            "📌 Location Unknown"
//        }
//    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}