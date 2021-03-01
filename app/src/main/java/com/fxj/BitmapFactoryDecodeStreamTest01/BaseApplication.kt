package com.fxj.BitmapFactoryDecodeStreamTest01

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this);
    }
}