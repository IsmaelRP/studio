package es.iessaladillo.pedrojoya.pr246.base;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import android.text.TextUtils;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

public class FirstLevelActionBarOnNavigatedListener implements NavController.OnNavigatedListener {
    private final AppCompatActivity mActivity;
    @Nullable
    private final DrawerLayout mDrawerLayout;
    private final int[] mFirstLevelDestinationIds;
    private DrawerArrowDrawable mArrowDrawable;
    private ValueAnimator mAnimator;

    public FirstLevelActionBarOnNavigatedListener(@NonNull AppCompatActivity activity,
            @NonNull DrawerLayout drawerLayout, @NonNull int[] firstLevelDestinationIds) {
        mActivity = activity;
        mDrawerLayout = drawerLayout;
        mFirstLevelDestinationIds = firstLevelDestinationIds;
    }

    @Override
    public void onNavigated(@NonNull NavController controller,
            @NonNull NavDestination destination) {
        ActionBar actionBar = mActivity.getSupportActionBar();
        CharSequence title = destination.getLabel();
        if (!TextUtils.isEmpty(title)) {
            actionBar.setTitle(title);
        }
        boolean isFirstLevelDestination = contains(mFirstLevelDestinationIds, destination.getId());

        actionBar.setDisplayHomeAsUpEnabled(mDrawerLayout != null || !isFirstLevelDestination);
        setActionBarUpIndicator(mDrawerLayout != null && isFirstLevelDestination);
    }

    private boolean contains(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) return true;
        }
        return false;
    }

    void setActionBarUpIndicator(boolean showAsDrawerIndicator) {
        ActionBarDrawerToggle.Delegate delegate = mActivity.getDrawerToggleDelegate();
        boolean animate = true;
        if (mArrowDrawable == null) {
            mArrowDrawable = new DrawerArrowDrawable(
                    delegate.getActionBarThemedContext());
            delegate.setActionBarUpIndicator(mArrowDrawable, 0);
            // We're setting the initial state, so skip the animation
            animate = false;
        }
        float endValue = showAsDrawerIndicator ? 0f : 1f;
        if (animate) {
            float startValue = mArrowDrawable.getProgress();
            if (mAnimator != null) {
                mAnimator.cancel();
            }
            mAnimator = ObjectAnimator.ofFloat(mArrowDrawable, "progress",
                    startValue, endValue);
            mAnimator.start();
        } else {
            mArrowDrawable.setProgress(endValue);
        }
    }
}
