package com.trdevendran.assignmentpsi.util

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.TextUtils
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog

/**
 * This class contains static method of the reusable methods to avoid redundant in different places
 */
class CommonSnippets {

    companion object {
        /**
         * @param context of the current Activity
         * @return true if the network info is available, otherwise false
         */
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivity =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivity.allNetworkInfo
            for (i in info.indices) {
                if (info[i].state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
            return false
        }

        /**
         * @param context of the current Activity
         * @param title shows the short text/ purpose of the dialog window appears
         * @param message Shows the description/detailed message on the dialog window
         * @param buttonText It represents the text of positive button
         * @param onClickListener It handles the event of positive button of an AlertDialog
         * @return AlertDialog to show the informative content as an alert
         */
        fun createAlertDialog(
            context: Context, title: String, message: String,
            buttonText: String, onClickListener: DialogInterface.OnClickListener
        ): AlertDialog {
            return createAlertDialogWithView(
                context,
                title,
                message,
                buttonText,
                onClickListener,
                null
            )

        }

        /**
         * @param context of the current Activity
         * @param title shows the short text/ purpose of the dialog window appears
         * @param message Shows the description/detailed message on the dialog window
         * @param buttonText It represents the text of positive button
         * @param onClickListener It handles the event of positive button of an AlertDialog
         * @param view uses to show the dynamic
         * @return AlertDialog to show the informative content as an alert
         */
        fun createAlertDialogWithView(
            context: Context, title: String, message: String,
            buttonText: String, onClickListener: DialogInterface.OnClickListener, view: View?
        ): AlertDialog {
            val alertDialog = AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(buttonText, onClickListener)
                .create()
            if (TextUtils.isEmpty(title)) {
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            }
            return alertDialog

        }
    }
}