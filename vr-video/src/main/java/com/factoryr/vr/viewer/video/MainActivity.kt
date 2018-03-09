package com.factoryr.vr.viewer.video

import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.widget.SeekBar
import com.google.vr.sdk.widgets.video.VrVideoEventListener
import com.google.vr.sdk.widgets.video.VrVideoView
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object {
        private var URL_VIDEO = "https://vr.jwplayer" + ".com/content/AgqYcfAT/AgqYcfAT-8yQ1cYbs.mp4"
    }

    private var vrVideoView: VrVideoView? = null
    private var videoSeekBar: SeekBar? = null
    private var playButton: AppCompatImageButton? = null
    private var muteButton: AppCompatImageButton? = null

    private var isPaused = false
    private var isMuted = false

    private val videoEventListener = object : VrVideoEventListener() {
        override fun onNewFrame() {
            videoSeekBar!!.progress = vrVideoView!!.currentPosition.toInt()
        }

        override fun onLoadSuccess() {
            videoSeekBar!!.isEnabled = true
            videoSeekBar!!.max = vrVideoView!!.duration.toInt()
            updatePlayButton(playButton)
            updateVolumeButton(muteButton)
        }

        override fun onCompletion() {
            vrVideoView!!.seekTo(0)
            vrVideoView!!.pauseVideo()
        }

        override fun onLoadError(errorMessage: String?) {
            videoSeekBar!!.isEnabled = false
            isPaused = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vrVideoView = findViewById(R.id.video_view)
        vrVideoView!!.setEventListener(videoEventListener)

        videoSeekBar = findViewById(R.id.sb_seek)
        videoSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    vrVideoView!!.seekTo(progress.toLong())
                }
            }
        })

        playButton = findViewById(R.id.aibtn_play)
        playButton!!.setOnClickListener {
            updatePlayButton(it as AppCompatImageButton)
        }
        muteButton = findViewById(R.id.aibtn_volume)
        muteButton!!.setOnClickListener {
            updateVolumeButton(it as AppCompatImageButton)
        }

        loadContent()
    }

    private fun loadContent() {
        volumeControlStream = AudioManager.STREAM_MUSIC

        val vOptions = VrVideoView.Options()
        vOptions.inputType = VrVideoView.Options.TYPE_MONO
        //        vOptions.inputFormat = VrVideoView.Options.FORMAT_HLS;
        vOptions.inputFormat = VrVideoView.Options.FORMAT_DEFAULT
        try {
            //            vrVideoView.loadVideo(Uri.parse(URL_VIDEO), vOptions);
            vrVideoView!!.loadVideo(Uri.parse(URL_VIDEO), vOptions)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        vrVideoView!!.resumeRendering()
    }

    override fun onPause() {
        super.onPause()
        vrVideoView!!.pauseRendering()
    }

    override fun onDestroy() {
        vrVideoView!!.shutdown()
        super.onDestroy()
    }


    private fun updateVolumeButton(button: AppCompatImageButton?) {
        vrVideoView!!.setVolume(if (isMuted) 0.0f else 1.0f)
        isMuted = !isMuted
        button!!.setImageResource(if (isMuted)
            R.drawable.ic_volume_up_black_24dp
        else
            R.drawable.ic_volume_off_black_24dp)
    }

    private fun updatePlayButton(button: AppCompatImageButton?) {
        if (isPaused) {
            vrVideoView!!.playVideo()
        } else {
            vrVideoView!!.pauseVideo()
        }
        isPaused = !isPaused
        button!!.setImageResource(if (isPaused)
            R.drawable.ic_play_arrow_black_24dp
        else
            R.drawable.ic_pause_black_24dp)
    }
}
