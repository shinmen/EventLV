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

class EventLvListRecyclerViewAdapter(
        private val context: Context,
        private var mValues: List<EventLV>
    ) : RecyclerView.Adapter<EventLvListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_eventlv, parent, false)
        return ViewHolder(view)
    }

    fun loadEvents(events: List<EventLV>) {
        mValues = events
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = mValues[position]
        Picasso.with(context)
                .load(event.locationStreetPictureUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_menu_gallery)
                .into(holder.mLocationPictureView)
        holder.mLocationName.text = event.locationName
        holder.mTitleView.text = event.title
        holder.mCategoryView.text = event.category
        val startedAt = event.startedAt
        val date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(startedAt)
        holder.mStartTimeView.text = date

        holder.mView.setOnClickListener {
            EventBus.getDefault().post(DisplayEventLvDetail(event))
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

        override fun toString(): String {
            return super.toString() + " '" + mTitleView.text + "'"
        }
    }
}
