package android.eurecom.fr.foodv1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.FrameLayout;


public class AccountFragment extends Fragment implements View.OnClickListener {

    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        Button registerBtn = (Button) rootView.findViewById(R.id.button3);
        Button LoginBtn = (Button) rootView.findViewById(R.id.button2);

        registerBtn.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.button3:

                fragment = new RegisterFragment();

                replaceFragment(fragment);
                break;

            case R.id.button2:

                fragment = new LoginFragment();

                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();



        transaction.replace(R.id.fragment_container, someFragment);

        transaction.addToBackStack(null);

        transaction.commit();

    }


}
