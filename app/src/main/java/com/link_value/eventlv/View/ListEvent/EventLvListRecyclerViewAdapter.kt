package com.link_value.eventlv.View.ListEvent

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.R
import com.link_value.eventlv.View.ListEvent.EventLvFragment.OnListFragmentInteractionListener
import com.squareup.picasso.Picasso

class EventLvListRecyclerViewAdapter(private val context: Context, private var mValues: List<EventLV>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<EventLvListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_eventlv, parent, false)
        return ViewHolder(view)
    }

    fun loadEvents(events: List<EventLV>) {
        mValues = events
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = mValues.get(position)
        Picasso.with(context)
                .load(event.locationStreetPictureUrl)
                .resize(50, 50)
                .centerCrop()
                .placeholder(R.drawable.ic_menu_gallery)
                .into(holder.mLocationPictureView)
        holder.mLocationName.text = event.locationName
        holder.mTitleView.text = event.title
        holder.mStartTimeView.text = event.startedAt.toString()

        holder.mView.setOnClickListener {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener!!.onListFragmentInteraction(event)
            }
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mLocationPictureView: ImageView = mView.findViewById(R.id.location_img)
        val mLocationName: TextView = mView.findViewById(R.id.location_name)
        val mTitleView: TextView = mView.findViewById(R.id.title)
        val mStartTimeView: TextView = mView.findViewById(R.id.start_time)

        override fun toString(): String {
            return super.toString() + " '" + mTitleView.getText() + "'"
        }
    }
}
