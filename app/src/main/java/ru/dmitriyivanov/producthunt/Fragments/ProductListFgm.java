package ru.dmitriyivanov.producthunt.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.dmitriyivanov.producthunt.Adapters.ProductListAdapter;
import ru.dmitriyivanov.producthunt.ProductHuntApi.ApiFactory;
import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.Category;
import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.Post;
import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.PostList;
import ru.dmitriyivanov.producthunt.ProductHuntApi.ProductHuntService;
import ru.dmitriyivanov.producthunt.R;
import ru.dmitriyivanov.producthunt.StartupActivity;
import ru.dmitriyivanov.producthunt.UI;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public class ProductListFgm extends Fragment implements UI {
    public static final String TAG = "ProductListFgm";
    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mProductRecyclerList;
    private List<Post> mPostList;
    private ProductHuntService mApiService;
    private ProductListAdapter mProductListAdapter;
    private Category currentCategory;

    public static ProductListFgm newInstance() {
        ProductListFgm fragment = new ProductListFgm();
        return fragment;
    }

    @Override
    public void initialize() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipeContainer);
        mProductRecyclerList = (RecyclerView) mView.findViewById(R.id.productRecyclerList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.product_list_fgm, container, false);
        initialize();
        mApiService = ApiFactory.getProductHuntService();
        mProductRecyclerList.setHasFixedSize(false);
        mProductRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshProductList);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_dark);
        return mView;
    }

    // Метод перезагружает список новостей
    private void refreshCategoryItems(String category) {
        mSwipeRefreshLayout.setRefreshing(true);
        // Request
        Call<PostList> call = mApiService.getPostsByCategoryName(category);
        call.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                if(response.isSuccessful()) {
                    mPostList = response.body().posts;
                    mProductListAdapter = new ProductListAdapter(getActivity(), mPostList, onClickProductItemListener);
                    mProductRecyclerList.setAdapter(mProductListAdapter);
                    EventBus.getDefault().post(mPostList);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                t.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    // Callback for product click action
    private ProductListAdapter.onProductItemClicked onClickProductItemListener = new ProductListAdapter.onProductItemClicked() {
        @Override
        public void onClick(int position) {
            FragmentManager.show(getActivity(), ProductDetailFgm.newInstance(mPostList.get(position)), ProductListFgm.TAG, true);
        }
    };

    // Callback for refreshing product list
    private SwipeRefreshLayout.OnRefreshListener onRefreshProductList = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshCategoryItems(currentCategory.slug);
        }
    };

    @Subscribe
    public void onEvent(Category category) {
        currentCategory = category;
        refreshCategoryItems(currentCategory.slug);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
