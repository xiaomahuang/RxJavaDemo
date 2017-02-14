package com.example.majun.rxjavademo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.majun.rxjavademo.R;
import com.example.majun.rxjavademo.data.LocalCache;
import com.example.majun.rxjavademo.data.MemoryCache;
import com.example.majun.rxjavademo.model.Beauty;
import com.example.majun.rxjavademo.model.Item;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by majun on 17/2/4.
 */
public class BeautyAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<Item> datas;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemHolder itemHolder = (ItemHolder) holder;
        Item beauty = datas.get(position);
        final String id = beauty._id;
        final String mImageUrl = beauty.image_url;
//        Observable<Bitmap> mMemoryObservable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                Bitmap bitmap = MemoryCache.getInstance().getMemory(id);
//                if (bitmap != null) {
//                    Log.i("majun", "memory_onnext");
//                    subscriber.onNext(bitmap);
//                } else {
//                    Log.i("majun", "memory_complete");
//                    subscriber.onCompleted();
//                }
//            }
//        });
//
//        Observable<Bitmap> mLocalObservable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                Bitmap bitmap = LocalCache.getInstance().readItem(id);
//                if (bitmap != null) {
//
//                    Log.i("majun", "local_onnext");
//                    subscriber.onNext(bitmap);
//                } else {
//                    Log.i("majun", "local_complete");
//                    subscriber.onCompleted();
//                }
//            }
//        });
//
//        Observable<Bitmap> mNetObservable = Observable.just(mImageUrl)
//                .flatMap(new Func1<String, Observable<Bitmap>>() {
//                    @Override
//                    public Observable<Bitmap> call(String s) {
//                        try {
//                            Bitmap bitmap = Glide.with(mContext).load(s).asBitmap().centerCrop().into(500, 500).get();
//                            MemoryCache.getInstance().putMemory(id, bitmap);
//                            LocalCache.getInstance().writeItem(id, bitmap);
//                            Log.i("majun", "net_get");
//                            return Observable.just(bitmap);
//                        } catch (InterruptedException e) {
//                            Log.e("majun", e.getMessage(), e);
//                        } catch (ExecutionException e) {
//                            Log.e("majun", e.getMessage(), e);
//                        }
//                        return null;
//                    }
//                })
//                .subscribeOn(Schedulers.io());
//        Observable.concat(mMemoryObservable, mLocalObservable, mNetObservable)
//                .first()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Bitmap>() {
//                    @Override
//                    public void call(Bitmap bitmap) {
//                        itemHolder.imageView.setImageBitmap(bitmap);
//                    }
//                });
        if (MemoryCache.getInstance().getMemory(id) != null) {
            itemHolder.imageView.setImageBitmap(MemoryCache.getInstance().getMemory(id));
        } else if (LocalCache.getInstance().readItem(id) != null) {
            itemHolder.imageView.setImageBitmap(LocalCache.getInstance().readItem(id));
        } else {
            Observable.just(mImageUrl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.newThread())
                    .map(new Func1<String, Bitmap>() {
                        @Override
                        public Bitmap call(String s) {
                            Bitmap bitmap = null;
                            try {
                                bitmap = Glide.with(mContext).load(s).asBitmap().centerCrop().into(100, 100).get();
                                MemoryCache.getInstance().putMemory(id, bitmap);
                                LocalCache.getInstance().writeItem(id, bitmap);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            return bitmap;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                        @Override
                        public void call(Bitmap bitmap) {
                            itemHolder.imageView.setImageBitmap(bitmap);
                        }
                    });

        }
        itemHolder.description.setText(beauty.description);
    }

    @Override
    public int getItemCount() {
        if (datas == null) {
            return 0;
        } else {
            return datas.size();
        }
    }

    public void setDatas(List<Item> beauties, int page) {
        if (page == 1) {
            datas = beauties;
        } else {
            datas.addAll(beauties);
        }
        notifyDataSetChanged();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_image)
        ImageView imageView;
        @Bind(R.id.item_description)
        TextView description;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
