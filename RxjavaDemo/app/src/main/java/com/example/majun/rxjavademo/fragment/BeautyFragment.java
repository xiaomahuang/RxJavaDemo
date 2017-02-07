package com.example.majun.rxjavademo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.majun.rxjavademo.R;
import com.example.majun.rxjavademo.adapter.BeautyAdapter;
import com.example.majun.rxjavademo.model.Beauty;
import com.example.majun.rxjavademo.model.BeautyModel;
import com.example.majun.rxjavademo.model.Item;
import com.example.majun.rxjavademo.network.api.BeautyApi;
import com.example.majun.rxjavademo.ui.SuperSwipeRefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by majun on 17/2/4.
 */
public class BeautyFragment extends BaseFragment implements SuperSwipeRefreshLayout.OnPullRefreshListener, SuperSwipeRefreshLayout.OnPushLoadMoreListener {

    //// TODO: 17/2/4 完善数据加载,和上拉加载 
    @Bind(R.id.superswipe)
    SuperSwipeRefreshLayout mSuperSwipeRefreshLayout;
    @Bind(R.id.beauty_recyclerview)
    RecyclerView mRecyclerView;

    private BeautyAdapter mBeautyAdapter;

    private BeautyApi beautyApi;

    private int mPage = 1;

    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {
            mSuperSwipeRefreshLayout.setRefreshing(false);
            mSuperSwipeRefreshLayout.setLoadMore(false);
        }

        @Override
        public void onError(Throwable e) {
            mSuperSwipeRefreshLayout.setRefreshing(false);
            mSuperSwipeRefreshLayout.setLoadMore(false);
        }

        @Override
        public void onNext(List<Item> beauties) {
            mBeautyAdapter.setDatas(beauties, mPage);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beauty, container, false);
        ButterKnife.bind(this, view);
        mBeautyAdapter = new BeautyAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(inflater.getContext(), 2));
        mRecyclerView.setAdapter(mBeautyAdapter);
        mSuperSwipeRefreshLayout.setRefreshing(true);
        mSuperSwipeRefreshLayout.setTargetScrollWithLayout(true);
        mSuperSwipeRefreshLayout.useFooter(true);
        View loadView = inflater.inflate(R.layout.foot_loader, null);
        mSuperSwipeRefreshLayout.setFooterView(loadView);
        //下拉刷新
        mSuperSwipeRefreshLayout.setOnPullRefreshListener(this);
        //上拉加载
        mSuperSwipeRefreshLayout.setOnPushLoadMoreListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        requestNewData();
    }

    @Override
    public void requestNewData() {
        if (beautyApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(new OkHttpClient())
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            beautyApi = retrofit.create(BeautyApi.class);
        }
        loadImage(mPage);
    }

    public void loadImage(int page) {
        subscription = beautyApi.getBeauties(10, page)
                .map(new Func1<BeautyModel, List<Item>>() {

                    @Override
                    public List<Item> call(BeautyModel beautyModel) {
                        List<Beauty> beauties = beautyModel.beauties;
                        List<Item> items = new ArrayList<>(beauties.size());
                        SimpleDateFormat beautyDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SS'Z'");
                        SimpleDateFormat itemDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                        for (Beauty beauty : beauties) {
                            try {
                                Date beautyDate = beautyDateFormat.parse(beauty.createdAt);
                                String itemDate = itemDateFormat.format(beautyDate);
                                Item item = new Item();
                                item.description = itemDate;
                                item.image_url = beauty.url;
                                items.add(item);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        return items;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadImage(mPage);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    @Override
    public void onLoadMore() {
        mPage++;
        loadImage(mPage);
    }

    @Override
    public void onPushDistance(int distance) {

    }

    @Override
    public void onPushEnable(boolean enable) {

    }
}
