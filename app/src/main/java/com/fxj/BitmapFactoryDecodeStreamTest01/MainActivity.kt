package com.fxj.BitmapFactoryDecodeStreamTest01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout

class MainActivity : AppCompatActivity() {

    companion object{
        val TAG=MainActivity::class.java.simpleName
    }

    val url01="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbpic.588ku.com%2Fart_origin_min_pic%2F19%2F03%2F19%2Fbc64723701ef42e295aa2e69fa80474c.jpg&refer=http%3A%2F%2Fbpic.588ku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1616929101&t=8a6f21867e359a0097ea282facabd1d5"

    var rl01:RelativeLayout?=null
    var rl02:RelativeLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rl01=findViewById(R.id.rl01)
        ViewUtils.setBackground(rl01,url01)
        rl02=findViewById(R.id.rl02)
        ViewUtils.setBackgroundByFresco(rl02,url01)
    }
}