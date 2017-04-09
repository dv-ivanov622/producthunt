package ru.dmitriyivanov.producthunt.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import ru.dmitriyivanov.producthunt.R;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public class FragmentManager {

    public static void show(FragmentActivity activity, Fragment fragment, String tag, Boolean addToStack) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment, tag);
        if(addToStack)
            ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    public static void reshowProductList(FragmentActivity activity) {
        int count = activity.getSupportFragmentManager().getBackStackEntryCount();
        if(count >= 1) {
            activity.getSupportFragmentManager().popBackStack();
        }
    }

}
