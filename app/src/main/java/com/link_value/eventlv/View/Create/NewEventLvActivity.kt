package com.link_value.eventlv.View.Create

import android.Manifest
import android.app.DialogFragment
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.link_value.eventlv.Common.DatePickerDialogFragment
import com.link_value.eventlv.Common.TimePickerDialogFragment
import com.link_value.eventlv.Event.PostDateEvent
import com.link_value.eventlv.Event.PostTimeEvent
import com.link_value.eventlv.Infrastructure.LocationApi.FetchUserLocation
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.CreatePresenter.CreateEventPresenterImpl
import com.link_value.eventlv.R
import kotlinx.android.synthetic.main.activity_new_event_lv.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*
import org.greenrobot.eventbus.Subscribe
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.common.api.GoogleApiClient
import com.link_value.eventlv.Infrastructure.LocationApi.AutocompleteAddress
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlin.collections.ArrayList


class NewEventLvActivity : AppCompatActivity(),
        CreateEventView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this@NewEventLvActivity, "impossible de se connecter", Toast.LENGTH_SHORT).show()
    }

    private lateinit var googleApiClient: GoogleApiClient

    override fun onConnected(p0: Bundle?) {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val autoComplete = AutocompleteAddress(FetchUserLocation(locationManager), googleApiClient)
        val adapter = AutoCompleteAddressAdapter(this@NewEventLvActivity, android.R.layout.simple_list_item_1)

        input_address.setAdapter(adapter)
        input_address.threshold = 4
        input_address.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (query.toString().length >= 4) {
                    Toast.makeText(this@NewEventLvActivity, query.toString(), Toast.LENGTH_SHORT).show()
                    launch (UI) {
                        try {
                            val predictions = autoComplete.getPredictions(query.toString())
                            adapter.update(predictions)
                            adapter.filter.filter(query)
                        } catch (e: Exception) {
                            Toast.makeText(this@NewEventLvActivity, e.javaClass.simpleName + e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(this@NewEventLvActivity, "impossible de se connecter", Toast.LENGTH_SHORT).show()

    }

    override fun onEventPersisted() {
    }

    override fun onError(message: String?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event_lv)
        EventBus.getDefault().register(this)

        askForUserLocation()
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val presenter = CreateEventPresenterImpl(this@NewEventLvActivity)

        event_date.onClick {
            val datePickerFragment = DatePickerDialogFragment.newInstance(Date())
            datePickerFragment.show(supportFragmentManager, "date")
        }

        event_time.onClick {
            val timePickerFragment = TimePickerDialogFragment.newInstance(Date())
            timePickerFragment.show(supportFragmentManager, "time")
        }

        googleApiClient = GoogleApiClient.Builder(this@NewEventLvActivity)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)

                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                //.addOnConnectionFailedListener(this)
                .build()
        googleApiClient.disconnect()

    }

    override fun onStart() {
        googleApiClient.connect()
        super.onStart()
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onStop() {
        googleApiClient.disconnect()
        super.onStop()
    }

    @Subscribe()
    fun onDatePicked(date: PostDateEvent) {
        input_date.text = date.toString()
    }

    @Subscribe()
    fun onTimePicked(date: PostTimeEvent) {
        input_time.text = date.toString()
    }

    private fun askForUserLocation() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this@NewEventLvActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this@NewEventLvActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSION_REQUEST_LOCATION)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@NewEventLvActivity, "thks", Toast.LENGTH_SHORT).show()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this@NewEventLvActivity, "too bad", Toast.LENGTH_SHORT).show()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request

    }

    companion object {
        val PERMISSION_REQUEST_LOCATION = 0
        fun newIntent(packageContext: Context): Intent {
            val intent = Intent(packageContext, NewEventLvActivity::class.java)

            return intent
        }
    }
}
