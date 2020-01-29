package com.liad.droptask.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.liad.droptask.R
import com.liad.droptask.extensions.changeFragment
import com.liad.droptask.fragments.ContactFragment
import com.liad.droptask.fragments.ReviewDropFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val contactFragment = ContactFragment.newInstance()
            changeFragment(
                supportFragmentManager,
                R.id.main_activity_frame_layout,
                contactFragment
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_bags -> {
                val reviewDropFragment = ReviewDropFragment.newInstance()
                val fragment = supportFragmentManager.findFragmentByTag(reviewDropFragment::class.java.simpleName)
                if (fragment == null) changeFragment(
                    supportFragmentManager,
                    R.id.main_activity_frame_layout,
                    reviewDropFragment,
                    true
                )
                else Toast.makeText(this, "You're already in this screen", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            MenuInflater(this).inflate(R.menu.menu_item, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack()
        else super.onBackPressed()

    }
}
