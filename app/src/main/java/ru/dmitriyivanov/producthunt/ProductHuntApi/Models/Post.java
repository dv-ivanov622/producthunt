package ru.dmitriyivanov.producthunt.ProductHuntApi.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public class Post {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("tagline")
    public String description;
    @SerializedName("votes_count")
    public int votes;
    @SerializedName("thumbnail")
    public Thumb thumb;
    @SerializedName("screenshot_url")
    public Screenshot screenshot;
    @SerializedName("redirect_url")
    public String url;

    public class Thumb {
        @SerializedName("image_url")
        public String image;
    }

    public class Screenshot {
        @SerializedName("300px")
        public String image;
    }
}
