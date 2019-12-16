package com.tuan88291.mvvmpattern.view.activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.tuan88291.mvvmpattern.BaseActivity
import com.tuan88291.mvvmpattern.R
import com.tuan88291.mvvmpattern.data.local.model.Data
import com.tuan88291.mvvmpattern.databinding.ActivityMainBinding
import com.tuan88291.mvvmpattern.utils.Utils.startSocketService
import com.tuan88291.mvvmpattern.utils.observe.AutoDisposable
import com.tuan88291.mvvmpattern.utils.observe.addTo
import com.tuan88291.mvvmpattern.view.fragment.chat.ChatFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var binding: ActivityMainBinding? = null
    private var item: Data? = null
    private var autodis: AutoDisposable? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        startSocketService(this)
        autodis = AutoDisposable(this.lifecycle)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding?.appBar?.toolbar)
        item = ViewModelProviders.of(this).get(Data::class.java)

        val toggle = ActionBarDrawerToggle(
            this, binding?.drawerLayout, binding?.appBar?.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding?.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        binding?.navView?.setNavigationItemSelectedListener(this)
        binding?.appBar?.title?.text = "Chat Socket"
        addFragment(ChatFragment())
    }

    override fun onBackPressed() {
        if (binding?.drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun setItem(it: String) {
        item?.example = it
    }

    fun getItem(): String {
        return item!!.example
    }
    fun setTyping(msg: String) {
        binding?.appBar?.typing?.visibility = View.VISIBLE
        binding?.appBar?.typing?.text = "$msg is typing..."
        setUpTyping()
    }

    fun setUpTyping() {
        Observable.just(true).delay(3000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                binding?.appBar?.typing?.visibility = View.GONE
            }
            .subscribe().addTo(autodis!!)

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                binding?.appBar?.title?.text = "Chat Socket"
                addFragment(ChatFragment())
            }
            R.id.nav_gallery -> {
            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }
}
