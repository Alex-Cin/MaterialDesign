package org.alex.behavior.fadeaction;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import org.alex.mdlibrary.R;
import org.alex.util.LogTrack;

import java.lang.ref.WeakReference;

/**
 * 作者：Alex
 * 时间：2017/2/26 15:14
 * 简述：
 */
@SuppressWarnings("all")
public class FadeActionBehavior extends CoordinatorLayout.Behavior<View> {
    /**
     * 依赖 控件
     */
    private WeakReference<View> dependentViewWeakReference;
    private WeakReference<View> childViewWeakReference;
    private int dependentViewId;
    private int leftMargin, topMargin, rightMargin, bottomMargin;
    private int lastProgress;
    private int progress;
    /**
     * 从 上 往下 滑动   为负
     */
    private int dyConsumedOffset;
    private int dyOffset;
    /**
     * RecyclerView  向下移动的距离
     */
    private int dyUnconsumedOffset;
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private int floatType = 0;
    /**
     * 处于 展示状态
     */
    private static final int FLOAT_TYPE_SHOWN = 0;
    /**
     * 处于 变化中
     */
    private static final int FLOAT_TYPE_ANIMATING = 1;
    /**
     * 处于 隐藏状态
     */
    private static final int FLOAT_TYPE_HIDDEN = 2;
    private static final int showDuration = 500;
    private static final int hideDuration = 300;
    private AnimatorSet hideAnimatorSet;
    private AnimatorSet showAnimatorSet;

    /**
     * Default constructor for instantiating Behaviors.
     */
    public FadeActionBehavior() {
        super();
        initBehavior(null, null);
    }

