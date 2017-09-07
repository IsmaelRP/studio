package es.iessaladillo.pedrojoya.pr107.oldutils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// De la librería https://github.com/ismoli/DynamicRecyclerView.
// Basada a su vez en https://github.com/romannurik/Android-SwipeToDismiss
@SuppressWarnings("unused")
public class SwipeToDismissTouchListener implements RecyclerView.OnItemTouchListener {

    private final RecyclerView mRecyclerView;
    private final int mSlop;
    private final int mMinFlingVelocity;
    private final int mMaxFlingVelocity;
    private final long mAnimationTime;

    private final DismissCallbacks mCallbacks;
    private int mViewWidth = 1; // 1 and not 0 to prevent dividing by zero

    // Transient properties
    private float mDownX;
    private float mDownY;
    private boolean mSwiping;
    private int mSwipingSlop;
    private VelocityTracker mVelocityTracker;
    private float mTranslationX;
    private boolean mPaused = false;
    private View mSwipeView;
    private int mDismissCount = 0;
    private final List<PendingDismissData> mPendingDismisses = new ArrayList<>();
    private SwipeDirection mAllowedSwipeDirection = SwipeDirection.NONE;


    /**
     * Constructs a new swipe-to-dismiss OnItemTouchListener for RecyclerView
     *
     * @param recyclerView RecyclerView
     * @param callbacks    The callback to trigger when the user has
     *                     indicated that she would like to dismiss this view.
     */
    public SwipeToDismissTouchListener(RecyclerView recyclerView, DismissCallbacks callbacks) {
        ViewConfiguration vc = ViewConfiguration.get(recyclerView.getContext());
        mSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity() * 4;
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        mAnimationTime = recyclerView.getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        mRecyclerView = recyclerView;
        mCallbacks = callbacks;
    }

    public void setEnabled(boolean enabled) {
        mPaused = !enabled;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        motionEvent.offsetLocation(mTranslationX, 0);
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                up(motionEvent);
                break;
            case MotionEvent.ACTION_CANCEL:
                cancel();
                break;
            case MotionEvent.ACTION_MOVE:
                move(motionEvent);
                break;
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public boolean onInterceptTouchEvent(final RecyclerView view, MotionEvent motionEvent) {
        if (mPaused) {
            return false;
        }
        // offset because the view is translated during swipe
        motionEvent.offsetLocation(mTranslationX, 0);

        if (mViewWidth < 2) {
            mViewWidth = view.getWidth();
        }

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return down(motionEvent);
            case MotionEvent.ACTION_MOVE:
                return move(motionEvent);
        }
        return false;
    }

    @SuppressWarnings("SameReturnValue")
    private boolean down(MotionEvent motionEvent) {
        if (mPaused) {
            return false;
        }
        mDownX = motionEvent.getRawX();
        mDownY = motionEvent.getRawY();
        mSwipeView = mRecyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (mSwipeView == null) {
            return false;
        }
        int pos = mRecyclerView.getChildAdapterPosition(mSwipeView);
        mAllowedSwipeDirection = mCallbacks.canDismiss(pos);
        if (mAllowedSwipeDirection != SwipeDirection.NONE) {

            mVelocityTracker = VelocityTracker.obtain();
            mVelocityTracker.addMovement(motionEvent);
            return false;
        }
        resetMotion();
        return false;
    }

    private void cancel() {
        if (mVelocityTracker == null) {
            return;
        }

        mSwipeView.animate().translationX(0).alpha(1).setDuration(mAnimationTime).setListener(null);
        mVelocityTracker.recycle();
        mVelocityTracker = null;
        mTranslationX = 0;
        mDownX = 0;
        mDownY = 0;
        mSwiping = false;
        mSwipeView = null;
    }

