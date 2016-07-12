package Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.haitr.planed_12062016.Encryption;
import com.example.haitr.planed_12062016.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewAccountFragment extends Fragment {
    private EditText txtName, txtEmail, txtPassword;
    private String sName, sEmail, sPassword;
    private ImageButton btnCreate;
    private Encryption encryption;
    private String sPassEncryp;
    private FragmentManager fragmentManager = getFragmentManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_account, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtName = (EditText) getActivity().findViewById(R.id.editText_Name);
        sName = txtName.getText().toString().trim();
        txtEmail = (EditText) getActivity().findViewById(R.id.editText_Email);
        sEmail = txtEmail.getText().toString().trim();
        txtPassword = (EditText) getActivity().findViewById(R.id.editText_Password);
        sPassword = txtPassword.getText().toString().trim();
        sPassEncryp = encryption.Encryption(sPassword);
        btnCreate = (ImageButton) getActivity().findViewById(R.id.imagebutton_Create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), encryption.Encryption(sPassEncryp), Toast.LENGTH_LONG).show();
            }
        });
    }


}
