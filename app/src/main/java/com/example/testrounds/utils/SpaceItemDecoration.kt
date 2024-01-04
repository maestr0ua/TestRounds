package com.example.testrounds.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class SpaceItemDecoration(context: Context, space: Int) : ItemDecoration() {

    private val space: Float

    init {
        this.space = space * context.resources.displayMetrics.density
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = space.toInt()

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space.toInt()
        } else {
            outRect.top = 0
        }
    }
}