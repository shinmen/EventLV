package com.link_value.eventlv.View.Create

import android.Manifest
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.transition.Explode
import android.widget.SeekBar
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.link_value.eventlv.View.Common.DatePickerDialogFragment
import com.link_value.eventlv.View.Common.TimePickerDialogFragment
import com.link_value.eventlv.Model.Event.PostDateEvent
import com.link_value.eventlv.Model.Event.PostTimeEvent
import com.link_value.eventlv.Infrastructure.LocationApi.FetchUserLocation
import com.link_value.eventlv.Presenter.CreatePresenter.CreateEventPresenterImpl
import com.link_value.eventlv.R
import kotlinx.android.synthetic.main.activity_new_event_lv.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*
import org.greenrobot.eventbus.Subscribe
import com.google.android.gms.location.places.Places
import com.google.android.gms.common.api.GoogleApiClient
import com.link_value.eventlv.Infrastructure.LocationApi.AutocompleteAddress
import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Model.Partner
import com.link_value.eventlv.Presenter.CreatePresenter.CreateEventPresenter
import com.link_value.eventlv.R.id.toolbar
import com.link_value.eventlv.Repository.Create.NewEventRepositoryImpl
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.text.DateFormat
import java.util.Calendar.*

class NewEventLvActivity : AppCompatActivity(),
        CreateEventView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private lateinit var adapter: AutoCompleteAddressAdapter
    private lateinit var googleApiClient: GoogleApiClient

    private var mDuration: Int? = null
    private var mEventName: String? = null
    private var mAddress: String? = null
    private var mLocationName: String? = null
    private var mStartedDate = Calendar.getInstance()
    private lateinit var mPresenter: CreateEventPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_event_lv)
        EventBus.getDefault().register(this)
        window.allowEnterTransitionOverlap = true
        val explode = Explode()
        explode.excludeTarget(toolbar, true)
        window.enterTransition = explode

        askForUserLocation()
        val repo = NewEventRepositoryImpl(HttpClient())
        mPresenter = CreateEventPresenterImpl(this@NewEventLvActivity, repo)

        event_date.onClick {
            val datePickerFragment = DatePickerDialogFragment.newInstance(Date())
            datePickerFragment.show(supportFragmentManager, "date")
        }

        event_time.onClick {
            val timePickerFragment = TimePickerDialogFragment.newInstance(Date())
            timePickerFragment.show(supportFragmentManager, "time")
        }

        input_duration.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                event_duration.text = resources.getString(R.string.duration_input_value, progress)
                mDuration = progress
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        propose_event.onClick(UI) {
            val loggedInUser = Partner.mockCurrentUser()
            mAddress = input_address.text.toString()
            mEventName = input_name.text.toString()
            mLocationName = input_location_name.text.toString()
            val proposedEvent = EventLV(
                    mEventName!!,
                    mStartedDate.time,
                    mDuration,
                    mLocationName!!,
                    mAddress!!,
                    loggedInUser,
                    emptyList()
            )
            mPresenter.persistEventLv(proposedEvent)
        }

        googleApiClient = GoogleApiClient.Builder(this@NewEventLvActivity)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .build()

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
    fun onDatePicked(event: PostDateEvent) {
        val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE)
        input_date.text = df.format(event.date)
        val startedDate = Calendar.getInstance()
        startedDate.time = event.date
        mStartedDate.set(startedDate.get(YEAR), startedDate.get(MONTH), startedDate.get(DAY_OF_MONTH))
    }

    @Subscribe()
    fun onTimePicked(event: PostTimeEvent) {
        val df = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE)
        input_time.text = df.format(event.date)
        val startedTime = Calendar.getInstance()
        startedTime.time = event.date
        mStartedDate.set(HOUR_OF_DAY, startedTime.get(HOUR_OF_DAY))
        mStartedDate.set(MINUTE, startedTime.get(MINUTE))
    }


    override fun onEventPersisted() {
        Toast.makeText(this@NewEventLvActivity, "event créé", Toast.LENGTH_SHORT)
    }

    override fun onError(message: String?) {
        Toast.makeText(this@NewEventLvActivity, "Désolé, une erreur est survenue lors de l'enregistrement de ", Toast.LENGTH_LONG)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this@NewEventLvActivity, "impossible de se connecter, l'autocomplétion de l'adresses n'est pas disponible", Toast.LENGTH_SHORT).show()
    }

    override fun onConnected(p0: Bundle?) {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val autoComplete = AutocompleteAddress(googleApiClient)
        adapter = AutoCompleteAddressAdapter(this@NewEventLvActivity, android.R.layout.simple_list_item_1)
        event_duration.text = resources.getString(R.string.duration_input_value, 1)
        input_address.setAdapter(adapter)
        input_address.threshold = 3
        input_address.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (query.toString().length >= 4) {
                    val userLocation = FetchUserLocation(locationManager)

                    launch(UI) {
                        val location = userLocation.fetchLocation()
                        adapter.update(autoComplete.getPredictions(query.toString(), location))
                    }
                }
            }
        })
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(this@NewEventLvActivity, "impossible de se connecter", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(
                            this@NewEventLvActivity,
                            "Merci, votre position sera utilisée pour l'autocomplete d'adresse",
                            Toast.LENGTH_SHORT
                    ).show()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(
                            this@NewEventLvActivity,
                            "Les bureaux d'LV Paris sont pris en compte comme position d'origine de l'autocomplete d'adresse",
                            Toast.LENGTH_SHORT
                    ).show()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    companion object {
        const val PERMISSION_REQUEST_LOCATION = 0
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, NewEventLvActivity::class.java)
        }
    }
}
