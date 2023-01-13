package com.udacity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.udacity.util.EXTRA_DOWNLOAD_FILE_NAME
import com.udacity.util.EXTRA_DOWNLOAD_STATUS
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val extras = intent.extras
        var fileName = extras?.getString(EXTRA_DOWNLOAD_FILE_NAME, "0")
        var downloadStatus = extras?.getString(EXTRA_DOWNLOAD_STATUS, "0")

        fileName?.let { Log.e("==== File Name", it) }
        downloadStatus?.let { Log.e("==== File Name", it) }
    }
}
