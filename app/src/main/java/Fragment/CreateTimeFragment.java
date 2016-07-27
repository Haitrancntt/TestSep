package Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.Calendar;

import Class.Task;
import Controller.Task_Control;
import Controller.Time_Control;

/**
 * Created by Thanh Huy on 7/23/2016.
 */
public class CreateTimeFragment extends Fragment {
    private Task task;
    private NumberPicker np1, np2;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private TextView txtTag, txtTask, txtDate;
    private int esTime;
    private ImageButton btnCal, btnSave;
    private Task_Control task_control;
    private Time_Control time_control;
    private String sDate;
    private int accountId, taskId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_time, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        task = (Task) bundle.getSerializable("task");
        np1 = (NumberPicker) getActivity().findViewById(R.id.numberPicker);
        np2 = (NumberPicker) getActivity().findViewById(R.id.numberPicker2);
        txtTag = (TextView) getActivity().findViewById(R.id.txtTagTime);
        txtTask = (TextView) getActivity().findViewById(R.id.txtTaskTime);
        txtDate = (TextView) getActivity().findViewById(R.id.txtDateTime);
        btnCal = (ImageButton) getActivity().findViewById(R.id.imgButtonTime);
        btnSave = (ImageButton) getActivity().findViewById(R.id.imageButtonSave);
        task_control = new Task_Control(LoginActivity.db.getConnection());
        time_control = new Time_Control(LoginActivity.db.getConnection());
        accountId = LoginActivity.Account_Id;
        try {
            taskId = task_control.GetTaskIDByName(task.getName(), accountId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR); // current year
        mMonth = calendar.get(Calendar.MONTH); // current month
        mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
        np1.setMinValue(00);
        np1.setMaxValue(8);
        np2.setMinValue(0);
        np2.setMaxValue(59);
        np2.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
        txtTask.setText(task.getName());
        txtTag.setText(task.getTag_name());
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR); // current year
        mMonth = calendar.get(Calendar.MONTH); // current month
        mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
        txtDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        sDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtDate.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);
                        sDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        // LoadList(accountID, sDate);
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esTime = np1.getValue() * 60 + np2.getValue();
                boolean b = time_control.InsertNewTime(taskId, accountId, sDate, esTime);
                if (b) {
                    Toast.makeText(getActivity(), "Insert Time for Task Successfully!", Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, new TimeFragment()).commit();
                }

            }
        });
    }
}
