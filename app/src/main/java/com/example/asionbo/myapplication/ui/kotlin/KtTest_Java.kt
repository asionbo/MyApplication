package com.example.asionbo.myapplication.ui.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.example.asionbo.myapplication.R
import kotlinx.android.synthetic.main.activity_kt_test.*

/**
 * Created by asionbo on 2017/11/23.
 */

class KtTest_Java : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kt_test)

        btn_change.setOnClickListener({
            tv_show.text = "你好，大兄弟!"
        })

        btn_change.setOnLongClickListener {
            tv_show.text = "Don't you have bigger number?"
            false
        }
    }
}
