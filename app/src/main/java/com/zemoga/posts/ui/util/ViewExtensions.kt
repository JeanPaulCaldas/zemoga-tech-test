package com.zemoga.posts.ui.util

import android.view.View

var View.visible: Boolean
    get() = this.visibility == View.VISIBLE
    set(show) {
        this.visibility = if (show.not()) View.GONE else View.VISIBLE
    }