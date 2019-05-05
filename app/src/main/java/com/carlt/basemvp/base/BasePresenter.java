package com.carlt.basemvp.base;


import com.carlt.basemvp.network.ApiRetrofit;
import com.carlt.basemvp.network.ApiService;
import com.carlt.basemvp.network.BaseObserver;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/3/4 15:14
 */
public class BasePresenter<V extends BaseView> {

    private CompositeDisposable compositeDisposable;


    public V baseView;

    protected ApiService apiServer = ApiRetrofit.getInstance().getService(ApiService.class);

    public BasePresenter(V v) {

        this.baseView = v;
    }

    /**
     * 解除绑定
     */
    public void detachView() {
        baseView = null;
        removeDisposable();
    }

    /**
     * 返回 view
     * @return
     */
    public V getBaseView() {
        return baseView;
    }


    public void addDisposable(Observable<?> observable, BaseObserver observer) {
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
        }
    }

}
