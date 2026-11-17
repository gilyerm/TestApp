package com.example.testapp.screens.register;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.testapp.R;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.screens.dashboard.MainDashboardFragment;
import com.example.testapp.utils.SharedPreferencesUtil;
import com.example.testapp.utils.Validator;
import androidx.lifecycle.Observer;
import com.example.testapp.models.User;

/**
 * RegisterFragment handles new user registration.
 * It provides input fields for user details and registers them through the ViewModel.
 */
public class RegisterFragment extends BaseFragment<RegisterViewModel> {
    private static final String TAG = "RegisterFragment";
    private EditText etEmail, etPassword, etFName, etLName, etPhone;
    private Button btnRegister;
    private TextView tvLogin;

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Register screen.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "Register";
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
        return inflater.inflate(R.layout.fragment_register, container, false);
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
        viewModel = getViewModel(RegisterViewModel.class);
        etEmail = view.findViewById(R.id.et_register_email);
        etPassword = view.findViewById(R.id.et_register_password);
        etFName = view.findViewById(R.id.et_register_first_name);
        etLName = view.findViewById(R.id.et_register_last_name);
        etPhone = view.findViewById(R.id.et_register_phone);
        btnRegister = view.findViewById(R.id.btn_register_register);
        tvLogin = view.findViewById(R.id.tv_register_login);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHost.goBack();
            }
        });
        viewModel.getRegisterSuccess().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    SharedPreferencesUtil.saveUser(requireContext(), user);
                    navigationHost.navigateTo(new MainDashboardFragment(), false);
                }
            }
        });
        viewModel.getRegisterError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                    if (error.equals("Failed to register user")) {
                        SharedPreferencesUtil.signOutUser(requireContext());
                    }
                }
            }
        });
    }

    /**
     * Validates the input fields and registers the user via the ViewModel.
     */
    private void registerUser() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String fName = etFName.getText().toString();
        String lName = etLName.getText().toString();
        String phone = etPhone.getText().toString();
        if (!checkInput(email, password, fName, lName, phone)) {
            return;
        }
        viewModel.registerUser(email, password, fName, lName, phone);
    }

    /**
     * Validates the user input fields for registration.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     * @param fName    The user's first name.
     * @param lName    The user's last name.
     * @param phone    The user's phone number.
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean checkInput(String email, String password, String fName, String lName, String phone) {
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
        if (!Validator.isNameValid(fName)) {
            etFName.setError("First name must be at least 3 characters long");
            etFName.requestFocus();
            return false;
        }
        if (!Validator.isNameValid(lName)) {
            etLName.setError("Last name must be at least 3 characters long");
            etLName.requestFocus();
            return false;
        }
        if (!Validator.isPhoneValid(phone)) {
            etPhone.setError("Phone number must be at least 10 characters long");
            etPhone.requestFocus();
            return false;
        }
        return true;
    }
}
