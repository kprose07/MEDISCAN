package com.example.mediscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Data.Communicator
import com.example.mediscan.Data.NarrowDownSearch
import com.example.mediscan.R
import java.util.HashMap

class NarrowAdapter(private var narrow: List<NarrowDownSearch>,
                    private var popupCard: CardView?,
                    private var popupTitle: TextView,
                    private var popupDetail: TextView,
                    private var close: View,
                    private var seeMore: TextView,
                    communicator: Communicator,
                    private var medicineName: String,
                    private var brandName: String
) :
    RecyclerView.Adapter<NarrowAdapter.ViewHolder>() {

    private val comm:Communicator = communicator

    // TODO: add the rest of the medicines
    // TODO: what to look at for hash map
    private val NarrowImageMap: HashMap<String, Int> = hashMapOf(
        "Summary" to R.drawable.ic_reports,
        "Instructions" to R.drawable.ic_task,
        "Pediatrics" to R.drawable.ic_pediatrics,
        "Geriatrics" to R.drawable.ic_geriatrics,
        "Contradictions" to R.drawable.ic_doctortool,
        "Limitations of Use" to R.drawable.ic_limits,
        "Warnings and Precautions" to R.drawable.ic_warning,
        "Dosage" to R.drawable.ic_dosafe,
        "Administration" to R.drawable.ic_doctor,
        "Overdosage" to R.drawable.ic_overdosage,
        "Ortho" to R.drawable.ic_kneebone,
        "Pediatrics" to R.drawable.ic_pediatrics,


    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.nsce_title)
        val itemImage: ImageView = itemView.findViewById(R.id.nsc_image)
        //var open = true

        init {
            popupCard?.visibility = View.GONE
            itemView.setOnClickListener { v: View ->
                // TODO: Launch next screen for the medicines
                val position: Int = adapterPosition

                if( popupCard?.visibility == View.GONE) {
                    loadpopup()
                }
                close.setOnClickListener{
                    popupCard?.visibility = View.GONE

                }
                seeMore.setOnClickListener {
                    comm.openResultsPage(narrow, itemTitle.text.toString(), medicineName, brandName)
                }

            }
        }
        private fun loadpopup() {
                popupCard?.visibility = View.VISIBLE
                popupTitle.setText(itemTitle.text)
                popupDetail.setText(narrow[position].data)

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vn = LayoutInflater.from(parent.context).inflate(R.layout.narrow_card_layout, parent, false)
        return ViewHolder(vn)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = narrow[position].title
        // TODO: what to look at for hash map
        NarrowImageMap[narrow[position].title]?.let { holder.itemImage.setImageResource(it) }

    }


    override fun getItemCount(): Int {
        return narrow.size
    }

}