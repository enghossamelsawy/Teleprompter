package com.example.hossam.teleprompter.helper;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.hossam.teleprompter.TextRotation;

/**
 * Created by hossam on 5/7/2017.
 */


public class Helper {


    /*
    *  using this  method to help to get screen size of any mobile phone
    *  this  method taking   window manger from activity
    *  it's return
    *  width of screen */
    public static int getScreenWidth(WindowManager windowManager)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
      windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        final int width = displayMetrics.widthPixels;
        return width ;

    }



    public static int getScreenHight(WindowManager windowManager)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        return height ;

    }
}
