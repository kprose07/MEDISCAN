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
        "Description" to R.drawable.ic_reports,
        "Indications and Usage" to R.drawable.ic_task,
        "Dosage and Administration" to R.drawable.ic_doctor,
        "Dosage Forms and Strengths" to R.drawable.ic_dosafe,
        "Contraindications" to R.drawable.ic_contradiction,
        "Warnings and Precautions" to R.drawable.ic_warning,
        "Adverse Reactions" to R.drawable.ic_reaction,
        "Drug Interactions" to R.drawable.ic_iteract,
        "Use in Specific Populations" to R.drawable.ic_doctor,
        "Overdosage" to R.drawable.ic_popu,
        "Clinical Pharmacology" to R.drawable.ic_pharma,
        "Nonclinical Toxicology" to R.drawable.ic_toxic,
        "Clinical Studies" to R.drawable.ic_doctortool,
        "How Supplied/Storage and Handling" to R.drawable.ic_limits,
        "Patient Counseling Information" to R.drawable.ic_counsel

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