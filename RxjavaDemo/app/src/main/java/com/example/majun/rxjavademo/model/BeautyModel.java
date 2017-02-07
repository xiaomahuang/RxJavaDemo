package com.example.majun.rxjavademo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by majun on 17/2/5.
 */
public class BeautyModel {
    public boolean error;
    @SerializedName("results")
    public List<Beauty> beauties;
}
