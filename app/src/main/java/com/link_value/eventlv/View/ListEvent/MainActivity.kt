package com.link_value.eventlv.View.ListEvent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.link_value.eventlv.R
import com.link_value.eventlv.View.Create.NewEventLvActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.ActivityOptionsCompat
import android.transition.Explode
import android.view.View


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val listFragment = EventLvFragment.newInstance()
            val tabListFragment = ListStatusTabFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, tabListFragment)
                    .add(R.id.list_container, listFragment)
                    .commit()
        }

        onClickGoToNewEvent()

        window.allowEnterTransitionOverlap = true
        val explode = Explode()
        window.enterTransition = explode
        window.exitTransition = explode
    }

    private fun onClickGoToNewEvent() {
        fab.setOnClickListener { view ->
            val i = NewEventLvActivity.newIntent(this)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view as View, resources.getString(R.string.transition_add_btn))
            startActivity(i, options.toBundle())
        }
    }
}
