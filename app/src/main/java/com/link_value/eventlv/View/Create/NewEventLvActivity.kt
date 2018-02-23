package com.link_value.eventlv.View.Create

import android.Manifest
import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.transition.Explode
import android.view.View
import android.widget.*
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
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.link_value.eventlv.Infrastructure.LocationApi.AutocompleteAddress
import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Model.Category
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Model.EventLocationLatLng
import com.link_value.eventlv.Model.Partner
import com.link_value.eventlv.Presenter.CreatePresenter.CreateEventPresenter
import com.link_value.eventlv.Repository.Create.NewEventRepositoryImpl
import com.link_value.eventlv.Repository.List.ListCategoryRepositoryImpl
import com.link_value.eventlv.View.ListEvent.ListCategoryView
import com.link_value.eventlv.View.ListEvent.MainActivity
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.text.DateFormat
import java.util.Calendar.*

class NewEventLvActivity : AppCompatActivity(),
        CreateEventView,
        ListCategoryView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private lateinit var adapter: AutoCompleteAddressAdapter
    private lateinit var googleApiClient: GoogleApiClient
    private var mDuration: Int = 1
    private var mEventName: String? = null
    private var mAddress: String? = null
    private var mLocationName: String? = null
    private var mStartedDate = Calendar.getInstance()
    private var mCategory: Category? = null

    private var mLatLng: EventLocationLatLng? = null
    private lateinit var mPresenter: CreateEventPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_event_lv)
        EventBus.getDefault().register(this)
        val explode = Explode()
        window.enterTransition = explode
        window.exitTransition = explode

        askForUserLocation()
        val newRepo = NewEventRepositoryImpl(HttpClient())
        val categoriesRepo = ListCategoryRepositoryImpl(HttpClient())
        mPresenter = CreateEventPresenterImpl(
                this@NewEventLvActivity,
                this@NewEventLvActivity,
                newRepo,
                categoriesRepo
        )
        mPresenter.start()

        displayDatePicker()
        displayTimePicker()
        updateDurationOnChange()
        updateCategoryOnChange()
        onClickedSave()

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

    private fun onClickedSave() {
        propose_event.onClick(UI) {
            val isValid = validateEventLv()
            if (isValid) {
                val ev = saveEventLv()
                val i = MainActivity.newIntent(this@NewEventLvActivity, ev)
                setResult(Activity.RESULT_OK, i)
                finish()
            }
        }
    }

    private fun saveEventLv(): EventLV {
        val loggedInUser = Partner.mockCurrentUser()
        mEventName = input_name.text.toString()
        mLocationName = input_location_name.text.toString()
        mCategory = categories_spinner.selectedItem as Category?
        val proposedEvent = EventLV(
                mEventName!!,
                mCategory!!,
                mStartedDate.time,
                mDuration,
                mLocationName!!,
                mAddress!!,
                loggedInUser,
                mutableListOf(),
                mLatLng
        )
        mPresenter.persistEventLv(proposedEvent)

        return proposedEvent
    }

    private fun validateEventLv(): Boolean {
        var isValid = true
        val formFields = listOf<View>(input_name, categories_spinner, input_address, input_location_name, input_date, input_time, event_duration)
        formFields.forEach {
            when(it) {
                is EditText -> {
                    if (it.text.isEmpty()) {
                        it.error = getString(R.string.required_field_error_message)
                        isValid = false
                    }
                }
                is TextView -> {
                    if (it.text.isEmpty()) {
                        it.error = getString(R.string.required_field_error_message)
                        isValid = false
                    }
                }
                is Spinner -> {
                    if (it.selectedItem == null) {
                        Toast.makeText(this@NewEventLvActivity, getString(R.string.form_fiels_all_required_message), Toast.LENGTH_LONG).show()
                        isValid = false
                    }
                }
            }
        }

        return isValid
    }

    private fun displayDatePicker() {
        event_date.onClick {
            val datePickerFragment = DatePickerDialogFragment.newInstance(Date())
            datePickerFragment.show(supportFragmentManager, "date")
        }
    }

    @Subscribe()
    fun onDatePicked(event: PostDateEvent) {
        val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE)
        input_date.text = df.format(event.date)
        val startedDate = Calendar.getInstance()
        startedDate.time = event.date
        mStartedDate.set(startedDate.get(YEAR), startedDate.get(MONTH), startedDate.get(DAY_OF_MONTH))
    }

    private fun displayTimePicker() {
        event_time.onClick {
            val timePickerFragment = TimePickerDialogFragment.newInstance(Date())
            timePickerFragment.show(supportFragmentManager, "time")
        }
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

    private fun updateDurationOnChange() {
        input_duration.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                event_duration.text = resources.getString(R.string.duration_input_value, progress)
                mDuration = progress
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun updateCategoryOnChange() {
        categories_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(spinner: AdapterView<*>?) {
                spinner?.getItemAtPosition(0)
            }

            override fun onItemSelected(spinner: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                mCategory = spinner?.getItemAtPosition(pos) as Category?
            }
        }
    }


    override fun onEventPersisted() {
        Toast.makeText(this@NewEventLvActivity, getString(R.string.success_new), Toast.LENGTH_LONG).show()
    }

    override fun onCategorySelected(category: Category) {}


    override fun onError(message: String?) {
        Toast.makeText(this@NewEventLvActivity, getString(R.string.error_new), Toast.LENGTH_LONG).show()
    }

    override fun onCategoriesFetched(mapCategories: Map<String, Category>) {
        val categories = mapCategories.map { it.value }.toList()
        val categoriesAdapter = CategoryAdapter(this@NewEventLvActivity, android.R.layout.simple_spinner_item, categories)
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categories_spinner.adapter = categoriesAdapter
    }

    override fun onErrorCategoryFetch(error: String?) {
        Toast.makeText(this@NewEventLvActivity, getString(R.string.error_list_categories), Toast.LENGTH_LONG).show()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this@NewEventLvActivity, getString(R.string.error_google_api_connection), Toast.LENGTH_SHORT).show()
    }

    override fun onConnected(p0: Bundle?) {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val autoComplete = AutocompleteAddress(googleApiClient)
        adapter = AutoCompleteAddressAdapter(this@NewEventLvActivity, android.R.layout.simple_list_item_1)
        event_duration.text = resources.getString(R.string.duration_input_value, 1)
        input_address.setAdapter(adapter)
        //input_address.threshold = 4
        input_address.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (query.toString().length >= 4) {
                    val userLocation = FetchUserLocation(locationManager)

                    launch(UI) {
                        val location = userLocation.fetchLocation()
                        val addressList = autoComplete.getPredictions(query.toString(), location)
                        adapter.update(addressList)
                    }
                }
            }
        })

        input_address.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val address = adapter.getItem(position)
            input_location_name.text = Editable.Factory.getInstance().newEditable(address.name)
            launch(UI) {
                mAddress = autoComplete.getPreciseAddress(address).first
                val latLng = autoComplete.getPreciseAddress(address).second
                mLatLng = EventLocationLatLng(latLng.latitude, latLng.longitude)
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(this@NewEventLvActivity, getString(R.string.error_google_api_connection), Toast.LENGTH_SHORT).show()
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
                            getString(R.string.success_location_permission),
                            Toast.LENGTH_SHORT
                    ).show()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(
                            this@NewEventLvActivity,
                            getString(R.string.refuse_location_permission),
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
