package ru.dmitriyivanov.producthunt.ProductHuntApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.CategoryList;
import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.PostList;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public interface ProductHuntService {
    @GET("/v1/categories")
    Call<CategoryList> getCategories();

    @GET("/v1/posts/all")
    Call<PostList> getPostsByCategoryName(@Query("search[category]") String name);
}
