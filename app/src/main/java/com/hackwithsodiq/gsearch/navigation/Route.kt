package com.hackwithsodiq.gsearch.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.hackwithsodiq.gsearch.R

enum class Route(@StringRes val title: Int, val route: String, @DrawableRes val selectedIcon: Int, @DrawableRes val unSelectedIcon: Int) {
    HOME(R.string.home, "home", R.drawable.ic_home_selected, R.drawable.ic_home_unselected),
    REPOSITORIES(R.string.repositories, "repositories", R.drawable.ic_search_selected, R.drawable.ic_search_unselected),
    USERS(R.string.users, "users", R.drawable.ic_user_selected, R.drawable.ic_user_unselected),
}
