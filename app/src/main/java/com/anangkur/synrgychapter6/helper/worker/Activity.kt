package com.anangkur.synrgychapter6.helper.worker

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun Activity.requestPermissions(
    permissions: Array<String>,
    requestCode: Int,
    doIfGranted: () -> Unit = {},
) {
    val deniedPermissions = mutableListOf<String>()
    permissions.forEach { permission ->
        if (!isGranted(permission)) {
            deniedPermissions.add(permission)
        }
    }
    if (deniedPermissions.isNotEmpty()) {
        ActivityCompat.requestPermissions(this, deniedPermissions.toTypedArray(), requestCode)
    } else {
        doIfGranted()
    }
}

fun Activity.isGranted(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}