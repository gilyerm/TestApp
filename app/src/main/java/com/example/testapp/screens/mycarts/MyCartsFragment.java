package com.example.testapp.screens.mycarts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testapp.R;
import com.example.testapp.adapters.CartAdapter;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.core.ViewModelFactory;
import com.example.testapp.utils.SharedPreferencesUtil;
import com.example.testapp.screens.addcart.AddCartFragment;
import com.example.testapp.screens.cartdetail.CartDetailFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

/**
 * MyCartsFragment displays a list of carts created by the currently logged-in user.
 * It provides functionality to view cart details or add a new cart.
 */
public class MyCartsFragment extends BaseFragment {
    private MyCartsViewModel viewModel;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private ExtendedFloatingActionButton fabAddCart;

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
        View view = inflater.inflate(R.layout.fragment_my_carts, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_carts);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        progressBar = view.findViewById(R.id.my_cart_progress_bar);
        emptyStateLayout = view.findViewById(R.id.empty_state);
        fabAddCart = view.findViewById(R.id.fab_add_cart);
        fabAddCart.setOnClickListener(v -> navigationHost.navigateTo(new AddCartFragment(), true));
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
        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(MyCartsViewModel.class);
        viewModel.carts.observe(getViewLifecycleOwner(), carts -> {
            cartAdapter.setCartList(carts);
            if (carts.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyStateLayout.setVisibility(View.GONE);
            }
        });
        viewModel.isLoading.observe(getViewLifecycleOwner(), loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });
        viewModel.loadCarts(SharedPreferencesUtil.getUserId(requireContext()));
    }

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the My Carts screen.
     */
    @Override
    protected String getTitle() {
        return "My Carts";
    }
}
