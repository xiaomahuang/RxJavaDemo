package com.example.majun.rxjavademo.network.api;

import com.example.majun.rxjavademo.model.Item;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by majun on 17/2/4.
 */
public interface ZhuangBApi {
    @GET("search")
    Observable<List<Item>> search(@Query("q") String name);
}
