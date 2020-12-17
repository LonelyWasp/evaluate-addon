package com.evaluate.lib.model

import androidx.annotation.DrawableRes

data class AndroidDialogConfig(
        val appName: String,
        val appPackageName: String,
        val cancelIfTouchOutside: Boolean,
        val dialogCornerRadii: Float,
        @DrawableRes val icon: Int?,
)