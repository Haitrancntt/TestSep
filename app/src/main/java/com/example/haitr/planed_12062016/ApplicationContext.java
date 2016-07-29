package com.example.haitr.planed_12062016;

import android.app.Application;
import android.content.Context;

/**
 * Created by haitr on 7/29/2016.
 */
public class ApplicationContext extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    public static final Context getContext() {
        return context;
    }
}
