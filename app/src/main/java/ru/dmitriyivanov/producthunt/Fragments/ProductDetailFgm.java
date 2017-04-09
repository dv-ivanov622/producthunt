package ru.dmitriyivanov.producthunt.Fragments;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.Post;
import ru.dmitriyivanov.producthunt.R;
import ru.dmitriyivanov.producthunt.StartupActivity;
import ru.dmitriyivanov.producthunt.UI;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public class ProductDetailFgm extends Fragment implements UI {
    public static final String TAG = "ProductDetailFgm";
    private View mView;
    private TextView productTitle, productDescription, productVote;
    private ImageView productImg;
    private Post mPost;
    private Button mGetItBtn;

    public static ProductDetailFgm newInstance(Post post) {
        Bundle bundle = new Bundle();
        bundle.putString("post", new Gson().toJson(post));
        ProductDetailFgm fragment = new ProductDetailFgm();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initialize() {
        productTitle = (TextView) mView.findViewById(R.id.productTitle);
        productDescription = (TextView) mView.findViewById(R.id.productDescription);
        productVote = (TextView) mView.findViewById(R.id.productVote);
        productImg = (ImageView) mView.findViewById(R.id.productImg);
        mGetItBtn = (Button) mView.findViewById(R.id.getItButton);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.product_item_detail, container, false);
        initialize();
        mPost = new Gson().fromJson(getArguments().getString("post"), Post.class);
        productTitle.setText(mPost.name);
        productDescription.setText(mPost.description);
        productVote.setText(String.valueOf(mPost.votes));
        Picasso.with(getActivity()).load(mPost.screenshot.image).placeholder(R.drawable.downloading_process).into(productImg, new Callback() {
            @Override
            public void onSuccess() {
                productImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }

            @Override
            public void onError() {

            }
        });
        mGetItBtn.setOnClickListener(onGetItListener);
        return mView;
    }

    // Обрабатываем клик Get It у продукта и открываем в браузере
    private View.OnClickListener onGetItListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPost.url));
            startActivity(intent);
        }
    };

}
