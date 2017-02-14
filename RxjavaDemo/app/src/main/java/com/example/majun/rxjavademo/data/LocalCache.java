package com.example.majun.rxjavademo.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.majun.rxjavademo.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by majun on 17/2/13.
 */
public class LocalCache {
    public static File LOCAL_DIR = App.getInstance().getFilesDir();
    private static LocalCache Instance;

    private LocalCache() {
    }

    public static LocalCache getInstance() {
        if (Instance == null) {
            Instance = new LocalCache();
        }
        return Instance;
    }

    public Bitmap readItem(String name) {
        File file = new File(LOCAL_DIR, name);
        if (!file.exists()) {
            return null;
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeItem(String name, Bitmap bitmap) {
        try {
            File file = new File(LOCAL_DIR, name);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                return;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteData() {
        for (File file : LOCAL_DIR.listFiles()) {
            file.delete();
        }
    }
}