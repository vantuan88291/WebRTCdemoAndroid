package com.tuan88291.mvvmpattern

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseActivity : AppCompatActivity(), BaseView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    protected fun addFragment(fragment: Fragment?) {
        if (fragment != null) {
            try {
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.contentHome, fragment, "tag")
                    ft.commit()
            } catch (e: Exception) {

            }
        }
    }

    override fun setErrorParent(data: Any) {

    }
}
