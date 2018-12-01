package android.eurecom.fr.foodv1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.eurecom.fr.foodv1.databinding.FragmentAccountBinding;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.databinding.DataBindingUtil;
import static android.eurecom.fr.foodv1.FlexibleFrameLayout.ORDER_LOGIN_STATE;
import static android.eurecom.fr.foodv1.FlexibleFrameLayout.ORDER_SIGN_UP_STATE;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class AccountActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentListener, RegisterFragment.OnRegisterFragmentListener{
    private FragmentAccountBinding binding;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_account);

        LoginFragment topLoginFragment = new LoginFragment();
        topLoginFragment.setOnHeadlineSelectedListener(this);
        RegisterFragment topSignUpFragment = new RegisterFragment();
        topSignUpFragment.setOnRegisterFragmentListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sign_up_fragment, topSignUpFragment)
                .replace(R.id.login_fragment, topLoginFragment)
                .commit();

        binding.signUpFragment.setRotation(90);

        binding.button.setOnSignUpListener(topSignUpFragment);
        binding.button.setOnLoginListener(topLoginFragment);

        binding.button.setOnButtonSwitched(isLogin -> {
            binding.getRoot()
                    .setBackgroundColor(ContextCompat.getColor(
                            this,
                            isLogin ? R.color.login_background_color : R.color.register_background_color));
        });

        binding.signUpFragment.setVisibility(INVISIBLE);
        binding.button.startAnimation();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        binding.loginFragment.setPivotX(binding.loginFragment.getWidth() / 2);
        binding.loginFragment.setPivotY(binding.loginFragment.getHeight());
        binding.signUpFragment.setPivotX(binding.signUpFragment.getWidth() / 2);
        binding.signUpFragment.setPivotY(binding.signUpFragment.getHeight());
    }

    public void switchFragment(View v) {
        if (isLogin) {
            binding.loginFragment.setVisibility(VISIBLE);
            binding.loginFragment.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    binding.signUpFragment.setVisibility(INVISIBLE);
                    binding.signUpFragment.setRotation(90);
                    binding.wrapper.setDrawOrder(ORDER_LOGIN_STATE);
                }
            });
        } else {
            binding.signUpFragment.setVisibility(VISIBLE);
            binding.signUpFragment.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    binding.loginFragment.setVisibility(INVISIBLE);
                    binding.loginFragment.setRotation(-90);
                    binding.wrapper.setDrawOrder(ORDER_SIGN_UP_STATE);
                }
            });
        }

        isLogin = !isLogin;
        binding.button.startAnimation();
    }


    @Override
    public void onLoginSuccessful() {
        binding.button.setVisibility(INVISIBLE);
        binding.button.setEnabled(false);
    }

    @Override
    public void onRegistrationSuccessful() {
        binding.button.setVisibility(INVISIBLE);
        binding.button.setEnabled(false);
    }
}
