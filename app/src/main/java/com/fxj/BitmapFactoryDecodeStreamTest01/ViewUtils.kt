package com.fxj.BitmapFactoryDecodeStreamTest01

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.DefaultExecutorSupplier
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequest
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executor


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
                    Log.d(
                        TAG,
                        "##setBackground##bitmap.byteCount=${if (bitmap != null) bitmap.byteCount else -1}"
                    )

                    Handler(view.context.mainLooper).post(object : Runnable {
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
                Log.e(
                    TAG,
                    "##setBackground##IOException,e.message=${e.message},e.localizedMessage=${e.localizedMessage}"
                )
                e.printStackTrace()
            }
        }

        fun setBackgroundByFresco(view: View?, drawableResUrl: String){
            if(view==null||TextUtils.isEmpty(drawableResUrl)){
                if(BuildConfig.DEBUG){
                    throw Exception("请检查传入的view或者drawableResUrl是否为空!")
                }
                return;
            }
            var imageRequest: ImageRequest=ImageRequest.fromUri(drawableResUrl)

            val imagePipeline = Fresco.getImagePipeline()
            val dataSource: DataSource<CloseableReference<CloseableImage>> =
                imagePipeline.fetchDecodedImage(imageRequest, view?.context)

            val cpuAvilableNumbers=Runtime.getRuntime().availableProcessors()
            Log.d(TAG,"##setBackgroundByFresco##cpuAvilableNumbers=${cpuAvilableNumbers}")

            var executor: Executor= DefaultExecutorSupplier(cpuAvilableNumbers).forBackgroundTasks()

            dataSource.subscribe(
                object : BaseBitmapDataSubscriber() {
                    override fun onNewResultImpl(bitmap: Bitmap?) {
                        Log.d(TAG,"##setBackgroundByFresco.onNewResultImpl##bitmap.byteCount=${if(bitmap!=null) bitmap.byteCount else -1}")
                        if(bitmap==null){
                            return
                        }

                        UiThreadImmediateExecutorService.getInstance().execute(object:Runnable{
                            override fun run() {
                                val bitmapDrawable = BitmapDrawable(view.resources, bitmap)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    view.background = bitmapDrawable
                                } else {
                                    view.setBackgroundDrawable(bitmapDrawable)
                                }
                            }
                        })
                    }

                    override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
                        Log.d(TAG,"##setBackgroundByFresco.onFailureImpl##")
                    }

                },
                executor
            )
        }
    }


}