package ru.dmitriyivanov.producthunt;

import java.util.ArrayList;
import java.util.List;

import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.Category;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public class Utils {
    public static List<String> getTitleCategories(List<Category> categories) {
        List<String> list = new ArrayList<>();
        for(Category c : categories)
            list.add(c.name);
        return list;
    }
}
