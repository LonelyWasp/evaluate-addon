package com.evaluate.lib.model

import androidx.annotation.DrawableRes

data class AndroidDialogConfig(
        val title: String,
        val description: String,
        val rateBtnText: String,
        val laterBtnText: String,
        val cancelIfTouchOutside: Boolean,
        val dialogCornerRadii: Float,
        @DrawableRes val icon: Int?,
)