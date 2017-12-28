package com.link_value.eventlv.View.Create

import android.content.Context
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
class AutoCompleteAddressAdapter(context: Context, resource: Int, addresses: MutableList<String>) : ArrayAdapter<String>(context, resource, addresses),
        Filterable
{
    private var autoCompleteAddress: AutocompleteAddress? = null
    private var addresses = ArrayList<String>()


    fun update(list: ArrayList<String>) {
        addresses = list
        notifyDataSetChanged()
    }

    /*constructor(context: Context, resource: Int, autoCompleteAddress: AutocompleteAddress) : this(context, resource) {
        this.autoCompleteAddress = autoCompleteAddress
    }*/

/*    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                if (constraint != null) {
                    launch(UI) {
                        autoCompleteAddress!!.getPredictions(constraint.toString())
                    }

                    // Retrieve the autocomplete results.
                    //resultList = autocomplete(constraint.toString())

                    // Assign the data to the FilterResults
                    //filterResults.values = resultList
                    //filterResults.count = resultList.size()
                }

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
    }*/

    override fun getCount(): Int {
        return addresses.size
    }

    override fun getItem(position: Int): String {
        return addresses.get(position).toString()
    }
}