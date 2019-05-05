package com.carlt.basemvp.base;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/3/4 15:14
 */
public interface BaseView {
    void showToast(String msg);

    /**
     * 显示dialog
     */
    void showLoading();

    /**
     * 隐藏 dialog
     */

    void hideLoading();

    /**
     * 显示错误信息
     * @param msg
     */
    void showError(String msg);

    /**
     * 错误码
     */
    void onErrorCode(int model);

}
