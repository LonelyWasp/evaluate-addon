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
            title(text = config.title)
            message(text = config.description)
            cornerRadius(config.dialogCornerRadii)
            cancelOnTouchOutside(config.cancelIfTouchOutside)
            config.icon?.let {
                icon(it)
            }

            positiveButton(text = config.rateBtnText) { dialog ->
                activity.openGooglePlay()
                dialog.dismiss()
            }
            negativeButton(text = config.laterBtnText) { dialog ->

                dialog.dismiss()
            }
        }
    }

    fun Context.openGooglePlay() {
        val appPackageName: String = packageName

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


private const val REVIEW_DEFAULT_TITLE = "Rate our app"
private const val REVIEW_DEFAULT_DESCRIPTION =
        "If you enjoy using our app, would you mind taking a moment to rate it? It won\'t take more than a minute. Thanks for your support!"
private const val REVIEW_DEFAULT_BTN_DONT_SHOW_AGAIN = "Don\'t show again"
private const val REVIEW_DEFAULT_BTN_WRITE_REVIEW = "Write a review in Google Play"
private const val REVIEW_DEFAULT_BTN_REMIND_LATER = "Remind me later"
private const val REVIEW_DEFAULT_ERROR_SHOW_IN_APP_REVIEW =
        "An error occurred. Please try again later!"