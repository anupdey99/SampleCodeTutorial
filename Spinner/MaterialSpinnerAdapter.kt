package com.a2i.apps.emcrp.utils

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class MaterialSpinnerAdapter<String>(context: Context, layout: Int, var values: List<String>): ArrayAdapter<String>(context, layout, values) {

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            results.values = values
            results.count = values.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }


    }

}