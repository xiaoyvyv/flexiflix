@file:Suppress("DEPRECATION")

package com.xiaoyv.flexiflix.extension.utils

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat

val Context.versionCode: Long
    get() {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            return PackageInfoCompat.getLongVersionCode(packageInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

val Context.versionName: String
    get() {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            return packageInfo.versionName.orEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
