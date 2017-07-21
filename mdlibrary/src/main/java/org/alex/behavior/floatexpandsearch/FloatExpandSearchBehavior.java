package org.alex.behavior.floatexpandsearch;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import org.alex.mdlibrary.R;
import org.alex.util.LogTrack;

import java.lang.ref.WeakReference;

/**
 * 作者：Alex
 * 时间：2017/2/27 22:30
 * 简述：
 */
@SuppressWarnings({"JavaDoc", "unused", "DanglingJavadoc"})
public class FloatExpandSearchBehavior extends CoordinatorLayout.Behavior<View> implements AppBarLayout.OnOffsetChangedListener {
    private int dependentViewId;
    private boolean isBindAppbarListener;
    /**
     * 依赖 控件
     */
    private WeakReference<View> dependentViewWeakReference;
    private WeakReference<View> childViewWeakReference;
    private int childWidth;
    private float childY;
    private int targetY;
    private int offsetY;
    private int dependencyHeight;
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private int floatType = 0;

    private static final int showDuration = 500;
    private static final int hideDuration = 300;
    private float lastProgress;
    private float progress;

    private float childTopMargin;
    private IFloatExpandSearch floatExpandSearch;

    public FloatExpandSearchBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBehavior(context, attrs);
    }

    private void initBehavior(Context context, AttributeSet attrs) {
        isBindAppbarListener = false;
        childTopMargin = -1;
        dependencyHeight = -1;
        lastProgress = -1F;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FloatExpandSearchBehavior);
        dependentViewId = typedArray.getResourceId(R.styleable.FloatExpandSearchBehavior_fesb_dependentViewId, -1);
        targetY = typedArray.getDimensionPixelOffset(R.styleable.FloatExpandSearchBehavior_fesb_targetY, 0);
        offsetY = typedArray.getDimensionPixelOffset(R.styleable.FloatExpandSearchBehavior_fesb_offsetY, 0);
        typedArray.recycle();
    }


    /**
     * 我们将 该 Behavior 绑定给一个控件，
     * 在 CoordinateLayout 中，与所绑定的控件 同一 目录级别的 控件中，  确定 所依赖的
     * 确定所提供的子视图是否有另一个特定的同级视图作为布局从属，确定你依赖哪些View。
     * 返回 true 即可确立依赖关系
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        boolean isHeadView = dependency.getId() == dependentViewId;
        if (!isBindAppbarListener && dependency instanceof AppBarLayout) {
            isBindAppbarListener = true;
            AppBarLayout appBarLayout = (AppBarLayout) dependency;
            appBarLayout.addOnOffsetChangedListener(this);
        }
        floatExpandSearch = (IFloatExpandSearch) child;
        childViewWeakReference = (childViewWeakReference == null) ? new WeakReference<>(dependency) : childViewWeakReference;
        if (isHeadView) {
            dependentViewWeakReference = (dependentViewWeakReference == null) ? new WeakReference<>(dependency) : dependentViewWeakReference;
        }
        return isHeadView;
    }

    CoordinatorLayout coordinatorLayout;
    View child;

    /**
     * 子控件 需要发生改变  返回 true
     *
     * @param coordinatorLayout
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View child, View dependency) {
        this.coordinatorLayout = (this.coordinatorLayout == null) ? coordinatorLayout : this.coordinatorLayout;
        this.child = (this.child == null) ? child : this.child;
        return true;
    }

    private void autoShowOrHide(boolean isScrollStop) {
        if (floatExpandSearch == null) {
            return;
        }
        lastProgress = (lastProgress < 0) ? progress : lastProgress;
        int floatEnum = floatExpandSearch.getFloatEnum();
        LogTrack.i(progress + "  " + (progress == 1F));
        if (progress == 1F) {
            floatExpandSearch.startUnFolding(-1);
        } else if (lastProgress > progress && progress < 0.5F) {
            //LogTrack.i("往下拉    隐藏");
            if (IFloatExpandSearch.FloatEnum.FOLDED_2_UNFOLDED == floatEnum) {
                /*由  展开状态 ->  折叠状态  进行中*/
                floatExpandSearch.cancelUnFolding();
                startFolding();
            } else if (IFloatExpandSearch.FloatEnum.UNFOLDED == floatEnum) {
                /*处于 折叠的状态*/
                startFolding();
            }
        } else if (lastProgress < progress && progress >= 0.5F && progress < 1F) {
            //LogTrack.i("往上推   展示");
            if (IFloatExpandSearch.FloatEnum.UNFOLDED_2_FOLDED == floatEnum) {
                /*由  展开状态 ->  折叠状态  进行中*/
                floatExpandSearch.cancelFolding();
                startUnFolding();
            } else if (IFloatExpandSearch.FloatEnum.FOLDED == floatEnum) {
                /*处于 折叠的状态*/
                startUnFolding();
            }
        } else if (progress == 0F) {
            floatExpandSearch.startFolding(-1);
        }
        lastProgress = progress;
    }

    private void offsetChildView(int verticalOffset) {
        if (getDependentView() == null || child == null) {
            return;
        }
        dependencyHeight = (dependencyHeight < 0) ? getDependentView().getMeasuredHeight() : dependencyHeight;
        childWidth = (childWidth < 0) ? child.getMeasuredWidth() : childWidth;
        childY = (childY < 0) ? child.getY() : childY;
        /**
         * progress 为 0  的 话， dependentView 被完全推上去了
         * progress 为 1  的 话， dependentView 被完全推拉下来了
         * */
        progress = (dependencyHeight - verticalOffset - offsetY) * 100 / (dependencyHeight - offsetY);
        progress *= 0.01F;

        CoordinatorLayout.LayoutParams childLayoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        childTopMargin = (childTopMargin < 0) ? childLayoutParams.topMargin : childTopMargin;
        childLayoutParams.topMargin = (int) (childTopMargin * progress);
        childLayoutParams.topMargin = (childLayoutParams.topMargin >= targetY) ? childLayoutParams.topMargin : targetY;
        child.setLayoutParams(childLayoutParams);
    }

    private View getChildView() {
        return childViewWeakReference.get();
    }

    /**
     * 隐藏时的动画
     */
    private void startFolding() {
        floatExpandSearch.startFolding();
    }

    /**
     * 显示时的动画
     */
    private void startUnFolding() {
        floatExpandSearch.startUnFolding();
    }

    @Nullable
    private View getDependentView() {
        return (dependentViewWeakReference == null) ? null : dependentViewWeakReference.get();
    }

    private int getDependentHeight() {
        View dependentView = getDependentView();
        return dependentView == null ? 0 : dependentView.getMeasuredHeight();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //LogTrack.i(verticalOffset);
        if (coordinatorLayout == null || child == null) {
            return;
        }
        offsetChildView(-verticalOffset);
        autoShowOrHide(true);

    }
}
