package com.example.majun.rxjavademo.network.api;

import com.example.majun.rxjavademo.model.BeautyModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by majun on 17/2/5.
 */
public interface BeautyApi {
    @GET("data/福利/{number}/{page}")
    Observable<BeautyModel> getBeauties(@Path("number") int number, @Path("page") int page);
}
