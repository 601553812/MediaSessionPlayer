package com.pxh.myMediaSessionPlayer.view

import android.content.ComponentName
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.pxh.myMediaSessionPlayer.*
import com.pxh.myMediaSessionPlayer.databinding.PlayerActivityMainBinding
import com.pxh.myMediaSessionPlayer.model.SongBean
import com.pxh.myMediaSessionPlayer.service.MyService
import com.pxh.myMediaSessionPlayer.util.Util
import com.pxh.myMediaSessionPlayer.view.fragment.MainFragment
import com.pxh.myMediaSessionPlayer.view.fragment.PlayListFragment
import com.pxh.myMediaSessionPlayer.viewModel.MyViewModel

class MainActivityPlayer : AppCompatActivity(), ClickListener {

    private lateinit var mediaBrowser: MediaBrowserCompat
    private lateinit var binding: PlayerActivityMainBinding
    private lateinit var myViewModel: MyViewModel
    private lateinit var mainFragment: MainFragment
    private lateinit var playListFragment: PlayListFragment
    private var playState = true
    private var backAllowed = true
    private var fragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]
        startService(Intent(this, MyService::class.java))
        binding = DataBindingUtil.setContentView(this,
            R.layout.player_activity_main
        )
        binding.swBackAllowed.setOnCheckedChangeListener { _, isChecked ->
            backAllowed = isChecked
        }
        mediaBrowser = MediaBrowserCompat(
            this,
            ComponentName(this, MyService::class.java),
            connectionCallbacks,
            null // optional Bundle
        )
    }

    public override fun onStart() {
        super.onStart()
        if (!mediaBrowser.isConnected) {
            mediaBrowser.connect()
        }
    }

    public override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
        if (!backAllowed){
            mediaController.transportControls.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaBrowser.isConnected) {
            mediaBrowser.disconnect()
            MediaControllerCompat.getMediaController(this)?.unregisterCallback(controllerCallback)
            stopService(Intent(this, MyService::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        if (!backAllowed){
            mediaController.transportControls.pause()
        }
    }

    public override fun onStop() {
        super.onStop()
        // (see "stay in sync with the MediaSession")
    }

    private val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            val path = mediaBrowser.root
            mediaBrowser.unsubscribe(path)
            mediaBrowser.subscribe(path, object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                }
            })

            // Get the token for the MediaSession
            mediaBrowser.sessionToken.also { token ->

                // Create a MediaControllerCompat
                val mediaController = MediaControllerCompat(
                    this@MainActivityPlayer, // Context
                    token
                )

                // Save the controller
                MediaControllerCompat.setMediaController(this@MainActivityPlayer, mediaController)
            }

            // Finish building the UI
            buildTransportControls()
        }

        override fun onConnectionSuspended() {
            // The Service has crashed. Disable transport controls until it automatically reconnects
        }

        override fun onConnectionFailed() {
            // The Service has refused our connection
        }
    }

    fun buildTransportControls() {
        val mediaController = MediaControllerCompat.getMediaController(this@MainActivityPlayer)


        // Register a Callback to stay in sync
        mediaController.registerCallback(controllerCallback)
    }

    private var controllerCallback = object : MediaControllerCompat.Callback() {
        override fun onSessionEvent(event: String?, extras: Bundle?) {
            myViewModel.setSongList(
                extras?.getSerializable("list").let { it as ArrayList<SongBean> })
            mainFragment = MainFragment()
            playListFragment = PlayListFragment()
            fragmentManager.beginTransaction().add(R.id.fl_main_activity, mainFragment)
                .add(R.id.fl_main_activity, playListFragment)
                .hide(playListFragment)
                .commit()
            super.onSessionEvent(event, extras)
        }


        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            myViewModel.setSong(
                SongBean(
                metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID),
                metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE),
                metadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST),
                metadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM),
            ).apply {
                length = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
            })
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            if (state.state != PlaybackStateCompat.STATE_PLAYING){
                mainFragment.view?.findViewById<Button>(R.id.bt_play)?.text = getString(R.string.play)
                playState = true
            }
            else {
                mainFragment.view?.findViewById<Button>(R.id.bt_play)?.text = getString(R.string.pause)
                playState = false
            }
        }
    }

    override fun fragmentChange() {
        fragmentManager.beginTransaction().hide(mainFragment).show(playListFragment).commit()
    }

    override fun fragmentBack() {
        fragmentManager.beginTransaction().hide(playListFragment).show(mainFragment).commit()
    }

    override fun playListener() {
        if (playState)
        mediaController.transportControls.play()
        else
        mediaController.transportControls.pause()
    }

    override fun prevListener() {
        mediaController.transportControls.skipToPrevious()
    }

    override fun nextListener() {
        mediaController.transportControls.skipToNext()
    }

    override fun reverse() {
        mediaController.transportControls.sendCustomAction("mode", Bundle())
        myViewModel.changeReverse()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun playUriListener(id:String) {
        mediaController.transportControls.playFromUri(Util.transportUri(id), Bundle())
    }
}

