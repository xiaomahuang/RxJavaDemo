package com.example.majun.rxjavademo.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.majun.rxjavademo.BaseFragment;
import com.example.majun.rxjavademo.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by majun on 2016/6/16.
 */
public class PhotoFragment extends BaseFragment {
    @Bind(R.id.layout)
    LinearLayout layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);
        //打开手机pictures的图片
        File folders = new File(Environment.getExternalStorageDirectory() + "/Pictures");
        Observable.just(folders).flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                return Observable.from(file.listFiles());
            }
        }).flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                if (file.listFiles() == null) {
                    return null;
                }
                return Observable.from(file.listFiles());
            }
        }).filter(new Func1<File, Boolean>() {
            @Override
            public Boolean call(File file) {
                return file.toString().endsWith(".jpg") || file.toString().endsWith(".png");
            }
        }).map(new Func1<File, Bitmap>() {
            @Override
            public Bitmap call(File file) {
                return BitmapFactory.decodeFile(file.toString());
            }
        }).subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        ImageView imageView = new ImageView(getActivity());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(bitmap);
                        layout.addView(imageView);
                    }
                });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
