package ru.dmitriyivanov.producthunt.ProductHuntApi.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public class Category {
    @SerializedName("name")
    public String name;
    @SerializedName("slug")
    public String slug;
}
