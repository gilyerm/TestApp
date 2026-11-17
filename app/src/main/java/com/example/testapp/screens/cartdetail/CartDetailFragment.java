package com.example.testapp.screens.cartdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testapp.R;
import com.example.testapp.adapters.FoodsAdapter;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.models.Cart;

/**
 * CartDetailFragment displays the details of a specific cart.
 * It shows the title, total items, total price, and the list of food items in the cart.
 */
public class CartDetailFragment extends BaseFragment<CartDetailViewModel> {
    private TextView tvCartTitle;
    private TextView tvCartTotalAmount, tvCartTotalPrice;
    private RecyclerView rvCartItems;
    private ProgressBar progressBar;

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
        View view = inflater.inflate(R.layout.fragment_cart_detail, container, false);
        tvCartTitle = view.findViewById(R.id.tv_cart_title);
        tvCartTotalAmount = view.findViewById(R.id.tv_cart_total_items);
        tvCartTotalPrice = view.findViewById(R.id.tv_cart_total_price);
        rvCartItems = view.findViewById(R.id.rv_cart_items);
        rvCartItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        progressBar = view.findViewById(R.id.cart_detail_progress_bar);
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
        viewModel = getViewModel(CartDetailViewModel.class);
        String cartId = null;
        if (getArguments() != null) {
            cartId = getArguments().getString("cart_id");
        }
        if (cartId == null) {
            Toast.makeText(requireContext(), "Cart ID not found", Toast.LENGTH_SHORT).show();
            navigationHost.goBack();
            return;
        }
        viewModel.cart.observe(getViewLifecycleOwner(), this::setCartView);
        viewModel.isLoading.observe(getViewLifecycleOwner(), loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            rvCartItems.setVisibility(loading ? View.GONE : View.VISIBLE);
        });
        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
            navigationHost.goBack();
        });
        viewModel.loadCart(cartId);
    }

    /**
     * Populates the views with the cart's data.
     *
     * @param cart The Cart object containing the details to display.
     */
    private void setCartView(Cart cart) {
        if (cart == null) return;
        tvCartTitle.setText(cart.getTitle());
        tvCartTotalAmount.setText(String.valueOf(cart.getFoods().size()));
        tvCartTotalPrice.setText(String.format("₪%.2f", cart.getTotalPrice()));
        FoodsAdapter foodsAdapter = new FoodsAdapter();
        foodsAdapter.setFoods(cart.getFoods());
        rvCartItems.setAdapter(foodsAdapter);
    }

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Cart Details screen.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "Cart Details";
    }
}
