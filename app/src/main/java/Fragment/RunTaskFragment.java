package Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.concurrent.TimeUnit;

import Class.TimePassData;
import Controller.Time_Control;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunTaskFragment extends Fragment {
    private TextView txtRemaining, txtNameTask, txtNameTag, txtStart, txtEnd;
    private TimePassData timePassData;
    private Bundle bgetData;
    private Button btnRun;
    private int iRun, iDone, iIdCurrent;
    private Time_Control time_control;
    private String sStart, sEnd;
    private boolean bCheck, cCheck;
    private CountDownTimer countDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_run_task, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo m3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWifi.isConnected() == false & m3g.isConnected() == false) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Error")
                    .setMessage("Please make sure that your device is already connected to the Internet!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finishAffinity();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            //Create time control

            time_control = new Time_Control(LoginActivity.db.getConnection());

            //Get Data
            bgetData = getArguments();
            timePassData = (TimePassData) bgetData.getSerializable("time");

            // Declare textview
            txtNameTask = (TextView) getActivity().findViewById(R.id.textView_NameTask_Run);
            txtNameTag = (TextView) getActivity().findViewById(R.id.textView_Run_NameTag);
            txtStart = (TextView) getActivity().findViewById(R.id.textView_Run_Start);
            txtEnd = (TextView) getActivity().findViewById(R.id.textView_Run_End);
            txtRemaining = (TextView) getActivity().findViewById(R.id.textView_Run_Remaining);
            btnRun = (Button) getActivity().findViewById(R.id.button_Start_End);

            //Set data into textview
            txtNameTask.setText(timePassData.getTaskName());
            txtNameTag.setText(timePassData.getTagName());
            txtRemaining.setText(timePassData.getiEsHour() + " hrs" + timePassData.getiEsMin() + " min");
            txtStart.setText(timePassData.getStart());

            //Count down timer
            countDownTimer = new CountDownTimer(timePassData.getiEsHour() * 3600000 + timePassData.getiEsMin() * 60000, 1000);

            //Set id for status
            iRun = timePassData.getiRun();
            iDone = timePassData.getiDone();
            iIdCurrent = timePassData.getId();
            if (iRun == 1) {
                btnRun.setText("End");
            } else {
                btnRun.setText("Start");
            }

            // Button listener
            btnRun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iRun == 0) {
                        bCheck = time_control.AddCurrentTimeStar(iIdCurrent);
                        if (bCheck) {
                            timePassData = time_control.LoadList(timePassData.getId());
                            txtStart.setText(timePassData.getStart());
                            countDownTimer.start();
                            btnRun.setText("End");
                        } else {
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else if (iRun == 1) {
                        cCheck = time_control.AddTimeEnd(iIdCurrent);
                        if (cCheck) {
                            timePassData = time_control.LoadList(timePassData.getId());
                            txtEnd.setText(timePassData.getEnd());
                            countDownTimer.cancel();
                            Toast.makeText(getActivity(), "Finish", Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getFragmentManager();
                            fm.beginTransaction().replace(R.id.content_frame, new TimeFragment()).commit();
                        } else {
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    //On backed button
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.content_frame, new TimeFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }

    //TIMER
    private class CountDownTimer extends android.os.CountDownTimer {
        public CountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            long millis = l;
            String sHMS = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            txtRemaining.setText(sHMS);

        }

        @Override
        public void onFinish() {

        }
    }
}
