package com.mohit.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mohit.bookhub.R
import com.mohit.bookhub.adapter.DashboardRecyclerAdapter
import com.mohit.bookhub.model.Book
import com.mohit.bookhub.util.ConnectionManager
import java.util.concurrent.CopyOnWriteArrayList

class DashboardFragment : Fragment() {
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnCheckInternet: Button
   /* val booklist= arrayListOf(
        "maze runner",
        "flamingo",
        "Lolita",
        "two states",
        "harry potter",
        "around the world in 90 days",
        "the lord of the rings",
        "war and peace",
        "alice and the wonderland",
        "mohit malviya the great"

    )*/
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    val bookInfoList= arrayListOf<Book>(/*
        Book("maze runner","mohit malviya","599","4.5") ,
        Book("flamingo","mahima malviya","5949","4.8"),
        Book("lolita","saroj malviya","5994","4.5"),
        Book("two states","mukesh malviya","5599","4.5"),
        Book("harry potter","ajay malviya","5939","4.5"),
        Book("around the world in 90 days","abhi malviya","5699","4.5"),
        Book("the lord of the rings","chirag malviya","5499","4.5"),
        Book("war and peace","shahbaaz malviya","5499","4.5"),
        Book("alice and the wonderland","rahul malviya","54399","3.5"),
        Book("mohit malviya the great","rohit malviya","59339","5.0")
   */ )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_dashboard, container, false)
        btnCheckInternet=view.findViewById(R.id.btnCheckInternet)
        btnCheckInternet.setOnClickListener {
            if (ConnectionManager().checkConnectivity(activity as Context)){
                //
                val dialog=AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet connection found")
                dialog.setPositiveButton("ok"){text, listener ->}
                dialog.setNegativeButton("cancel"){text , listener ->}
                dialog.create()
                dialog.show()
            }
            else{
                //
                val dialog=AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Internet connection  not found")
                dialog.setPositiveButton("open settings"){text, listener ->
                    val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    activity?.finish()
                }
                dialog.setNegativeButton("exit"){text , listener ->
                    ActivityCompat.finishAffinity(activity as Activity)
                }
                dialog.create()
                dialog.show()
            }
        }
        recyclerDashboard=view.findViewById(R.id.recyclerDashboard)
        layoutManager=LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)
        val url= "http://13.235.250.119/v1/book/fetch_books/"
        if(ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    val success = it.getBoolean("success")
                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            bookInfoList.add(bookObject)
                            recyclerAdapter =
                                DashboardRecyclerAdapter(activity as Context, bookInfoList)
                            recyclerDashboard.adapter = recyclerAdapter
                            recyclerDashboard.layoutManager = layoutManager
                            recyclerDashboard.addItemDecoration(
                                DividerItemDecoration(
                                    recyclerDashboard.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            )
                        }

                    } else {
                        Toast.makeText(
                            activity as Context,
                            "some error occured!!!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, Response.ErrorListener {
                    println("error is $it")

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }
        else{
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection  not found")
            dialog.setPositiveButton("open settings"){text, listener ->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("exit"){text , listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }
        return view
    }

}
