package com.fxj.BitmapFactoryDecodeStreamTest01

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import java.io.IOException
import java.net.URL

class ViewUtils {
    companion object{
        val TAG=ViewUtils::class.java.simpleName

        /**
         * 从网络给一个View设置一个BitmapDrawable作为Background
         * @param view
         * @param drawableResUrl 图片资源Url
         */
        fun setBackground(view: View?, drawableResUrl: String) {
            if (TextUtils.isEmpty(drawableResUrl) || view == null) {
                return
            }
            Log.d(TAG, "##setBackground##drawableResUrl=$drawableResUrl")
            try {
                var runnable:Runnable=Runnable(){
                    val drawableResURI = URL(drawableResUrl)
                    val conn = drawableResURI.openConnection()
                    conn.connect()
                    val mInputStream = conn.getInputStream()
                    val bitmap = BitmapFactory.decodeStream(mInputStream)
                    Log.d(TAG, "##setBackground##bitmap.byteCount=${if(bitmap!=null) bitmap.byteCount else -1}")

                    Handler(view.context.mainLooper).post (object : Runnable {
                        override fun run() {
                            if (bitmap != null) {
                                val bitmapDrawable = BitmapDrawable(view.resources, bitmap)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    view.background = bitmapDrawable
                                } else {
                                    view.setBackgroundDrawable(bitmapDrawable)
                                }
                            }
                        }
                    })
                }
                Thread(runnable).start()
            } catch (e: IOException) {
                Log.e(TAG,"##setBackground##IOException,e.message=${e.message},e.localizedMessage=${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }


}