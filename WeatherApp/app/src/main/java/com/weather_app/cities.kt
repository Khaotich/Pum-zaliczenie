package com.weather_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.navigation.Navigation

class cities : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cities, container, false)
        val listView =  view.findViewById<ListView>(R.id.list_view)
        val assets = context?.assets?.open("cities.csv")?.buffered()
        var cities = readCsv(assets!!)

        listView.setOnItemClickListener { _, _, position, _ ->
            val args = Bundle()

            args.putString("city", cities[position].city)
            args.putDouble("lon", cities[position].lon)
            args.putDouble("lat", cities[position].lat)

            Navigation.findNavController(view).navigate(R.id.weather, args)
        }

        val list = ArrayList<HashMap<String,Any>>()
        for(i in cities.indices)
        {
            val map = HashMap<String,Any>()
            map["names"] = cities[i].city
            list.add(map)
        }

        val from = arrayOf("names", "dates", "img")
        val to = intArrayOf(R.id.name)
        val adapter = SimpleAdapter(this.context, list, R.layout.item, from, to)
        listView.adapter = adapter

        return view
    }
}