package com.example.mediscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Accordresults
import com.example.mediscan.R
import kotlinx.android.synthetic.main.fragment_results.*

class DescriptionAdapter(val descriptionList: List<Accordresults>) :
RecyclerView.Adapter<DescriptionAdapter.AccordianVH>(){
    class AccordianVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titletxt : TextView = itemView.findViewById(R.id.accordtitle)
        var descriptxt : TextView = itemView.findViewById(R.id.description)
       // var linearlayout : LinearLayout = itemView.findViewById(R.id.linear)
        var expandablelayout : RelativeLayout = itemView.findViewById(R.id.expandrect)
        var dropdown : View = itemView.findViewById(R.id.expand)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccordianVH {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return AccordianVH(view)
    }
    override fun getItemCount(): Int {
        return descriptionList.size
    }

    override fun onBindViewHolder(holder: AccordianVH, position: Int) {
      val accordresults : Accordresults = descriptionList[position]
      val switcha : Accordresults = descriptionList[position]
      val exa : Accordresults = descriptionList[position]


      holder.titletxt.text = accordresults.accordtitle
      holder.descriptxt.text = accordresults.description

        var isExpandable : Boolean = descriptionList[position].expandable
        val switchar : Boolean = descriptionList[position].expandable
        //val exall : Boolean = descriptionList[position].expandable

        if(switchar) holder.dropdown.setBackgroundResource(R.drawable.ic_uparrow) else  holder.dropdown.setBackgroundResource(R.drawable.ic_downarrow)
       // if(!exall) holder.all.setText(R.string.collapse_al) else holder.all.setText(R.string.expand_al)
        holder.expandablelayout.visibility = if(isExpandable) View.VISIBLE else View.GONE


        holder.dropdown.setOnClickListener{
            val accordresults = descriptionList[position]
            switcha.switcharr = !switcha.switcharr
            accordresults.expandable = !accordresults.expandable
            notifyItemChanged(position)
        }

    }


}