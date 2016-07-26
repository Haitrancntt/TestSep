package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.example.haitr.planed_12062016.R;

import Class.Task;

/**
 * Created by Thanh Huy on 7/23/2016.
 */
public class CreateTimeFragment extends Fragment {
    Task task;
    NumberPicker np1, np2;
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
        np1.setMinValue(0);
        np1.setMaxValue(8);
        np2.setMinValue(0);
        np2.setMaxValue(59);
        Bundle bundle = getArguments();
        task = (Task) bundle.getSerializable("task");

    }
}
