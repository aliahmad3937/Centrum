package com.cc.cenntrum.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import com.cc.cenntrum.utils.LoadingUtils
import android.os.Build
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import com.cc.cenntrum.R
import android.widget.TextView
import java.util.*

class LoadingUtils {
    private val context: Context? = null

    companion object {
        private const val TAG = "LoadingUtils"
        var lotiedilouge: Dialog? = null
        fun showLoading(activity: Activity?) {
            lotiedilouge = Dialog(activity!!)
            lotiedilouge!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Optional.ofNullable(lotiedilouge!!.window).ifPresent { window: Window ->
                    window.setBackgroundDrawable(
                        ColorDrawable(
                            Color.TRANSPARENT
                        )
                    )
                }
            }
            lotiedilouge!!.setCancelable(true)
            lotiedilouge!!.setContentView(R.layout.loading_dilouge_lottie)
            lotiedilouge!!.show()
        }

        fun setMessage(message: String) {
            val textView = lotiedilouge!!.findViewById<TextView>(R.id.message_tv)
            if (!message.isEmpty()) {
                textView.text = message
            }
        }

        fun pauseLoading() {
            if (lotiedilouge != null && lotiedilouge!!.isShowing) {
                Log.d(TAG, "pauseLotieProgressDialog: ")
                lotiedilouge!!.cancel()
                lotiedilouge!!.dismiss()
            }
        }
    }



}