package com.meria.playtaylermel.ui

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.huawei.hms.jos.AppUpdateClient
import com.huawei.hms.jos.JosApps
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack
import com.huawei.updatesdk.service.otaupdate.UpdateKey
import com.meria.playtaylermel.manager.IUpdateVersionManager
import com.meria.playtaylermel.util.DEFAULT

class UpdateVersionManager (private val context: Activity, private val listener: IUpdateVersionManager):
    CheckUpdateCallBack {
    private var client: AppUpdateClient? = null
    var flag = true


    fun checkAppUpdate(){
        client = JosApps.getAppUpdateClient(context)
        client?.checkAppUpdate(context, this)
    }

    override fun onMarketStoreError(p0: Int) {
        //not code
    }

    override fun onUpdateStoreError(p0: Int) {
        //not code
    }

    override fun onUpdateInfo(intent: Intent?) {
        if (intent != null) {
            val status =
                intent.getIntExtra(UpdateKey.STATUS, DEFAULT)
            val rtnCode =
                intent.getIntExtra(UpdateKey.FAIL_CODE, DEFAULT)
            val rtnMessage = intent.getStringExtra(UpdateKey.FAIL_REASON)
            val info = intent.getSerializableExtra(UpdateKey.INFO)
            if (flag){
                flag = false
                if (info is ApkUpgradeInfo) {
                    val upgradeInfo = info
                    checkUpdatePop( upgradeInfo )
                    listener.onShowUpdateDialog()
                    context.finish()
                    Log.d("dataUpdate","There is a new update")
                }else{
                    listener.onNotUpdateDialog()
                    Log.d("dataUpdate","onUpdateInfo status: $status, rtnCode: $rtnCode, rtnMessage: $rtnMessage")
                }
            }


        }
    }

    private fun checkUpdatePop(apkUpgradeInfo : ApkUpgradeInfo) {
        client?.showUpdateDialog(context, apkUpgradeInfo, false)
        Log.d("dataUpdateForc", "checkUpdatePop success")
    }

    override fun onMarketInstallInfo(p0: Intent?) {
        //not code
    }
}