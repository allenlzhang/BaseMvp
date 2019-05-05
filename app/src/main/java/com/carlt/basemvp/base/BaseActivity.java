package com.carlt.basemvp.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.carlt.basemvp.manager.ActivityControl;
import com.carlt.basemvp.network.ApiRetrofit;
import com.carlt.basemvp.network.ApiService;
import com.carlt.basemvp.network.BaseMvcObserver;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/3/4 15:54
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    public abstract P getPresenter();

    public abstract int initLayout();

    public abstract void init();

    protected ApiService          apiServer = ApiRetrofit.getInstance().getService(ApiService.class);
    private   CompositeDisposable compositeDisposable;
    private   ProgressDialog      dialog;
    protected P                   mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityControl.getManager().addActivity(this);
        mPresenter = getPresenter();

        setContentView(initLayout());
        init();

    }

    private void closeLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    private void showLoadingDialog() {

        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

    @Override
    public void showError(String msg) {
        showToast(msg);
    }

    @Override
    public void onErrorCode(int code) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachView();
        removeDisposable();
    }

    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    public void addDisposable(Observable<?> observable, BaseMvcObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));


    }

    public void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }

}
