package com.pxh.myMediaSessionPlayer

interface ClickListener {
    fun playListener()
    fun prevListener()
    fun nextListener()
    fun fragmentChange()
    fun fragmentBack()
    fun reverse()
    fun playUriListener(id:String)
}