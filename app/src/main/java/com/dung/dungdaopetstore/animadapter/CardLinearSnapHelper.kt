package com.dung.dungdaopetstore.animadapter

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.View


class CardLinearSnapHelper {

    var mNoNeedToScroll = false

    fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray {
        return if (mNoNeedToScroll) {
            intArrayOf(0, 0)
        } else {
            calculateDistanceToFinalSnap(layoutManager, targetView)
        }
    }

}