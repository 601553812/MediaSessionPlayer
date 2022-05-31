package com.pxh.myMediaSessionPlayer.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pxh.myMediaSessionPlayer.model.SongBean
import kotlin.collections.ArrayList

class MyViewModel:ViewModel(){
    val song:MutableLiveData<SongBean> by lazy { MutableLiveData<SongBean>() }
    private lateinit var songList: ArrayList<SongBean>
    private var reverse = false
fun getSong(): SongBean {
    return song.value!!
}
    fun setSong(newSong: SongBean){
        song.value = newSong
    }
    fun setSongList(newSongList:ArrayList<SongBean>){
        songList = newSongList
        song.value = songList[0]
    }
    fun getSongList():ArrayList<SongBean>{
        return songList
    }

    fun changeReverse(){
        reverse = !reverse
        songList.reverse()
    }

}