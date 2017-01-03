package com.izeni.rapidocommon.recycler

import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView

/**
 * Mimics the behavior of ViewPagers
 * Created on 10/3/16.
 */
class PagerSnapHelper : LinearSnapHelper() {
    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
        val centerView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION

        val position = layoutManager.getPosition(centerView)
        var targetPosition = -1
        if (layoutManager.canScrollHorizontally()) {
            if (velocityX < 0) {
                targetPosition = position - 1
            } else {
                targetPosition = position + 1
            }
        }

        if (layoutManager.canScrollVertically()) {
            if (velocityY < 0) {
                targetPosition = position - 1
            } else {
                targetPosition = position + 1
            }
        }

        val firstItem = 0
        val lastItem = layoutManager.itemCount - 1
        targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem))
        return targetPosition
    }
}