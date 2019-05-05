package com.carlt.basemvp.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.carlt.basemvp.base.BasePresenter;
import com.carlt.basemvp.constant.ConstantKey;
import com.carlt.basemvp.network.BaseErr;
import com.carlt.basemvp.network.BaseObserver;
import com.carlt.basemvp.network.User;
import com.carlt.basemvp.view.ILoginView;

import java.util.HashMap;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/3/4 15:20
 */
public class LoginPresenter extends BasePresenter<ILoginView> {

    public LoginPresenter(ILoginView iLoginView) {
        super(iLoginView);
    }

    @SuppressLint("CheckResult")
    public void login(HashMap<String, Object> map) {

        addDisposable(apiServer.commonLogin(map), new BaseObserver<User>(baseView) {
            @Override
            public void onSuccess(User o) {
                Log.e("---->", o.toString());
                SPUtils.getInstance().put(ConstantKey.TOKEN, o.token);
                baseView.loginSuccess(o.token);
            }

            @Override
            public void onError(String msg) {
                Log.e("---->", msg);
                baseView.showError(msg);
            }
        });
    }

    public void setRemotePwd(HashMap<String, Object> map) {
        addDisposable(apiServer.SetRemotePassword(map), new BaseObserver<BaseErr>(baseView) {
            @Override
            public void onSuccess(BaseErr result) {
                LogUtils.e(result);
            }

            @Override
            public void onError(String msg) {
                LogUtils.e(msg);
            }
        });
    }
}
