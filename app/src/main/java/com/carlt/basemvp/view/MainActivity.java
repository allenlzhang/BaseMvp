package com.carlt.basemvp.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.carlt.basemvp.R;
import com.carlt.basemvp.base.BaseActivity;
import com.carlt.basemvp.constant.ConstantKey;
import com.carlt.basemvp.manager.ActivityControl;
import com.carlt.basemvp.presenter.LoginPresenter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MainActivity extends BaseActivity<LoginPresenter> implements ILoginView {

    //    private LoginPresenter mLoginPresenter;
    private EditText mEtName;
    private EditText mEtPwd;
    private EditText etRemotePwd;

    @Override
    public LoginPresenter getPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        Button btnLogin = findViewById(R.id.btnLogin);
        mEtName = findViewById(R.id.etName);
        mEtPwd = findViewById(R.id.etPwd);
        etRemotePwd = findViewById(R.id.etRemotePwd);
        Activity activity = ActivityControl.getManager().currentActivity();
        LogUtils.e("currentActivity===>" + activity);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                String pwd = mEtPwd.getText().toString();
                HashMap<String, Object> map = new HashMap<>();

                map.put("version", 1);
                map.put("moveDeviceName", "vivo");
                map.put("loginModel", "x5");
                map.put("loginSoftType", "Android");
                map.put("moveDeviceid", "sdfsadfa");


                map.put("mobile", name);
                map.put("password", md5(pwd));
                map.put("loginType", 1);
                map.put("pwdReally", pwd);
                mPresenter.login(map);


                //                Observable<UserInfo> observable = apiServer.commonLogin(map)
                //
                //                        .filter(new Predicate<User>() {
                //                            @Override
                //                            public boolean test(User user) throws Exception {
                //                                if (user.err != null) {
                //                                    LogUtils.e(user.err.msg);
                //                                }
                //                                return user.err == null;
                //                            }
                //                        })
                //                        .flatMap(new Function<User, Observable<UserInfo>>() {
                //
                //                            @Override
                //                            public Observable<UserInfo> apply(User user) throws Exception {
                //                                HashMap<String, Object> map = new HashMap<>();
                //                                map.put("token", user.token);
                //                                return apiServer.getUserInfo(map);
                //                            }
                //                        });
                //                addDisposable(observable, new BaseMvcObserver<UserInfo>() {
                //
                //                    @Override
                //                    public void onSuccess(UserInfo result) {
                //                        LogUtils.e(result.toString());
                //                    }
                //
                //                    @Override
                //                    public void onError(String msg) {
                //                        LogUtils.e(msg);
                //                    }
                //                });
                //
            }
        });

    }

    public void setRemote(View view) {
        String pwd = etRemotePwd.getText().toString();
        HashMap<String, Object> params = new HashMap<>();
        //                params.put(GlobalKey.USER_TOKEN, token);
        params.put("remotePwd", md5(pwd));
        params.put("token", SPUtils.getInstance().getString(ConstantKey.TOKEN));
        mPresenter.setRemotePwd(params);
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }




    @Override
    public void loginSuccess(String msg) {
        showToast(msg);
    }


}
