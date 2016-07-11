package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import Controller.Tag_Control;

/**
 * Created by Thanh Huy on 7/11/2016.
 */
public class CreateTagFragment extends Fragment {
    private ImageButton btnAdd;
    private EditText txtName;
    private int Account_ID;
    private Tag_Control tag_control;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_tag, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnAdd = (ImageButton) getActivity().findViewById(R.id.btnCreateTag);
        txtName = (EditText) getActivity().findViewById(R.id.txtCreateTag);
        this.Account_ID = LoginActivity.Account_Id;
        tag_control = new Tag_Control(LoginActivity.db.getConnection());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                boolean b = tag_control.AddTag(name, Account_ID);
                if (b) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new TagFragment()).commit();

                }
            }
        });
    }
}
