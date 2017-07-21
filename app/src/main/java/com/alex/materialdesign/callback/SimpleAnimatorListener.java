package com.alex.materialdesign.callback;

import android.animation.Animator;

/**
 * 作者：Alex
 * 时间：2017/2/28 23:05
 * 简述：
 */
public class SimpleAnimatorListener implements Animator.AnimatorListener {
    /**
     * <p>Notifies the start of the animation.</p>
     *
     * @param animation The started animation.
     */
    @Override
    public void onAnimationStart(Animator animation) {

    }

    /**
     * <p>Notifies the end of the animation. This callback is not invoked
     * for animations with repeat count set to INFINITE.</p>
     *
     * @param animation The animation which reached its end.
     */
    @Override
    public void onAnimationEnd(Animator animation) {

    }

    /**
     * <p>Notifies the cancellation of the animation. This callback is not invoked
     * for animations with repeat count set to INFINITE.</p>
     *
     * @param animation The animation which was canceled.
     */
    @Override
    public void onAnimationCancel(Animator animation) {

    }

    /**
     * <p>Notifies the repetition of the animation.</p>
     *
     * @param animation The animation which was repeated.
     */
    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
