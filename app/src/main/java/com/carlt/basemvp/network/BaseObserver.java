package com.carlt.basemvp.network;

import android.net.ParseException;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.basemvp.base.BaseView;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/3/5 13:54
 */
public abstract class BaseObserver<T> extends DisposableObserver<T> {
    protected           BaseView view;
    /**
     * 解析数据失败
     */
    public static final int      PARSE_ERROR     = 1001;
    /**
     * 网络问题
     */
    public static final int      BAD_NETWORK     = 1002;
    /**
     * 连接错误
     */
    public static final int      CONNECT_ERROR   = 1003;
    /**
     * 连接超时
     */
    public static final int      CONNECT_TIMEOUT = 1004;
    private             Field    mErr;
    private             Field    mCode;


    public BaseObserver(BaseView view) {
        this.view = view;
    }

    @Override
    protected void onStart() {
        if (view != null) {
            view.showLoading();
        }
    }

    private Class<?> analysisClassInfo(Object obj) {
        //可以得到包含原始类型，参数化类型，类型变量，基本类型
        Type type = obj.getClass().getGenericSuperclass();
        Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
        return (Class<?>) typeArguments[0];

    }

    @Override
    public void onNext(T result) {

        //        Log.e("---->", result.toString());

        try {
            mErr = result.getClass().getDeclaredField("err");
            mErr.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (mErr == null)
            try {
                mCode = result.getClass().getDeclaredField("code");
                mCode.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        LogUtils.e("mErr----" + mErr);
        LogUtils.e("mCode----" + mCode);
        try {
            if (mErr == null && mCode == null) {
                LogUtils.e(result);
                onSuccess(result);
            } else {
                //            if (view!=null) {
                //                view.onErrorCode(result);
                //            }
                if (mCode != null) {
                    int code = mCode.getInt(result);
                    if (code == 0) {
                        onSuccess(result);
                    } else {
                        view.onErrorCode(code);
                        Field msgField = result.getClass().getDeclaredField("msg");
                        String msg = (String) msgField.get(result);
                        onError(msg);
                        view.showError(msg);
                    }
                    LogUtils.e(code);
                }
                if (mErr != null) {
                    BaseErr err = (BaseErr) mErr.get(result);
                    if (err == null) {
                        onSuccess(result);
                    } else {
                        LogUtils.e(err.code);
                        onError(err.msg);
                        view.onErrorCode(err.code);
                        view.showError(err.msg);
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
        //        try {
        //            BaseModel model = (User) result;
        //            if (model.code == 0) {
        //                onSuccess(result);
        //            } else {
        //                if (view != null) {
        //                    view.onErrorCode(model.code);
        //                }
        //            }
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //            onError(e.toString());
        //        }


    }

    @Override
    public void onError(Throwable e) {
        if (view != null) {
            view.hideLoading();
        }
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR);
        } else {
            if (e != null) {
                onError(e.toString());
            } else {
                onError("未知错误");
            }
        }

    }

    private void onException(int unknownError) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError("连接错误");
                break;

            case CONNECT_TIMEOUT:
                onError("连接超时");
                break;

            case BAD_NETWORK:
                onError("网络问题");
                break;

            case PARSE_ERROR:
                onError("解析数据失败");
                break;

            default:
                break;
        }
    }


    @Override
    public void onComplete() {
        if (view != null) {
            view.hideLoading();
        }

    }

    public abstract void onSuccess(T result);

    public abstract void onError(String msg);
}
