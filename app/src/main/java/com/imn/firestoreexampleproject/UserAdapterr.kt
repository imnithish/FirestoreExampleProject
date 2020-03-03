package com.imn.firestoreexampleproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView


class UserAdapterr(val arrayList: ArrayList<User>) :
        RecyclerView.Adapter<UserAdapterr.UserHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.First.text = arrayList[position].firstName
        holder.Last.text = arrayList[position].lastName
    }

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val First = itemView.findViewById<TextView>(R.id.first)
        val Last = itemView.findViewById<TextView>(R.id.last)
        val Ll = itemView.findViewById<LinearLayout>(R.id.ll)


    }

}
