package com.example.mediscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Data.Medicine
import com.example.mediscan.R
import java.util.HashMap


class RecyclerAdapter(private var medicines: List<Medicine>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    // TODO: add the rest of the medicines
    // TODO: what to look at for hash map
    private val imageMap: HashMap<String, Int> = hashMapOf(
        "adcirca" to R.drawable.adcirca,
        "alimta" to R.drawable.alimta,
        "amyvid" to R.drawable.avyvid,
        "baqsimi" to R.drawable.baqsimi,
        "baricitinib" to R.drawable.baricitinib,
        "basaglar" to R.drawable.basaglar,
        "bebtelovimab" to R.drawable.bebtelovimab,
        "cyramza" to R.drawable.cyramza,
        "emgality" to R.drawable.emgality,
        "erbitux" to R.drawable.erbitux,
        "forteo" to R.drawable.forteo,
        "glucagon" to R.drawable.glucagon,
        "glyxambi" to R.drawable.glyxambi,
        "humalog" to R.drawable.humalog,
        "humalogfifty" to R.drawable.humalogfifty,
        "humalogjunior" to R.drawable.humalogjunior,
        "humalogsev" to R.drawable.humalogsev,
        "humatrope" to R.drawable.humatrope,
        "humulinn" to R.drawable.humulinn,
        "humulinr" to R.drawable.humulinr,
        "humulinru" to R.drawable.humulinr,
        "humulinsev" to R.drawable.humulinsev,
        "insulinli" to R.drawable.insulinli,
        "insulinlik" to R.drawable.insulinlik,
        "inslinlli" to R.drawable.insulinlli,
        "jardiance" to R.drawable.jardiance,
        "jaypirca" to R.drawable.jaypirca,
        "jentadueto" to R.drawable.jentadueto,
        "logom" to R.drawable.logom,
        "lyumje" to R.drawable.lyumje,
        "mounjaro" to R.drawable.mounjaro,
        "olumiantc" to R.drawable.olumiantc,
        "retevmo" to R.drawable.retevmo,
        "reyvow" to R.drawable.reyvow,
        "synjardy" to R.drawable.synjardy,
        "synjardyxr" to R.drawable.synjardyxr,
        "taltz" to R.drawable.taltz,
        "tauvid" to R.drawable.tauvid,
        "tradjenta" to R.drawable.tradjenta,
        "trijardyxr" to R.drawable.trijardyxr,
        "trulicity" to R.drawable.trulicity,
        "verzenio" to R.drawable.verzenio,
        "zyprexa" to R.drawable.zyprexa
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.md_title)
        val itemImage: ImageView = itemView.findViewById(R.id.md_image)

        init {
            itemView.setOnClickListener { v: View ->
                // TODO: Launch next screen for the medicines
                val position: Int = adapterPosition
                Toast.makeText(
                    itemView.context,
                    "You clicked on item # ${position + 1}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.medicine_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = medicines[position].name
        // TODO: what to look at for hash map
        imageMap[medicines[position].name]?.let { holder.itemImage.setImageResource(it) }
    }

    override fun getItemCount(): Int {
        return medicines.size
    }

}