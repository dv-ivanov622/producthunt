package ru.dmitriyivanov.producthunt.ProductHuntApi.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public class PostList {
    @SerializedName("posts")
    public List<Post> posts;
}
