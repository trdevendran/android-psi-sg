package com.trdevendran.assignmentpsi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trdevendran.assignmentpsi.model.PSIInfoResponse
import com.trdevendran.assignmentpsi.network.PSIInfo

/**
 * It behaves the bridge between view and model to sync data from model to populate it on view
 */
public class PSIInfoViewModel : ViewModel() {

    private var mutableLiveData: MutableLiveData<PSIInfoResponse>? = null
    private var psiInfo: PSIInfo? = null

    fun init() {
        if (mutableLiveData != null) {
            return
        }
        psiInfo = PSIInfo.getInstance()
        mutableLiveData = psiInfo!!.getLatestInfo()
    }

    fun getNewsRepository(): LiveData<PSIInfoResponse>? {
        return mutableLiveData
    }
}