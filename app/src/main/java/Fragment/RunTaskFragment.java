package Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.example.haitr.planed_12062016.CountdownTimer;
import com.example.haitr.planed_12062016.TimeService;

import Class.TimePassData;
import Controller.Time_Control;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunTaskFragment extends Fragment {
    public static TextView txtRemaining, txtNameTask, txtNameTag, txtStart, txtEnd;
    private TimePassData timePassData;
    private Bundle bgetData;
    private Button btnRun, btnStop;
    private int iRun, iDone, iIdCurrent;
    private Time_Control time_control;
    private String sStart, sEnd;
    private boolean bCheck, cCheck;
    public static long millisInFuture;
    //private CountDownTimer countDownTimer;

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
        btnRun = (Button) getActivity().findViewById(R.id.button_Start);
        btnStop = (Button) getActivity().findViewById(R.id.button_End);
        //Set data into textview
        txtNameTask.setText(timePassData.getTaskName());
        txtNameTag.setText(timePassData.getTagName());
        txtRemaining.setText(timePassData.getiEsHour() + " hrs" + timePassData.getiEsMin() + " min");
        txtStart.setText(timePassData.getStart());

        //countDownTimer = new CountDownTimer(timePassData.getiEsHour() * 3600000 + timePassData.getiEsMin() * 60000, 1000);
        millisInFuture = timePassData.getiEsHour() * 3600000 + timePassData.getiEsMin() * 60000;


        //Set id for status
        iRun = timePassData.getiRun();
        iDone = timePassData.getiDone();
        iIdCurrent = timePassData.getId();

        if (iRun == 1) {
            btnRun.setEnabled(false);
        }
        // Button listener
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bCheck = time_control.AddCurrentTimeStar(iIdCurrent);
                if (bCheck) {
                    timePassData = time_control.LoadList(timePassData.getId());
                    txtStart.setText(timePassData.getStart());
                    getActivity().startService(new Intent(getContext(), TimeService.class));
                    btnRun.setEnabled(false);
                } else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cCheck = time_control.AddTimeEnd(iIdCurrent);
                if (cCheck) {
                    timePassData = time_control.LoadList(timePassData.getId());
                    txtEnd.setText(timePassData.getEnd());
                    getActivity().stopService(new Intent(getContext(), TimeService.class));
                    //countDownTimer.cancel();
                    Toast.makeText(getActivity(), "Finish", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.content_frame, new TimeFragment()).commit();
                } else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    //Count down timer
    public static long SendTime() {

        return millisInFuture;
    }


    //Send TextView
    public static TextView SendText() {
        return txtRemaining;
    }

    //BROADCAST RECEIVER
    private final BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UpdateGui(intent);
        }
    };

    private void UpdateGui(Intent intent) {
        if (intent.getExtras() != null) {
            String FocusHMS = intent.getStringExtra("TimeCountDown");
            txtRemaining.setText(FocusHMS);
        }
    }

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

        getActivity().registerReceiver(timeReceiver, new IntentFilter(
                CountdownTimer.COUNTDOWN_BROADCAST_RECEIVER));
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(timeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
