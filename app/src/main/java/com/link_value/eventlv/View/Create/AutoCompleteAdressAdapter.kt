package com.link_value.eventlv.View.Create

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.link_value.eventlv.Model.AddressEventLV
import com.link_value.eventlv.Infrastructure.LocationApi.AutocompleteAddress
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*


/**
 * Created by julo on 30/12/17.
 */
class AutoCompleteAddressAdapter(context: Context, resource: Int) : ArrayAdapter<String>(context, resource),
        Filterable
{
    private var addresses = ArrayList<String>()


    fun update(list: ArrayList<String>) {
        addresses = list
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                    // Assign the data to the FilterResults
                    filterResults.values = addresses
                    filterResults.count = addresses.size

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }

    override fun getCount(): Int {
        return addresses.size
    }

    override fun getItem(position: Int): String {
        return addresses[position]
    }
}