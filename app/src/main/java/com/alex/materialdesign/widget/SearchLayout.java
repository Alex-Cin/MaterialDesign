package com.alex.materialdesign.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alex.materialdesign.R;
import com.alex.materialdesign.callback.SimpleAnimatorListener;

import org.alex.behavior.floatexpandsearch.IFloatExpandSearch;
import org.alex.util.LogTrack;

/**
 * 作者：Alex
 * 时间：2017/2/27 19:55
 * 简述：
 */
@SuppressWarnings({"SuspiciousNameCombination", "JavaDoc", "FieldCanBeLocal", "StatementWithEmptyBody", "unused"})
public class SearchLayout extends RelativeLayout implements IFloatExpandSearch {
    private View runnerView;
    private View logoView;
    private int runnerViewWidth;
    private int runnerViewHeight;
    /**
     * 折叠  时间
     */
    private int foldingDuration;
    /**
     * 展开  时间
     */
    private int unFoldingDuration;
    private ObjectAnimator foldingAnimator;
    private ObjectAnimator unFoldingAnimator;
    private int floatEnum;
    private GradientDrawable runnerDrawable;
    /**
     * 折叠 状态的 颜色
     */
    private int foldingAlpha;
    /**
     * 展开 状态的 颜色
     */
    private int unFoldingAlpha;

    public SearchLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public SearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        foldingAlpha = 255;
        unFoldingAlpha = 200;
        foldingDuration = 500;
        unFoldingDuration = 500;
        runnerViewWidth = -1;
        runnerViewHeight = -1;
        floatEnum = FloatEnum.FOLDED;
        View childView = LayoutInflater.from(context).inflate(R.layout.layout_search, this, false);
        runnerView = childView.findViewById(R.id.search_runner);
        runnerDrawable = (GradientDrawable) runnerView.getBackground();
        runnerDrawable.setAlpha(255);
        logoView = childView.findViewById(R.id.search_logo);
        addView(childView);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 开始 展开
     *
     * @param duration
     */
    @Override
    public void startUnFolding(int duration) {
        if (duration <= 0) {
            cancelFolding();
            LogTrack.w("完全 展开");
            floatEnum = FloatEnum.UNFOLDED;
            setRunnerViewWidth(runnerViewWidth);
            return;
        }
        unFoldingAnimator = (unFoldingAnimator == null) ? ObjectAnimator.ofFloat(this, "runnerViewWidth", runnerViewHeight, runnerViewWidth) : unFoldingAnimator;
        unFoldingAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                floatEnum = FloatEnum.FOLDED_2_UNFOLDED;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                floatEnum = FloatEnum.UNFOLDED;
            }
        });
        unFoldingAnimator.setDuration(duration);
        if (!unFoldingAnimator.isStarted()) {
            unFoldingAnimator.start();
        }
    }

    /**
     * 开始 展开
     */
    @Override
    public void startUnFolding() {
        startUnFolding(unFoldingDuration);
    }

    /**
     * 开始 折叠
     *
     * @param duration
     */
    @Override
    public void startFolding(int duration) {
        if (duration <= 0) {
            LogTrack.w("完全折叠");
            cancelUnFolding();
            floatEnum = FloatEnum.FOLDED;
            setRunnerViewWidth(runnerViewHeight);
            return;
        }
        foldingAnimator = (foldingAnimator == null) ? ObjectAnimator.ofFloat(this, "runnerViewWidth", runnerViewWidth, runnerViewHeight) : foldingAnimator;
        foldingAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                floatEnum = FloatEnum.UNFOLDED_2_FOLDED;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                floatEnum = FloatEnum.FOLDED;
            }
        });
        foldingAnimator.setDuration(duration);
        if (!foldingAnimator.isStarted()) {
            foldingAnimator.start();
        }
    }

    /**
     * 开始 折叠
     */
    @Override
    public void startFolding() {
        startFolding(foldingDuration);
    }

    /**
     * 取消 折叠 动画
     */
    @Override
    public void cancelFolding() {
        if (foldingAnimator != null) {
            foldingAnimator.cancel();
        }
    }

    /**
     * 取消 展开 动画
     */
    @Override
    public void cancelUnFolding() {
        if (foldingAnimator != null) {
            foldingAnimator.cancel();
        }
    }

    /**
     * 获取 搜索框的 状态
     *
     * @return
     */
    @Override
    public int getFloatEnum() {
        return floatEnum;
    }

    private void setRunnerViewWidth(float width) {
        int alpha = (int) (unFoldingAlpha + (foldingAlpha - unFoldingAlpha) * width / (float) runnerViewWidth);
        runnerDrawable.setAlpha(alpha);
        final ViewGroup viewParent = (ViewGroup) runnerView.getParent();
        final ViewGroup.LayoutParams layoutParams = runnerView.getLayoutParams();
        if (layoutParams.width == (int) width) {
            return;
        }
        layoutParams.height = runnerViewHeight;
        layoutParams.width = (int) width;
        viewParent.updateViewLayout(runnerView, layoutParams);

        if (width == runnerViewWidth || width == runnerViewHeight) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewParent.updateViewLayout(runnerView, layoutParams);
                }
            }, 2);
        }

        LogTrack.i("width = " + layoutParams.width + " height  = " + layoutParams.height + "  " + runnerView.getWidth() + "  " + runnerView.getHeight() + "  " + alpha);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureWidth(0, widthMeasureSpec);
        int measureHeight = measureHeight(0, heightMeasureSpec);
        if (runnerViewHeight >= 0 || runnerViewWidth >= 0) {
            return;
        }

        for (int i = 0; i < getChildCount() && i < 1; i++) {
            View childView = getChildAt(i);
            ViewGroup.LayoutParams childViewLayoutParams = childView.getLayoutParams();
            int widthSpec = 0;
            int heightSpec = 0;
            if (childViewLayoutParams.width > 0) {
                widthSpec = MeasureSpec.makeMeasureSpec(childViewLayoutParams.width, MeasureSpec.EXACTLY);
            } else if (childViewLayoutParams.width == -1) {
                widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
            } else if (childViewLayoutParams.width == -2) {
                widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.AT_MOST);
            }

            if (childViewLayoutParams.height > 0) {
                heightSpec = MeasureSpec.makeMeasureSpec(childViewLayoutParams.height, MeasureSpec.EXACTLY);
            } else if (childViewLayoutParams.height == -1) {
                heightSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY);
            } else if (childViewLayoutParams.height == -2) {
                heightSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.AT_MOST);
            }
            childView.measure(widthSpec, heightSpec);
            runnerViewWidth = childView.getMeasuredWidth();
            runnerViewHeight = childView.getMeasuredHeight();
        }
    }

    private int measureWidth(int size, int widthMeasureSpec) {
        int result = size;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);// 得到尺寸
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return result;
    }


    private int measureHeight(int size, int heightMeasureSpec) {
        int result = size;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return result;
    }


}
