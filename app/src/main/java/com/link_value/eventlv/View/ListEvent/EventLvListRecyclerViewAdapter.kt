package com.link_value.eventlv.View.ListEvent

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.link_value.eventlv.Model.Event.DisplayEventLvDetail
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.R
import com.squareup.picasso.Picasso
import org.greenrobot.eventbus.EventBus
import java.text.DateFormat
import java.util.*

class EventLvListRecyclerViewAdapter(
        private val listener: EventLvFragment.ListListener,
        private val context: Context,
        private var mValues: MutableList<EventLV>
    ) : RecyclerView.Adapter<EventLvListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_eventlv, parent, false)
        return ViewHolder(view)
    }

    fun loadEvents(events: MutableList<EventLV>) {
        mValues = events
        notifyDataSetChanged()
    }

    fun updateEvent(event: EventLV) {
        mValues.map {
            if (it.uuid == event.uuid) {
                val index = mValues.indexOf(it)
                mValues[index] = event
                notifyItemChanged(index)
            }
        }
    }

    fun addEvent(event: EventLV) {
        mValues.add(event)
        val index = mValues.indexOf(event)
        notifyItemInserted(index)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = mValues[position]
        Picasso.with(context)
                .load(event.locationStreetPictureUrl)
                .fit()
                .centerCrop()
                .error(android.R.drawable.stat_notify_error)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.mLocationPictureView)
        holder.mLocationName.text = context.resources.getString(R.string.meet_at, event.locationName)
        holder.mTitleView.text = event.title
        holder.mCategoryView.text = event.category.toString()
        val startedAt = event.startedAt
        val date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault()).format(startedAt)
        holder.mStartTimeView.text = context.resources.getString(R.string.start_at, date)
        holder.mParticipantsNbView.text = context.resources.getString(R.string.participants_nb, event.participants.size.toString())

        holder.mView.setOnClickListener {
            listener.onDisplayDetail(it, event)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mLocationPictureView: ImageView = mView.findViewById(R.id.location_img)
        val mCategoryView: TextView = mView.findViewById(R.id.category)
        val mLocationName: TextView = mView.findViewById(R.id.location_name)
        val mTitleView: TextView = mView.findViewById(R.id.title)
        val mStartTimeView: TextView = mView.findViewById(R.id.start_time)
        val mParticipantsNbView: TextView = mView.findViewById(R.id.participant_nb)

        override fun toString(): String {
            return super.toString() + " '" + mTitleView.text + "'"
        }
    }
}
