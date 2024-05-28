package com.xiaoyv.comic.flexiflix

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

lateinit var application: Application


/**
 * [MainApplication]
 *
 * @author why
 * @since 5/7/24
 */
@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        application = this
    }
}