package org.alex.behavior.floatexpandsearch;

/**
 * 作者：Alex
 * 时间：2017/2/27 23:37
 * 简述：
 */
public interface IFloatExpandSearch {
    /**
     * 开始 展开
     */
    void startUnFolding(int duration);

    /**
     * 开始 展开
     */
    void startUnFolding();

    /**
     * 开始 折叠
     */
    void startFolding(int duration);

    /**
     * 开始 折叠
     */
    void startFolding();

    /**
     * 取消 折叠 动画
     */
    void cancelFolding();

    /**
     * 取消 展开 动画
     */
    void cancelUnFolding();

    /**
     * 获取 搜索框的 状态
     *
     * @return
     */
    int getFloatEnum();

    interface FloatEnum {
        /**
         * 处于 展开状态
         */
        int UNFOLDED = 0;
        /**
         * 由  展开状态 ->  折叠状态  进行中
         */
        int UNFOLDED_2_FOLDED = 1;
        /**
         * 由  折叠状态 ->   展开状态   进行中
         */
        int FOLDED_2_UNFOLDED = 2;
        /**
         * 处于 折叠的状态
         */
        int FOLDED = 3;
    }
}
