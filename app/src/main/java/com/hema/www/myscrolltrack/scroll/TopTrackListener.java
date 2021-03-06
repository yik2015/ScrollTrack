package com.hema.www.myscrolltrack.scroll;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/5/6.
 */
public class TopTrackListener extends RecyclerView.OnScrollListener {
    private static final String TAG = TopTrackListener.class.getSimpleName();

    private int mLastDy = 0;
    private int mTotalDy = 0;

    private View mTargetView;

    private ObjectAnimator mAnimator;

    private boolean isAlreadyHide = false, isAlreadyShow = false;

    public TopTrackListener(View target) {
        if (target == null) {
            throw new IllegalArgumentException("target shouldn't be null!");
        }
        mTargetView = target;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                final float transY = mTargetView.getTranslationY();
                int distance = -mTargetView.getBottom();

//                Log.d(TAG, "transY == " + transY + ", distance == "
//                        + distance + ", mLastDy == " + mLastDy);

                // transY == 0, hided. transY == distance, showed.
                if (transY == 0 || transY == distance) {
                    return;
                }

                // scroll down(fingers up), dy > 0, need to hide.
                // scroll up(fingers down), dy < 0, need to show.
                if (mLastDy > 0) {
                    mAnimator = animateHide(mTargetView);
                } else {
                    mAnimator = animateShow(mTargetView);
                }
                break;

            case RecyclerView.SCROLL_STATE_DRAGGING:
                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.cancel();
                }
                break;

            case RecyclerView.SCROLL_STATE_SETTLING:
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mTotalDy -= dy;
        mLastDy = dy;

        final float transY = mTargetView.getTranslationY();
        float newTransY;

        int distance = -mTargetView.getBottom();

        Log.d(TAG, "dx == " + dx + ", dy == " + dy + ", mTotalDy == " + mTotalDy +
                ", distance ==" + distance + ", transY == " + transY);

        if (mTotalDy >= distance && dy > 0) return;
        if (isAlreadyHide && dy > 0) return;
        if (isAlreadyShow && dy < 0) return;

        newTransY = transY - dy;

        if (newTransY < distance) newTransY = distance;
        else if (newTransY > 0) newTransY = 0;
        else if (newTransY == distance || newTransY == 0) return;

        mTargetView.setTranslationY(newTransY);
        isAlreadyHide = newTransY == distance;
        isAlreadyShow = newTransY == 0;

        Log.d(TAG, "------- onScrolled ---------");
    }

    private ObjectAnimator animateHide(View targetView) {
        int distance = -targetView.getBottom();
        return animationFromTo(targetView, targetView.getTranslationY(), distance);
    }

    private ObjectAnimator animateShow(View targetView) {
        return animationFromTo(targetView, targetView.getTranslationY(), 0);
    }

    private ObjectAnimator animationFromTo(View view, float start, float end) {
        String propertyName = "translationY";
        Log.d(TAG, "animate, start == " + start + ", end == " + end);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, propertyName,
                start, end);
        animator.start();
        return animator;
    }
}
