package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

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
    private TextView dummy1, dummy2;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private TextView txtTag, txtTask, txtDate;
    private int esTime;
    private ImageButton btnCal, btnSave;
    private Task_Control task_control;
    private Time_Control time_control;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_time, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        np1 = (NumberPicker) getActivity().findViewById(R.id.numberPicker);
        np2 = (NumberPicker) getActivity().findViewById(R.id.numberPicker2);
        txtTag = (TextView) getActivity().findViewById(R.id.txtTagTime);
        txtTask = (TextView) getActivity().findViewById(R.id.txtTaskTime);
        txtDate = (TextView) getActivity().findViewById(R.id.txtDateTime);
        btnCal = (ImageButton) getActivity().findViewById(R.id.imageButtonTime);
        btnSave = (ImageButton) getActivity().findViewById(R.id.imageButtonSave);
        task_control = new Task_Control(LoginActivity.db.getConnection());
        time_control = new Time_Control(LoginActivity.db.getConnection());
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
        Bundle bundle = getArguments();
        task = (Task) bundle.getSerializable("task");
        txtTask.setText(task.getName());
        txtTag.setText(task.getTag_name());

        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
