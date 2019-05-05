package com.carlt.basemvp.network;


import com.carlt.basemvp.model.UserInfo;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/3/5 11:52
 */
public interface ApiService {
//    @GET("article/list/{page}/json")
//    Observable<BaseModel<HomeArticleList>> getList(@Path("page") int page);

    @POST("User/Login")
    Observable<User> commonLogin(@Body HashMap<String, Object> map);

    @POST("User/GetUserInfo")
    Observable<UserInfo> getUserInfo(@Body HashMap<String, Object> token);

    //设置远程密码
    @POST("User/SetRemotePassword")
    Observable<BaseErr> SetRemotePassword(@Body HashMap<String, Object> params);
}
