package com.pxh.myMediaSessionPlayer.model

import com.pxh.myMediaSessionPlayer.R
import junit.framework.TestCase



class SongBeanTest() : TestCase() {
    lateinit var song0:SongBean
    lateinit var song1:SongBean
    lateinit var song2:SongBean



    public override fun setUp() {
        super.setUp()
         song0 = SongBean(R.raw.a.toString(), "a", "singer", "album").apply { length = 0 }
         song1 = SongBean(R.raw.b.toString(), "b", "singer", "album").apply { length = 1 }
         song2 = SongBean(R.raw.c.toString(), "c", "singer", "album").apply { length = 2 }
    }

    public override fun tearDown() {

    }


    fun testTestToString() {
        assertEquals("SongBean(id='${R.raw.b}', title='b', artist='singer', album='album', length=1)",song1.toString())

    }

    fun testGetId() {
        assertEquals(R.raw.a.toString(),song0.id)
    }

    fun testSetId() {
       song0.id = R.raw.a.toString()
    }

    fun testShowTime() {
        assertEquals("0:0",song2.showTime())

    }



}