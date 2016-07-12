package Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.haitr.planed_12062016.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private ImageButton imgbutton_CreateAccount, imgbutton_ChangePass, imgbutton_ResetPass;
    private FragmentManager fragmentManager;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imgbutton_CreateAccount = (ImageButton) getActivity().findViewById(R.id.imagebutton_createaccount);
        imgbutton_CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager=getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CreateNewAccountFragment()).commit();
            }
        });


        imgbutton_ResetPass = (ImageButton) getActivity().findViewById(R.id.imagebutton_resetpass);
        imgbutton_ChangePass = (ImageButton) getActivity().findViewById(R.id.imagebutton_changepass);

    }
}
