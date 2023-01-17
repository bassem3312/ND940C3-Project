package com.udacity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.util.EXTRA_DOWNLOAD_FILE_NAME
import com.udacity.util.EXTRA_DOWNLOAD_STATUS
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    private lateinit var activityDetailBinding: ActivityDetailBinding
    private var fileName: String = ""
    private var downloadStatus: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)


        activityDetailBinding.fabOk.setOnClickListener {
            finish()
        }
        setSupportActionBar(toolbar)
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val extras = intent.extras
        fileName = extras?.getString(EXTRA_DOWNLOAD_FILE_NAME, "0").toString()
        downloadStatus = extras?.getString(EXTRA_DOWNLOAD_STATUS, "0").toString()

        fileName.let { Log.e("==== File Name", it) }
        downloadStatus.let { Log.e("==== File Name", it) }

        activityDetailBinding.notification = fileName
        activityDetailBinding.status = downloadStatus
    }
}
