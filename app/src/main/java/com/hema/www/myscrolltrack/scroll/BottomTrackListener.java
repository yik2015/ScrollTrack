package com.hema.www.myscrolltrack.scroll;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;

/**
 * Created by Administrator on 2016/5/6.
 */
public class BottomTrackListener extends RecyclerView.OnScrollListener {
    private static final String TAG = BottomTrackListener.class.getSimpleName();
    private View mTargetView = null;
    private boolean isAlreadyHide = false, isAlreadyShow = false;

    private ObjectAnimator mAnimator = null;

    private int mLastDy = 0;

    public BottomTrackListener(View target) {
        if (target == null) {
            throw new IllegalArgumentException("target shouldn't be null!");
        }
        mTargetView = target;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            final float transY = mTargetView.getTranslationY();
            if (transY == 0 || transY == getDistance()) {
                return;
            }
            if (mLastDy > 0) {
                mAnimator = animateHide(mTargetView);
            } else {
                mAnimator = animateShow(mTargetView);
            }
        } else {
            if (mAnimator != null && mAnimator.isRunning()) {
                mAnimator.cancel();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        mLastDy = dy;
        final float transY = mTargetView.getTranslationY();

        float newsTransY = transY + dy;
        final int distance = getDistance();

        if (newsTransY < 0) newsTransY = 0;
        else if (newsTransY > distance) newsTransY = distance;
        if (newsTransY == 0 && isAlreadyShow) return;
        if (newsTransY == distance && isAlreadyHide) return;

        mTargetView.setTranslationY(newsTransY);
        isAlreadyHide = newsTransY == distance;
        isAlreadyShow = newsTransY == 0;
    }

    private int getDistance() {
        ViewParent parent = mTargetView.getParent();
        if (parent instanceof View) {
            return ((View) parent).getHeight() - mTargetView.getTop();
        }
        return 0;
    }

    private ObjectAnimator animateShow(View view) {
        return animatorFromTo(view, view.getTranslationY(), 0);
    }

    private ObjectAnimator animateHide(View view) {
        return animatorFromTo(view, view.getTranslationY(), getDistance());
    }

    private ObjectAnimator animatorFromTo(View view, float start, float end) {
        String propertyName = "translationY";
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, propertyName, start, end);
        animator.start();
        return animator;
    }
}
