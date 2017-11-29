package com.link_value.eventlv.View.ListEvent

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.R
import com.link_value.eventlv.View.ListEvent.EventLvFragment.OnListFragmentInteractionListener

class EventLvListRecyclerViewAdapter(private val mValues: List<EventLV>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<EventLvListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_eventlv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mEvent = mValues.get(position)
        holder.mIdView.setText(mValues.get(position).title)
        holder.mContentView.setText(mValues.get(position).address)

        holder.mView.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener!!.onListFragmentInteraction(holder.mEvent!!)
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView
        val mContentView: TextView
        var mEvent: EventLV? = null

        init {
            mIdView = mView.findViewById(R.id.id)
            mContentView = mView.findViewById(R.id.content)
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView.getText() + "'"
        }
    }
}
