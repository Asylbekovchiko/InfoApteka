package kg.sunrise.infoapteka.ui.shared.farmAdressDivider

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class DividerItemDecoration : ItemDecoration {
    private var mDivider: Drawable?
    private var mShowFirstDivider = false
    private var mShowLastDivider = false
    var mOrientation = -1

    constructor(context: Context?, attrs: AttributeSet?) {
        val a: TypedArray? = context
            ?.obtainStyledAttributes(attrs, intArrayOf(R.attr.listDivider))
        mDivider = a?.getDrawable(0)
        a?.recycle()
    }

    constructor(
        context: Context?, attrs: AttributeSet?, showFirstDivider: Boolean,
        showLastDivider: Boolean
    ) : this(context, attrs) {
        mShowFirstDivider = showFirstDivider
        mShowLastDivider = showLastDivider
    }

    constructor(context: Context?, resId: Int) {
        mDivider = context?.let { ContextCompat.getDrawable(it, resId) }
    }

    constructor(
        context: Context?, resId: Int, showFirstDivider: Boolean,
        showLastDivider: Boolean
    ) : this(context, resId) {
        mShowFirstDivider = showFirstDivider
        mShowLastDivider = showLastDivider
    }

    constructor(divider: Drawable?) {
        mDivider = divider
    }

    constructor(
        divider: Drawable?, showFirstDivider: Boolean,
        showLastDivider: Boolean
    ) : this(divider) {
        mShowFirstDivider = showFirstDivider
        mShowLastDivider = showLastDivider
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (view != null) {
            super.getItemOffsets(outRect, view, parent, state)
        }
        if (mDivider == null) {
            return
        }
        val position = view?.let { parent.getChildAdapterPosition(it) }
        if (position == RecyclerView.NO_POSITION || position == 0 && !mShowFirstDivider) {
            return
        }
        if (mOrientation == -1) getOrientation(parent)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.top = mDivider!!.intrinsicHeight
            if (mShowLastDivider && position == state.itemCount - 1) {
                outRect.bottom = outRect.top
            }
        } else {
            outRect.left = mDivider!!.intrinsicWidth
            if (mShowLastDivider && position == state.itemCount - 1) {
                outRect.right = outRect.left
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mDivider == null) {
            super.onDrawOver(c, parent, state)
            return
        }

        // Initialization needed to avoid compiler warning
        var left = 0
        var right = 0
        var top = 0
        var bottom = 0
        val size: Int
        val orientation = if (mOrientation != -1) mOrientation else getOrientation(parent)
        val childCount = parent.childCount
        if (orientation == LinearLayoutManager.VERTICAL) {
            size = mDivider!!.intrinsicHeight
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
        } else { //horizontal
            size = mDivider!!.intrinsicWidth
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
        }
        val num = if (mShowFirstDivider) 0 else 1
        for (i in  num until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.getLayoutParams() as RecyclerView.LayoutParams
            if (orientation == LinearLayoutManager.VERTICAL) {
                top = child.getTop() - params.topMargin - size
                bottom = top + size
            } else { //horizontal
                left = child.getLeft() - params.leftMargin
                right = left + size
            }
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)
        }

        // show last divider
        if (mShowLastDivider && childCount > 0) {
            val child: View = parent.getChildAt(childCount - 1)
            if (parent.getChildAdapterPosition(child) == state.itemCount - 1) {
                val params = child.getLayoutParams() as RecyclerView.LayoutParams
                if (orientation == LinearLayoutManager.VERTICAL) {
                    top = child.getBottom() + params.bottomMargin
                    bottom = top + size
                } else { // horizontal
                    left = child.getRight() + params.rightMargin
                    right = left + size
                }
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(c)
            }
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        if (mOrientation == -1) {
            mOrientation = if (parent.layoutManager is LinearLayoutManager) {
                val layoutManager = parent.layoutManager as LinearLayoutManager?
                layoutManager!!.orientation
            } else {
                throw IllegalStateException(
                    "DividerItemDecoration can only be used with a LinearLayoutManager."
                )
            }
        }
        return mOrientation
    }
}