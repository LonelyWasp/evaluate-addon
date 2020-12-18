package com.evaluate.lib.callbacks

interface ReviewListener {
    fun onInAppReviewCompleted()
    fun onNoThanksBtnClicked()
    fun onRemindLaterClicked()
}