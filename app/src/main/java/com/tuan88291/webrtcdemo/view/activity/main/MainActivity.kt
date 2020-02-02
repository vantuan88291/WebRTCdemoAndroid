package com.tuan88291.webrtcdemo.view.activity.main

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.tuan88291.webrtcdemo.BaseActivity
import com.tuan88291.webrtcdemo.R
import com.tuan88291.webrtcdemo.databinding.ActivityMainBinding
import com.tuan88291.webrtcdemo.utils.Utils.startSocketService
import com.tuan88291.webrtcdemo.utils.Utils.stopSocketService
import com.tuan88291.webrtcdemo.utils.observe.AutoDisposable
import com.tuan88291.webrtcdemo.utils.observe.addTo
import com.tuan88291.webrtcdemo.utils.webrtc.RTCClient
import com.tuan88291.webrtcdemo.view.fragment.chat.ChatFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{

    var binding: ActivityMainBinding? = null
    private var autodis: AutoDisposable? =  null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startSocketService(this)
        autodis = AutoDisposable(this.lifecycle)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding?.appBar?.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding?.drawerLayout, binding?.appBar?.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding?.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        binding?.navView?.setNavigationItemSelectedListener(this)
        binding?.appBar?.title?.text = "Chat"
        addFragment(ChatFragment())
    }

    override fun onBackPressed() {
        if (binding?.drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                stopSocketService(this)
                Toast.makeText(this, "Logout success!", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                binding?.appBar?.title?.text = "Chat"
                addFragment(ChatFragment())
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
