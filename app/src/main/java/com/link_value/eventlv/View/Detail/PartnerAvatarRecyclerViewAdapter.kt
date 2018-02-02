package com.link_value.eventlv.View.Detail

import android.content.Context
import com.link_value.eventlv.Model.Partner
import android.support.v7.widget.RecyclerView
import com.squareup.picasso.Picasso
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.link_value.eventlv.Model.MockLoggedInPartner
import com.link_value.eventlv.R
import com.link_value.eventlv.View.Detail.PartnerAvatarRecyclerViewAdapter.ViewHolder


/**
 * Created by julienb on 24/01/18.
 */
class PartnerAvatarRecyclerViewAdapter
    (private val context: Context, private val participants: MutableList<Partner>)
    : RecyclerView.Adapter<ViewHolder>() {

    private var itemCount: Int = 0

    init {
        itemCount = participants.size
    }

    fun replaceParticipant(participant: Partner) {
        val index = participants.indexOf(participant)
        participants[index] = participant
        notifyItemChanged(index)
    }

    fun addParticipant(loggedInPartner: Partner) {
        participants.add(0, loggedInPartner)
        //itemCount = participants.size
        notifyItemInserted(0)
        //notifyItemRangeInserted(position, participants.size)
    }

    fun removeParticipant(loggedInPartner: Partner) {
        //itemCount = participants.size
        participants.removeAt(0)
        notifyItemRemoved(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_partner_avatar, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val participant = participants[position]
        holder.partner = participant
        Picasso.with(context)
                .load(participant.avatarUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .fit()
                .into(holder.avatarView)
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var partner: Partner
        var avatarView: ImageView = view.findViewById(R.id.partner_avatar)

        override fun toString(): String {
            return partner.username
        }
    }
}
