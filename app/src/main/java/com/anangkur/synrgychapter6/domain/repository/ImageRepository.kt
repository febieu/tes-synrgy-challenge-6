package com.anangkur.synrgychapter6.domain.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.work.Data
import androidx.work.WorkInfo

interface ImageRepository {
    fun applyBlur(imageUri: Uri?)

    fun setInputDataForUri(imageUri: Uri?): Data

    fun getWorkManagerLiveData(): LiveData<List<WorkInfo>>

}