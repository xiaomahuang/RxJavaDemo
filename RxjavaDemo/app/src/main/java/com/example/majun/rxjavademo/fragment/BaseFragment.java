package com.example.majun.rxjavademo.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;

import rx.Subscription;

/**
 * Created by majun on 2016/6/16.
 */
public abstract class BaseFragment extends Fragment {
    protected Subscription subscription;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }



    public abstract void requestNewData();
}
