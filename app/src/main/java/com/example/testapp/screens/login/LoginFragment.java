package com.example.testapp.screens.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.testapp.R;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.screens.dashboard.MainDashboardFragment;
import com.example.testapp.screens.register.RegisterFragment;
import com.example.testapp.utils.SharedPreferencesUtil;
import com.example.testapp.utils.Validator;

/**
 * LoginFragment handles the user authentication process.
 * It provides fields for email and password input and handles login and registration actions.
 */
public class LoginFragment extends BaseFragment<LoginViewModel> implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Login screen.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "Login";
    }

    /**
     * Indicates whether the back button should be shown in the action bar.
     *
     * @return true to show the back button, false otherwise.
     */
    @Override
    protected boolean shouldShowBackButton() {
        return true;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored in to the view.
     *
     * @param view               The View returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = getViewModel(LoginViewModel.class);
        etEmail = view.findViewById(R.id.et_login_email);
        etPassword = view.findViewById(R.id.et_login_password);
        btnLogin = view.findViewById(R.id.btn_login_login);
        tvRegister = view.findViewById(R.id.tv_login_register);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        viewModel.getLoginSuccess().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                Log.d(TAG, "User logged in: " + user.toString());
                SharedPreferencesUtil.saveUser(requireContext(), user);
                navigationHost.navigateTo(new MainDashboardFragment(), false);
            }
        });
        viewModel.getLoginError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                etPassword.setError(error);
                etPassword.requestFocus();
                SharedPreferencesUtil.signOutUser(requireContext());
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == btnLogin.getId()) {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if (!checkInput(email, password)) {
                return;
            }
            viewModel.loginUser(email, password);
        } else if (v.getId() == tvRegister.getId()) {
            navigationHost.navigateTo(new RegisterFragment(), true);
        }
    }

    /**
     * Validates the email and password input fields.
     *
     * @param email    The email string to validate.
     * @param password The password string to validate.
     * @return true if both email and password are valid, false otherwise.
     */
    private boolean checkInput(String email, String password) {
        if (!Validator.isEmailValid(email)) {
            etEmail.setError("Invalid email address");
            etEmail.requestFocus();
            return false;
        }
        if (!Validator.isPasswordValid(password)) {
            etPassword.setError("Password must be at least 6 characters long");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }
}
