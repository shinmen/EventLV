package com.link_value.eventlv.View.Detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.link_value.eventlv.Model.EventLV

import com.link_value.eventlv.R
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment(),
        OnMapReadyCallback
{
    private lateinit var mEventDetail: EventLV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (arguments != null) {
            mEventDetail = arguments!!.getParcelable(MapFragment.EVENT_LV) as EventLV
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val mapView = view.findViewById<MapView>(R.id.map)
        mapView.onCreate(savedInstanceState)

        mapView.onResume() // needed to get the map to display immediately
        mapView.getMapAsync(this)

        return view
    }

    override fun onMapReady(map: GoogleMap?) {
        val marker = MarkerOptions()
        if (mEventDetail.coordinates != null) {
            marker.title(mEventDetail.locationName)
            val eventLatLng = LatLng(mEventDetail.coordinates!!.lat, mEventDetail.coordinates!!.lng)
            marker.position(eventLatLng)
            map?.addMarker(marker)
            map?.moveCamera(CameraUpdateFactory.zoomTo(12.toFloat()))
            map?.moveCamera(CameraUpdateFactory.newLatLng(eventLatLng))
        }
    }

    companion object {
        private const val EVENT_LV = "map_event_fragment"

        fun newInstance(eventLv: EventLV): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()
            args.putParcelable(EVENT_LV, eventLv)
            fragment.arguments = args

            return fragment
        }

    }

}
