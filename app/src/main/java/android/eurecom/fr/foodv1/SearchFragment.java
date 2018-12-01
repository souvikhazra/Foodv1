package android.eurecom.fr.foodv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class SearchFragment extends Fragment implements View.OnClickListener {
    public SearchFragment(){}
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        Button postBtn = (Button) rootView.findViewById(R.id.button);
        postBtn.setOnClickListener(this);
        return rootView;
    }
    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.button:
                firebaseAuth = FirebaseAuth.getInstance();
                if(firebaseAuth.getCurrentUser() != null) {

                    fragment = new PostFragment();

                    replaceFragment(fragment);
                }
                else{
                    Intent intent = new Intent(getActivity(), AccountActivity.class);
                    startActivity(intent);
                }
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