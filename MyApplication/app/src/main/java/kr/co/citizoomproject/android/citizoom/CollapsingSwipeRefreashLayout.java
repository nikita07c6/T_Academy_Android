package kr.co.citizoomproject.android.citizoom;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

/**
 * Created by ccei on 2016-08-03.
 */
public class CollapsingSwipeRefreashLayout extends SwipeRefreshLayout {
    private View myScrollableView = null;

    public CollapsingSwipeRefreashLayout(Context context) {
        super(context);
    }

    public CollapsingSwipeRefreashLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTargetScrollableView(View view) {
        myScrollableView = view;
    }

    @Override
    public boolean canChildScrollUp() {
        if (myScrollableView == null) return false;

        if (Build.VERSION.SDK_INT < 14) {
            if(myScrollableView instanceof AbsListView){
                final AbsListView absListVIew = (AbsListView) myScrollableView;
                return absListVIew.getChildCount()>0 && (absListVIew.getFirstVisiblePosition()>0||absListVIew.getChildAt(0).getTop()<absListVIew.getPaddingTop());
            }else{
                return myScrollableView.getScrollY()>0;
            }
        } else {
            return ViewCompat.canScrollVertically(myScrollableView, -1);
        }


    }
}