    private void up(MotionEvent motionEvent) {
        if (mPaused || mVelocityTracker == null || mSwipeView == null) {
            return;
        }
        mSwipeView.setPressed(false);
        float deltaX = motionEvent.getRawX() - mDownX;
        mVelocityTracker.addMovement(motionEvent);
        mVelocityTracker.computeCurrentVelocity(1000);
        float velocityX = mVelocityTracker.getXVelocity();
        float absVelocityX = Math.abs(velocityX);
        float absVelocityY = Math.abs(mVelocityTracker.getYVelocity());
        boolean dismiss = false;
        boolean dismissRight = false;
        if (Math.abs(deltaX) > mViewWidth / 2 && mSwiping) {
            dismiss = true;
            dismissRight = deltaX > 0;
        } else if (mMinFlingVelocity <= absVelocityX && absVelocityX <= mMaxFlingVelocity
                && absVelocityY < absVelocityX && absVelocityY < absVelocityX && mSwiping) {
            // dismiss only if flinging in the same direction as dragging
            dismiss = (velocityX < 0) == (deltaX < 0);
            dismissRight = mVelocityTracker.getXVelocity() > 0;
        }
        if (dismiss) {
            // dismiss
            final int pos = mRecyclerView.getChildAdapterPosition(mSwipeView);
            final View swipeViewCopy = mSwipeView;
            final SwipeDirection swipeDirection = dismissRight ? SwipeDirection.RIGHT
                                                               : SwipeDirection.LEFT;
            ++mDismissCount;
            mSwipeView.animate()
                    .translationX(dismissRight ? mViewWidth : -mViewWidth)
                    .alpha(0)
                    .setDuration(mAnimationTime);

            //this is instead of unreliable onAnimationEnd callback
            swipeViewCopy.postDelayed(() -> {
                performDismiss(swipeViewCopy, pos, swipeDirection);
                swipeViewCopy.setTranslationX(0);
                //                    swipeViewCopy.setAlpha(1);

            }, mAnimationTime + 100);

        } else if (mSwiping) {
            // cancel
            mSwipeView.animate().translationX(0).alpha(1).setDuration(mAnimationTime).setListener(
                    null);
        }


        resetMotion();
    }

    private boolean move(MotionEvent motionEvent) {
        if (mSwipeView == null || mVelocityTracker == null || mPaused) {
            return false;
        }

        mVelocityTracker.addMovement(motionEvent);
        float deltaX = motionEvent.getRawX() - mDownX;
        float deltaY = motionEvent.getRawY() - mDownY;
        if (Math.abs(deltaX) > mSlop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
            mSwiping = true;
            mSwipingSlop = deltaX > 0 ? mSlop : -mSlop;
            mSwipeView.setPressed(false);

            MotionEvent cancelEvent = MotionEvent.obtain(motionEvent);
            cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (motionEvent.getActionIndex()
                    << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
            mSwipeView.onTouchEvent(cancelEvent);
        }

        //Prevent swipes to disallowed directions
        if ((deltaX < 0 && mAllowedSwipeDirection == SwipeDirection.RIGHT) || (deltaX > 0
                && mAllowedSwipeDirection == SwipeDirection.LEFT)) {
            resetMotion();
            return false;
        }

        if (mSwiping) {
            mCallbacks.onTouchDown();
            mTranslationX = deltaX;
            mSwipeView.setTranslationX(deltaX - mSwipingSlop);
            mSwipeView.setAlpha(
                    Math.max(0f, Math.min(1f, 1f - 2f * Math.abs(deltaX) / mViewWidth)));
            return true;
        }
        return false;
    }

    private void resetMotion() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
        mCallbacks.onResetMotion();
        mTranslationX = 0;
        mDownX = 0;
        mDownY = 0;
        mSwiping = false;
        mSwipeView = null;
        mAllowedSwipeDirection = SwipeDirection.NONE;
    }

    private void performDismiss(View dismissView, int pos, SwipeDirection direction) {
        --mDismissCount;
        mPendingDismisses.add(new PendingDismissData(pos, dismissView, direction));
        if (mDismissCount == 0) {
            Collections.sort(mPendingDismisses);
            List<PendingDismissData> dismissData = new ArrayList<>(mPendingDismisses);
            mCallbacks.onDismiss(mRecyclerView, dismissData);
            mPendingDismisses.clear();
        }
    }

    @SuppressWarnings("unused")
    public interface DismissCallbacks {
        SwipeDirection canDismiss(int position);

        void onDismiss(RecyclerView view, List<PendingDismissData> dismissData);

        void onResetMotion();

        void onTouchDown();
    }

    public class PendingDismissData implements Comparable<PendingDismissData> {
        public final int position;
        public final View view;
        public final SwipeDirection direction;

        public PendingDismissData(int position, View view, SwipeDirection direction) {
            this.position = position;
            this.view = view;
            this.direction = direction;

        }

        @Override
        public int compareTo(@NonNull PendingDismissData other) {
            return other.position - position;
        }
    }

    public enum SwipeDirection {
        LEFT, RIGHT, BOTH, NONE
    }

}
