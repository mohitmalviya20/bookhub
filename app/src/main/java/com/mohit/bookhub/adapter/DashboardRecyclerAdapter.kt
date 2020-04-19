package com.mohit.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mohit.bookhub.R
import com.mohit.bookhub.activity.DescriptionActivity
import com.mohit.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter (val context:Context,val itemList: ArrayList<Book>): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){
    class  DashboardViewHolder(view:View):RecyclerView.ViewHolder(view){
        val textView: TextView =view.findViewById(R.id.txtRecyclerRowItem)
        val textView1:TextView= view.findViewById(R.id.txtRecyclerRowItem1)
        val textView2:TextView= view.findViewById(R.id.txtRecyclerRowItem2)
        val textView4:TextView= view.findViewById(R.id.txtRecyclerRowItem4)
        val imgBookImage:ImageView=view.findViewById(R.id.imageView)
        val llcontent:RelativeLayout=view.findViewById(R.id.llcontent)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
    val  view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
    return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book=itemList[position]
        holder.textView.text=book.bookName
        holder.textView1.text=book.bookAuthor
        holder.textView2.text=book.bookPrice
        holder.textView4.text=book.bookRating
        //holder.imgBookImage.setImageResource(book.book Image)
        Picasso.get().load(book.bookImage).into(holder.imgBookImage)
        holder.llcontent.setOnClickListener {
            val intent=Intent(context , DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }

    }
}