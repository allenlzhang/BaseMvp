package com.carlt.basemvp.network;

public interface IApiService {

    <T> T getService(final Class<T> service);

}
