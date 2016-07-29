package com.example.haitr.planed_12062016;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import Fragment.RunTaskFragment;

/**
 * Created by haitr on 7/28/2016.
 */
public class CountdownTimer extends CountDownTimer {
    public static final String COUNTDOWN_BROADCAST_RECEIVER = "CountDown";
    private final Intent countDownTimer = new Intent(COUNTDOWN_BROADCAST_RECEIVER);
    private static final String TAG = CountdownTimer.class.getSimpleName();
    private RunTaskFragment runTaskFragment;
    private TextView txtTime;
    public static boolean isRunning = false;

    public CountdownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    //Running time
    @Override
    public void onTick(long millisUntilFinished) {
        String sHMS = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
        txtTime = runTaskFragment.SendText();
        txtTime.setText(sHMS);
        sendTimerBroadcastTimer(sHMS, millisUntilFinished, false, txtTime);
    }

    //Finish
    @Override
    public void onFinish() {
        sendTimerBroadcastTimer("00:00:00", 0, true, txtTime);
        isRunning = false;
        stopTimer();
    }

    //    Stop timer service as soon as the Focus hour ends
    private void stopTimer() {
        Intent startTimer = new Intent(ApplicationContext.getContext(), TimeService.class);
        ApplicationContext.getContext().stopService(startTimer);
    }

    //Send Extra
    private void sendTimerBroadcastTimer(String hms, long millisUntilFinished, boolean isTimeFinished, TextView textView) {
        countDownTimer.putExtra("TimeCountDown", hms);
        // countDownTimer.putExtra("Millisecond", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));

    }
}
