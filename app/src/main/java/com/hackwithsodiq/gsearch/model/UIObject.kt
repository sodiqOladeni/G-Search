package com.hackwithsodiq.gsearch.model

data class UIObject<T>(
    var status:Int = 0,
    var uiData: T? = null
)
