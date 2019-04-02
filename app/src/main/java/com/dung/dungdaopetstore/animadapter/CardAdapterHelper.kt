package com.dung.dungdaopetstore.animadapter

import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View


class CardAdapterHelper {

    private var mPagePadding = 10
    private var mShowLeftCardWidth = 10

    fun onCreateViewHolder(parent: ViewGroup, itemView: View) {
        val lp = itemView.getLayoutParams() as RecyclerView.LayoutParams
        lp.width = parent.width - ScreenUtil().dip2px(itemView.getContext(), 2 * (mPagePadding + mShowLeftCardWidth))
        itemView.setLayoutParams(lp)
    }

    fun onBindViewHolder(itemView: View, position: Int, itemCount: Int) {
        val padding = ScreenUtil().dip2px(itemView.getContext(), mPagePadding)
        itemView.setPadding(padding, 0, padding, 0)
        val leftMarin = if (position == 0) padding + ScreenUtil().dip2px(itemView.getContext(), mShowLeftCardWidth) else 0
        val rightMarin =
            if (position == itemCount - 1) padding + ScreenUtil().dip2px(itemView.getContext(), mShowLeftCardWidth) else 0
        setViewMargin(itemView, leftMarin, 0, rightMarin, 0)
    }

    private fun setViewMargin(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        val lp = view.getLayoutParams() as ViewGroup.MarginLayoutParams
        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom)
            view.setLayoutParams(lp)
        }
    }

    fun setPagePadding(pagePadding: Int) {
        mPagePadding = pagePadding
    }

    fun setShowLeftCardWidth(showLeftCardWidth: Int) {
        mShowLeftCardWidth = showLeftCardWidth
    }

}