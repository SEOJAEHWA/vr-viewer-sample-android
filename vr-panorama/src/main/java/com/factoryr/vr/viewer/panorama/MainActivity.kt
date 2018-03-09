package com.factoryr.vr.viewer.panorama

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener
import com.google.vr.sdk.widgets.pano.VrPanoramaView

class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {

    companion object {
        private val VR_IMAGE_URL = "http://home.smartinfini.com/andes.jpg"
    }

    private var vrPanoramaView: VrPanoramaView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vrPanoramaView = findViewById(R.id.pano_view)
        val infoSwitch = findViewById<SwitchCompat>(R.id.switch_info)
        val stereoModeSwitch = findViewById<SwitchCompat>(R.id.switch_stereo_mode)
        val fullScreenSwitch = findViewById<SwitchCompat>(R.id.switch_full_screen)
        infoSwitch.setOnCheckedChangeListener(this)
        stereoModeSwitch.setOnCheckedChangeListener(this)
        fullScreenSwitch.setOnCheckedChangeListener(this)
        findViewById<View>(R.id.abtn_show_on_fragment).setOnClickListener(this)
        findViewById<View>(R.id.abtn_show_on_recyclerView).setOnClickListener(this)

        vrPanoramaView!!.apply {
            setFlingingEnabled(true)
            setInfoButtonEnabled(infoSwitch.isChecked)
            setStereoModeButtonEnabled(stereoModeSwitch.isChecked)
            setFullscreenButtonEnabled(fullScreenSwitch.isChecked)
            rootView.findViewById<View>(R.id.info_button).setOnClickListener(this@MainActivity)
            setEventListener(PanoramaEventListener)
        }

        loadContent()
    }

    private fun loadContent() {
        GlideApp.with(this)
                .asBitmap()
                .load(VR_IMAGE_URL)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val options = VrPanoramaView.Options()
                        options.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER
                        vrPanoramaView!!.loadImageFromBitmap(resource, options)
                    }
                })
    }

    object PanoramaEventListener : VrPanoramaEventListener() {
        override fun onLoadSuccess() {
            super.onLoadSuccess()
            Log.i("VRPanoramaView", "onLoadSuccess")
        }

        override fun onLoadError(errorMessage: String) {
            super.onLoadError(errorMessage)
            Log.e("VRPanoramaView", "onLoadError: " + errorMessage)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.switch_info -> vrPanoramaView!!.setInfoButtonEnabled(isChecked)
            R.id.switch_stereo_mode -> vrPanoramaView!!.setStereoModeButtonEnabled(isChecked)
            R.id.switch_full_screen -> vrPanoramaView!!.setFullscreenButtonEnabled(isChecked)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.info_button -> Toast.makeText(v.context, "INFO", Toast.LENGTH_SHORT).show()
            R.id.abtn_show_on_fragment -> {

            }
            R.id.abtn_show_on_recyclerView -> {

            }
        }
    }
}
