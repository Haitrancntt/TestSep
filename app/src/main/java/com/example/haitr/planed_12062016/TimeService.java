package com.example.haitr.planed_12062016;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import Fragment.RunTaskFragment;

public class TimeService extends Service {
    private final static String TAG = TimeService.class.getSimpleName();
    private CountdownTimer countdownTimer;
    private long timeDifference;
    public RunTaskFragment runTaskFragment;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getTimeDifferent();
        countdownTimer = new CountdownTimer(timeDifference, 1000);
        if (!CountdownTimer.isRunning) {
            countdownTimer.start();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void getTimeDifferent() {
        timeDifference = runTaskFragment.SendTime();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
