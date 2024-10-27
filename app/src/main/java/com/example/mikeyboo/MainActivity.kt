package com.example.mikeyboo

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {


    private lateinit var fab_plus : FloatingActionButton

    private lateinit var change : MaterialButton

    private lateinit var mikeyText : MaterialTextView

    private lateinit var address : MaterialTextView

    private lateinit var instagram_icon : ShapeableImageView

    private lateinit var whatsapp_icon : ShapeableImageView

    private lateinit var phone_icon : ShapeableImageView

    private lateinit var googleMap_icon : ShapeableImageView

    private lateinit var worksText : MaterialTextView

    private lateinit var what_do_today_text : MaterialTextView

    private lateinit var lak_gel : MaterialButton

    private lateinit var milui : MaterialButton

    private lateinit var menikur : MaterialButton

    private lateinit var tikun : MaterialButton

    private lateinit var hasara : MaterialButton

    private lateinit var tzior : MaterialButton

    private lateinit var bnia_hadash : MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViews()
        initViews()
        change.visibility = View.INVISIBLE

    }

    private fun findViews() {
        fab_plus = findViewById(R.id.fab_plus)
        change = findViewById(R.id.change)
        mikeyText = findViewById(R.id.mikeyText)
        address = findViewById(R.id.address)
        instagram_icon = findViewById(R.id.instagram_icon)
        whatsapp_icon = findViewById(R.id.whatsapp_icon)
        phone_icon = findViewById(R.id.phone_icon)
        googleMap_icon = findViewById(R.id.googleMap_icon)
        worksText = findViewById(R.id.worksText)
        what_do_today_text = findViewById(R.id.what_do_today_text)
        lak_gel = findViewById(R.id.lak_gel)
        milui = findViewById(R.id.milui)
        menikur = findViewById(R.id.menikur)
        tikun = findViewById(R.id.tikun)
        hasara = findViewById(R.id.hasara)
        tzior = findViewById(R.id.tzior)
        bnia_hadash = findViewById(R.id.bnia_hadash)
    }

    private fun initViews() {
        fab_plus.setOnClickListener{OpenOptions()}
        change.setOnClickListener{changeTor()}
        instagram_icon.setOnClickListener {MoveToInstagram()}
        whatsapp_icon.setOnClickListener {MoveToWhatsapp()}
        phone_icon.setOnClickListener {MoveToPhone()}
        googleMap_icon.setOnClickListener {MoveToGoogleMaps()}
        lak_gel.setOnClickListener{OpenCalendar()}
        milui.setOnClickListener{OpenCalendar()}
        menikur.setOnClickListener{OpenCalendar()}
        tikun.setOnClickListener{OpenCalendar()}
        hasara.setOnClickListener{OpenCalendar()}
        tzior.setOnClickListener{OpenCalendar()}
        bnia_hadash.setOnClickListener{OpenCalendar()}
    }

    private fun changeTor() {
        TODO("Not yet implemented")
    }

    private fun OpenCalendar() {
        TODO("Not yet implemented")
    }

    private fun OpenOptions() {
        change.visibility = View.VISIBLE
    }

    private fun MoveToGoogleMaps() {
        TODO("Not yet implemented")
    }

    private fun MoveToPhone() {
        TODO("Not yet implemented")
    }

    private fun MoveToWhatsapp() {
        TODO("Not yet implemented")
    }

    private fun MoveToInstagram() {
        TODO("Not yet implemented")
    }
}