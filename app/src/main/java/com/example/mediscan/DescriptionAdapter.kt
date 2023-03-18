package com.example.mediscan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DescriptionAdapter(val descriptionList: List<Accordresults>) :
    RecyclerView.Adapter<DescriptionAdapter.AccordianVH>(){
    class AccordianVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titletxt : TextView = itemView.findViewById(R.id.accordtitle)
        var descriptxt : TextView = itemView.findViewById(R.id.description)
        var linearlayout : LinearLayout = itemView.findViewById(R.id.linear)
        var expandablelayout : RelativeLayout = itemView.findViewById(R.id.expand)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccordianVH {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return AccordianVH(view)
    }
    override fun getItemCount(): Int {
        return descriptionList.size
    }

    override fun onBindViewHolder(holder: AccordianVH , position: Int) {
      val accordresults : Accordresults = descriptionList[position]
      holder.titletxt.text = accordresults.accordtitle
      holder.descriptxt.text = accordresults.description

        val isExpandable : Boolean = descriptionList[position].expandable
        holder.expandablelayout.visibility = if(isExpandable) View.VISIBLE else View.GONE

        holder.linearlayout.setOnClickListener{
            val accordresults = descriptionList[position]
            accordresults.expandable = !accordresults.expandable
            notifyItemChanged(position)
        }

    }

}