package ru.dmitriyivanov.producthunt.ProductHuntApi.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public class CategoryList {
    @SerializedName("categories")
    public List<Category> categories;
}
