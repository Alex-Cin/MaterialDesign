package org.alex.behavior.scrollportrait;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import org.alex.mdlibrary.R;
import org.alex.util.LogTrack;

import java.lang.ref.WeakReference;

/**
 * 作者：Alex
 * 时间：2017/2/26 19:01
 * 简述：
 */
@SuppressWarnings("DanglingJavadoc")
public class ScrollPortraitBehavior extends CoordinatorLayout.Behavior<View> implements AppBarLayout.OnOffsetChangedListener {
    private int dependentViewId;
    private boolean isBindAppbarListener;
    /**
     * 依赖 控件
     */
    private WeakReference<View> dependentViewWeakReference;
    private WeakReference<View> childViewWeakReference;
    private int dependencyHeight;
    private int childHeight;
    private int childWidth;
    private float childX;
    private float childY;
    private int targetX;
    private int offsetY;
    private int targetY;
    private int targetWidth;
    private float childTopMargin;
    /**
     * Appbar  被拉出来 拉下来 的进度， 全拉出来时  为  100
     */
    private float progress;

    /**
     * Default constructor for instantiating Behaviors.
     */
    public ScrollPortraitBehavior() {
        super();
        initBehavior(null, null);
    }

    /**
     * 如果 在xml 中使用   app:layout_behavior="xxx.xxx.xxx.MyBehavior" 必须要复写 这个方法
     * 如果需要在构造函数中 传递参数，只能给 使用 这个 behavior 添加
     */
    public ScrollPortraitBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBehavior(context, attrs);
    }

    private void initBehavior(Context context, AttributeSet attrs) {
        isBindAppbarListener = false;
        dependencyHeight = -1;
        childTopMargin = -1;
        childWidth = -1;
        childHeight = -1;
        childX = -1;
        childY = -1;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollPortraitBehavior);
        dependentViewId = typedArray.getResourceId(R.styleable.ScrollPortraitBehavior_sb_dependentViewId, -1);
        targetX = typedArray.getDimensionPixelOffset(R.styleable.ScrollPortraitBehavior_sb_targetX, 0);
        targetWidth = typedArray.getDimensionPixelOffset(R.styleable.ScrollPortraitBehavior_sb_targetWidth, 0);
        offsetY = typedArray.getDimensionPixelOffset(R.styleable.ScrollPortraitBehavior_sb_offsetY, 0);
        targetY = typedArray.getDimensionPixelOffset(R.styleable.ScrollPortraitBehavior_sb_targetY, 0);
        typedArray.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        boolean isHeadView = dependency.getId() == dependentViewId;

        if (!isBindAppbarListener && dependency instanceof AppBarLayout) {
            isBindAppbarListener = true;
            AppBarLayout appBarLayout = (AppBarLayout) dependency;
            appBarLayout.addOnOffsetChangedListener(this);
        }
        ViewCompat.setPivotX(child, 0);
        ViewCompat.setPivotY(child, 0);
        childViewWeakReference = (childViewWeakReference == null) ? new WeakReference<>(dependency) : childViewWeakReference;
        if (isHeadView) {
            dependentViewWeakReference = (dependentViewWeakReference == null) ? new WeakReference<>(dependency) : dependentViewWeakReference;
        }
        return isHeadView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View child, View dependency) {
        this.coordinatorLayout = (this.coordinatorLayout == null) ? coordinatorLayout : this.coordinatorLayout;
        this.child = (this.child == null) ? child : this.child;
        return true;
    }

    View child;
    CoordinatorLayout coordinatorLayout;

    private View getDependentView() {
        return (dependentViewWeakReference == null) ? null : dependentViewWeakReference.get();
    }


    private float lastProgress = -1F;
    private float lastTopMargin = -1F;

    private void offsetChildView(int verticalOffset) {
        if (getDependentView() == null || coordinatorLayout == null || child == null) {
            return;
        }
        dependencyHeight = (dependencyHeight < 0) ? getDependentView().getMeasuredHeight() : dependencyHeight;
        childHeight = (childHeight < 0) ? child.getMeasuredHeight() : childHeight;
        childWidth = (childWidth < 0) ? child.getMeasuredWidth() : childWidth;
        childX = (childX < 0) ? child.getX() : childX;
        childY = (childY < 0) ? child.getY() : childY;
        progress = (dependencyHeight - verticalOffset - offsetY) * 100 / (dependencyHeight - offsetY);
        progress *= 0.01F;
        final CoordinatorLayout.LayoutParams childLayoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        childTopMargin = (childTopMargin < 0) ? childLayoutParams.topMargin : childTopMargin;

        /**
         * 最终大小  ÷  原始 大小
         */
        float scale = ((float) targetWidth / (float) childWidth);
        /**
         * 变化的  比例
         */
        float scaleProgress = scale + (1 - scale) * progress;

        if (progress == 0 && scaleProgress == scale && lastTopMargin == 0 && child.getY() == targetY) {
            return;
        }
        LogTrack.i(childLayoutParams.topMargin + "  " + childY + "  " + child.getY() + "  " + targetY);
        lastTopMargin = childLayoutParams.topMargin;
        lastProgress = progress;
        //LogTrack.w(progress + "  " + scaleProgress + "  " + verticalOffset + "  " + scale + "  " + childLayoutParams.topMargin);
        ViewCompat.setScaleY(child, scaleProgress);
        ViewCompat.setScaleX(child, scaleProgress);

        float newY = targetY + (childY - targetY) * (progress);
        float newX = targetX + (childX - targetX) * (progress);
        ViewCompat.setX(child, newX);
        ViewCompat.setY(child, newY);
        childLayoutParams.topMargin = (int) (childTopMargin * progress);

        child.setLayoutParams(childLayoutParams);
        if (progress == 0) {
            ViewCompat.postOnAnimationDelayed(child, new Runnable() {
                @Override
                public void run() {
                    childLayoutParams.topMargin = 0;
                    child.setLayoutParams(childLayoutParams);
                }
            }, 2);

        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (coordinatorLayout == null || child == null) {
            return;
        }
        offsetChildView(-verticalOffset);
    }
}
