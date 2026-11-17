package com.example.testapp.screens.userprofile;

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
import com.example.testapp.models.User;
import com.example.testapp.utils.SharedPreferencesUtil;
import com.example.testapp.utils.Validator;
import com.example.testapp.screens.landing.LandingFragment;

/**
 * UserProfileFragment displays and manages a user's profile information.
 * It allows the user to update their details or an admin to view them.
 */
public class UserProfileFragment extends BaseFragment<UserProfileViewModel> implements View.OnClickListener {
    private static final String TAG = "UserProfileFragment";
    private static final String ARG_USER_UID = "USER_UID";
    private EditText etUserFirstName, etUserLastName, etUserEmail, etUserPhone, etUserPassword;
    private TextView tvUserDisplayName, tvUserDisplayEmail;
    private Button btnUpdateProfile, btnSignOut;
    private View adminBadge;
    private String selectedUid;
    private User selectedUser;
    private boolean isCurrentUser = false;

    /**
     * Creates a new instance of UserProfileFragment for a specific user.
     *
     * @param uid The UID of the user.
     * @return A new instance of UserProfileFragment.
     */
    public static UserProfileFragment newInstance(String uid) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the User Profile screen.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "User Profile";
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        User currentUser = SharedPreferencesUtil.getUser(requireContext());
        if (currentUser == null) {
            navigationHost.goBack();
            return view;
        }
        if (getArguments() != null) {
            selectedUid = getArguments().getString(ARG_USER_UID);
        }
        if (selectedUid == null) {
            selectedUid = currentUser.getId();
        }
        isCurrentUser = selectedUid.equals(currentUser.getId());
        if (!currentUser.isAdmin() && !isCurrentUser) {
            Toast.makeText(getContext(), "You are not authorized to view this profile", Toast.LENGTH_SHORT).show();
            navigationHost.goBack();
            return view;
        }
        etUserFirstName = view.findViewById(R.id.et_user_first_name);
        etUserLastName = view.findViewById(R.id.et_user_last_name);
        etUserEmail = view.findViewById(R.id.et_user_email);
        etUserPhone = view.findViewById(R.id.et_user_phone);
        etUserPassword = view.findViewById(R.id.et_user_password);
        tvUserDisplayName = view.findViewById(R.id.tv_user_display_name);
        tvUserDisplayEmail = view.findViewById(R.id.tv_user_display_email);
        btnUpdateProfile = view.findViewById(R.id.btn_edit_profile);
        btnSignOut = view.findViewById(R.id.btn_sign_out);
        adminBadge = view.findViewById(R.id.admin_badge);
        btnUpdateProfile.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        if (!isCurrentUser) {
            btnSignOut.setVisibility(View.GONE);
        }
        viewModel = getViewModel(UserProfileViewModel.class);
        setupObservers();
        viewModel.loadUser(selectedUid);
        return view;
    }

    /**
     * Sets up Observers for the ViewModel data.
     */
    private void setupObservers() {
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            selectedUser = user;
            etUserFirstName.setText(user.getFirstName());
            etUserLastName.setText(user.getLastName());
            etUserEmail.setText(user.getEmail());
            etUserPhone.setText(user.getPhone());
            etUserPassword.setText(user.getPassword());
            String displayName = user.getFirstName() + " " + user.getLastName();
            tvUserDisplayName.setText(displayName);
            tvUserDisplayEmail.setText(user.getEmail());
            if (user.isAdmin()) {
                adminBadge.setVisibility(View.VISIBLE);
            } else {
                adminBadge.setVisibility(View.GONE);
            }
            if (!isCurrentUser) {
                etUserEmail.setEnabled(false);
                etUserPassword.setEnabled(false);
            } else {
                etUserEmail.setEnabled(true);
                etUserPassword.setEnabled(true);
                btnUpdateProfile.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            if (error.equals("User profile not found")) {
                navigationHost.goBack();
            }
        });
        viewModel.getUpdateSuccessLiveData().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                viewModel.loadUser(selectedUid);
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
        if(v.getId() == R.id.btn_edit_profile) {
            updateUserProfile();
        } else if(v.getId() == R.id.btn_sign_out) {
            signOut();
        }
    }

    /**
     * Validates the input fields and updates the user profile via the ViewModel.
     */
    private void updateUserProfile() {
        if (selectedUser == null) return;
        String firstName = etUserFirstName.getText().toString();
        String lastName = etUserLastName.getText().toString();
        String phone = etUserPhone.getText().toString();
        String email = etUserEmail.getText().toString();
        String password = etUserPassword.getText().toString();
        if (!isValid(firstName, lastName, phone, email, password)) {
            return;
        }
        selectedUser.setFirstName(firstName);
        selectedUser.setLastName(lastName);
        selectedUser.setPhone(phone);
        selectedUser.setEmail(email);
        selectedUser.setPassword(password);
        if (!isCurrentUser && !selectedUser.isAdmin()) {
            Toast.makeText(getContext(), "You can only update your own profile", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.updateUser(selectedUser);
    }

    /**
     * Validates the input fields.
     *
     * @param firstName The user's first name.
     * @param lastName  The user's last name.
     * @param phone     The user's phone number.
     * @param email     The user's email address.
     * @param password  The user's password.
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean isValid(String firstName, String lastName, String phone, String email, String password) {
        if (!Validator.isNameValid(firstName)) {
            etUserFirstName.setError("First name is required");
            etUserFirstName.requestFocus();
            return false;
        }
        if (!Validator.isNameValid(lastName)) {
            etUserLastName.setError("Last name is required");
            etUserLastName.requestFocus();
            return false;
        }
        if (!Validator.isPhoneValid(phone)) {
            etUserPhone.setError("Phone number is required");
            etUserPhone.requestFocus();
            return false;
        }
        if (!Validator.isEmailValid(email)) {
            etUserEmail.setError("Email is required");
            etUserEmail.requestFocus();
            return false;
        }
        if (!Validator.isPasswordValid(password)) {
            etUserPassword.setError("Password is required");
            etUserPassword.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Signs out the current user and navigates to the landing screen.
     */
    private void signOut() {
        SharedPreferencesUtil.signOutUser(requireContext());
        navigationHost.navigateTo(new LandingFragment(), false);
    }
}
