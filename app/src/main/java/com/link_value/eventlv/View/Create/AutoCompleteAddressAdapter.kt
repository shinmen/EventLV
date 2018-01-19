package com.link_value.eventlv.View.Create

import android.content.Context
import android.util.Log
import android.widget.*
import com.link_value.eventlv.Model.AddressEventLV
import com.link_value.eventlv.Infrastructure.LocationApi.AutocompleteAddress
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*


/**
 * Created by julo on 30/12/17.
 */
class AutoCompleteAddressAdapter(context: Context, resource: Int) : ArrayAdapter<AddressEventLV>(context, resource),
        Filterable
{

    private var addresses = ArrayList<AddressEventLV>()


    fun update(list: ArrayList<AddressEventLV>) {
        addresses = list
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return addresses.size
    }

    override fun getItem(position: Int): AddressEventLV {
        return  addresses[position]
    }
}