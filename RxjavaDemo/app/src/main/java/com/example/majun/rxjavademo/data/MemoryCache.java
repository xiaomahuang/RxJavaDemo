package com.example.majun.rxjavademo.data;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by majun on 17/2/13.
 */
public class MemoryCache {
    LruCache<String, Bitmap> mMemoryCatch;

    private static MemoryCache Instance;

    public static MemoryCache getInstance() {
        if (Instance == null) {
            Instance = new MemoryCache();
        }
        return Instance;
    }

    private MemoryCache() {
        long maxMemory = Runtime.getRuntime().maxMemory() / 8;
        mMemoryCatch = new LruCache<String, Bitmap>((int) maxMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public void putMemory(String id, Bitmap bitmap) {
        if (mMemoryCatch.get(id) == null) {
            mMemoryCatch.put(id, bitmap);
        }
    }

    public Bitmap getMemory(String id) {
        return mMemoryCatch.get(id);
    }

}
