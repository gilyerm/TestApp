package com.example.testapp.screens.userslist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testapp.R;
import com.example.testapp.adapters.UserAdapter;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.models.User;
import com.example.testapp.screens.userprofile.UserProfileFragment;

/**
 * UsersListFragment displays a list of users for administrators.
 * Admins can select users to view their profiles.
 */
public class UsersListFragment extends BaseFragment<UsersListViewModel> {
    private static final String TAG = "UsersListFragment";
    private UserAdapter userAdapter;
    private TextView tvUserCount;

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Users List screen.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "Users List";
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
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);
        RecyclerView usersList = view.findViewById(R.id.rv_users_list);
        tvUserCount = view.findViewById(R.id.tv_user_count);
        usersList.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new UserAdapter(new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                navigationHost.navigateTo(UserProfileFragment.newInstance(user.getId()), true);
            }

            @Override
            public void onLongUserClick(User user) {
                Log.d(TAG, "User long clicked: " + user);
            }
        });
        usersList.setAdapter(userAdapter);
        viewModel = getViewModel(UsersListViewModel.class);
        setupObservers();
        return view;
    }

    /**
     * Sets up Observers for the ViewModel data.
     */
    private void setupObservers() {
        viewModel.getUsersLiveData().observe(getViewLifecycleOwner(), users -> {
            userAdapter.setUserList(users);
            tvUserCount.setText("Total users: " + users.size());
        });
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            Log.e(TAG, error);
        });
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadUsers();
    }
}
