package com.meria.playtaylermel.ui

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.meria.playtaylermel.manager.IUpdateVersionManager


class UpdateVersionManager(private val context: Activity,private val listener: IUpdateVersionManager){

    private  val UPDATE_CODE = 10001

    fun checkAppUpdate(){
        val appUpdateManager = AppUpdateManagerFactory.create(context)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, context, UPDATE_CODE)
                context.finish()
                listener.onShowUpdateDialog()
            } else listener.onNotUpdateDialog()

        }
        appUpdateInfoTask.addOnFailureListener {
            listener.onNotUpdateDialog()
        }
    }
}