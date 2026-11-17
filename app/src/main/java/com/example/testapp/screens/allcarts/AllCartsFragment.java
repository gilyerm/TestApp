package com.example.testapp.screens.allcarts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testapp.R;
import com.example.testapp.adapters.CartAdapter;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.screens.cartdetail.CartDetailFragment;

/**
 * AllCartsFragment displays a list of all available carts from the database.
 * Users can view details for any cart from the list.
 */
public class AllCartsFragment extends BaseFragment<AllCartsViewModel> {
    private CartAdapter cartAdapter;

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
        View view = inflater.inflate(R.layout.fragment_all_carts, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_carts);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartAdapter = new CartAdapter(cart -> {
            CartDetailFragment fragment = new CartDetailFragment();
            Bundle args = new Bundle();
            args.putString("cart_id", cart.getId());
            fragment.setArguments(args);
            navigationHost.navigateTo(fragment, true);
        });
        recyclerView.setAdapter(cartAdapter);
        return view;
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
        viewModel = getViewModel(AllCartsViewModel.class);
        viewModel.carts.observe(getViewLifecycleOwner(), carts -> {
            cartAdapter.setItems(carts);
        });
        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });
        viewModel.loadAllCarts();
    }

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the All Carts screen.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "All Carts";
    }
}
