package com.evaluate.lib

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.afollestad.materialdialogs.MaterialDialog
import com.evaluate.lib.callbacks.InAppReviewListener
import com.evaluate.lib.model.AndroidDialogConfig
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class ReviewController {

    private lateinit var reviewManager: ReviewManager

    fun showReviewDialog(activity: Activity, config: AndroidDialogConfig) {
        MaterialDialog(activity).show {

            title(text = activity.getString(R.string.evaluate_title_format, config.appName))
            message(text = activity.getString(R.string.evaluate_description_format, config.appName))
            cornerRadius(config.dialogCornerRadii)
            cancelOnTouchOutside(config.cancelIfTouchOutside)
            config.icon?.let {
                icon(it)
            }

            positiveButton(text = activity.getString(R.string.evaluate_write_review)) { dialog ->
                activity.openGooglePlay(config.appPackageName)
                dialog.dismiss()
            }
            negativeButton(text = activity.getString(R.string.evaluate_remind_later)) { dialog ->

                dialog.dismiss()
            }
        }
    }

    private fun Context.openGooglePlay(appPackageName: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$MARKET_DETAILS$appPackageName")))
        } catch (ex: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$WEB_GOOGLE_PLAY$appPackageName")))
        }
    }

    fun showInAppReview(activity: Activity, listener: InAppReviewListener) {
        reviewManager = ReviewManagerFactory.create(activity)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    listener.onReviewCompleted()
                }
            }
        }
    }
}

private const val MARKET_DETAILS = "market://details?id="
private const val WEB_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id="