    /**
     * 如果 在xml 中使用   app:layout_behavior="xxx.xxx.xxx.MyBehavior" 必须要复写 这个方法
     * 如果需要在构造函数中 传递参数，只能给 使用 这个 behavior 添加
     */
    public FadeActionBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBehavior(context, attrs);
    }

    private void initBehavior(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FadeActionBehavior);
        dependentViewId = typedArray.getResourceId(R.styleable.FadeActionBehavior_fb_dependentViewId, -1);
        leftMargin = typedArray.getDimensionPixelOffset(R.styleable.FadeActionBehavior_fb_marginLeft, 0);
        topMargin = typedArray.getDimensionPixelOffset(R.styleable.FadeActionBehavior_fb_marginTop, 0);
        rightMargin = typedArray.getDimensionPixelOffset(R.styleable.FadeActionBehavior_fb_marginRight, 0);
        bottomMargin = typedArray.getDimensionPixelOffset(R.styleable.FadeActionBehavior_fb_marginBottom, 0);
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
        childViewWeakReference = (childViewWeakReference == null) ? new WeakReference<>(dependency) : childViewWeakReference;
        if (isHeadView) {
            dependentViewWeakReference = (dependentViewWeakReference == null) ? new WeakReference<>(dependency) : dependentViewWeakReference;
        }
        return isHeadView;
    }

    /**
     * 子控件 需要发生改变  返回 true
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        offsetChildView(parent, child, dependency);
        autoShowOrHide(parent, child);
        return true;
    }

    /**
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx
     * @param dy                垂直方向嵌套滑动的子View想要变化的距离，向上滑动 > 0
     * @param consumed
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        dyOffset += dy;
        //LogTrack.w(ViewCompat.getY(target) + "  " + dyOffset);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    @Nullable
    private View getDependentView() {
        return (dependentViewWeakReference == null) ? null : dependentViewWeakReference.get();
    }

    /**
     * @param coordinatorLayout
     * @param child             绑定 该  behavior 的 控件
     * @param target            可以 滑动的 控件（ScrollView Recyclerview）
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    /**
     * @param coordinatorLayout
     * @param child             绑定 该  behavior 的 控件
     * @param target            可以 滑动的 控件（ScrollView Recyclerview）
     * @param velocityX
     * @param velocityY
     * @param consumed          父View是否消耗了fling
     * @return
     */
    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    /**
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed
     * @param dyConsumed        垂直方向嵌套滑动的子View滑动的距离(消耗的距离)  从 上 往下 滑动   为负
     * @param dxUnconsumed
     * @param dyUnconsumed      垂直方向嵌套滑动的子View未滑动的距离(未消耗的距离)
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        dyConsumedOffset += dyConsumed;
        dyUnconsumedOffset += dyUnconsumed;
        //LogTrack.w(ViewCompat.getY(target) + "  " + target.getTop() + "  " + dyConsumed + "  " + dyConsumedOffset + "  " + dyUnconsumed + "  " + dyUnconsumedOffset);
    }

    /**
     * @param coordinatorLayout
     * @param child             绑定 该  behavior 的 控件
     * @param target            是 可以滑动的   控件
     */
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        // LogTrack.w(ViewCompat.getY(target) + "  " + dyOffset + "  " + dyConsumedOffset);
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    private void autoShowOrHide(final CoordinatorLayout parent, final View child) {
        View dependentView = getDependentView();
        int dependentHeight = dependentView.getMeasuredHeight();
        int offsetY = -dependentView.getTop();
        progress = 100 * offsetY / dependentHeight;
        LogTrack.i(progress);
        if (lastProgress > progress && progress < 50) {
            LogTrack.i("↓  往下拉   展示");
            if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
                hideAnimatorSet.cancel();
            }
            show(child);
        } else if (lastProgress < progress && progress > 50) {
            if (showAnimatorSet != null && showAnimatorSet.isRunning()) {
                showAnimatorSet.cancel();
            }
            LogTrack.i("↑  往上推    隐藏");
            hide(child);
        }

        lastProgress = progress;
    }

    private void offsetChildView(final CoordinatorLayout parent, final View child, final View dependentView) {
        int dependentHeight = dependentView.getMeasuredHeight();
        int offsetY = -dependentView.getTop();
        ViewCompat.setTranslationX(child, parent.getMeasuredWidth() - child.getMeasuredWidth() - rightMargin);
        ViewCompat.setTranslationY(child, dependentHeight - child.getMeasuredHeight() / 2 - offsetY);
    }

    private View getChildView() {
        return childViewWeakReference.get();
    }

    //隐藏时的动画
    private void hide(final View view) {
        if ((hideAnimatorSet != null && hideAnimatorSet.isRunning()) || view.getVisibility() == View.GONE) {
            return;
        }
        //组合动画
        hideAnimatorSet = new AnimatorSet();
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 1F, 0F);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 1F, 0F);
        hideAnimatorSet.setDuration(hideDuration);
        hideAnimatorSet.setInterpolator(INTERPOLATOR);
        hideAnimatorSet.play(scaleXAnim).with(scaleYAnim);
        hideAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                floatType = FLOAT_TYPE_ANIMATING;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
                //LogTrack.w("--");
                floatType = FLOAT_TYPE_HIDDEN;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                //show(view);
                //LogTrack.w("--");
                view.setScaleX(1F);
                view.setScaleY(1F);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        hideAnimatorSet.start();
    }

    //显示时的动画
    private void show(final View view) {
        if ((showAnimatorSet != null && showAnimatorSet.isRunning()) || view.getVisibility() == View.VISIBLE) {
            LogTrack.i(view.getVisibility()+"  "+View.VISIBLE);
            return;
        }
        LogTrack.i("--");
        //组合动画
        showAnimatorSet = new AnimatorSet();
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 0F, 1F);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 0F, 1F);
        showAnimatorSet.setDuration(showDuration);
        showAnimatorSet.setInterpolator(INTERPOLATOR);
        showAnimatorSet.play(scaleXAnim).with(scaleYAnim);
        showAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
                floatType = FLOAT_TYPE_ANIMATING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                floatType = FLOAT_TYPE_SHOWN;
                //LogTrack.e("--");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //LogTrack.e("--");
                //hide(view);
                view.setScaleX(0F);
                view.setScaleY(0F);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        showAnimatorSet.start();
    }
}

