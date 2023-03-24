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
        "Adcirca" to R.drawable.adcirca,
        "Alimta" to R.drawable.alimta,
        "Amyvid" to R.drawable.avyvid,
        "Baqsimi" to R.drawable.baqsimi,
        "Baricitinib" to R.drawable.baricitinib,
        "Basaglar" to R.drawable.basaglar,
        "Bebtelovimab" to R.drawable.bebtelovimab,
        "Cyramza" to R.drawable.cyramza,
        "Emgality" to R.drawable.emgality,
        "Erbitux" to R.drawable.erbitux,
        "Forteo" to R.drawable.forteo,
        "Glucagon" to R.drawable.glucagon,
        "Glyxambi" to R.drawable.glyxambi,
        "Humalog" to R.drawable.humalog,
        "Humalog Fifty" to R.drawable.humalogfifty,
        "Humalog Junior" to R.drawable.humalogjunior,
        "Humalog 75/25" to R.drawable.humalogsev,
        "Humatrope" to R.drawable.humatrope,
        "Humulinn" to R.drawable.humulinn,
        "Humulin R" to R.drawable.humulinr,
        "Humulin R U-500" to R.drawable.humulinr,
        "Humulin 70/30" to R.drawable.humulinsev,
        "Insulin Lispro" to R.drawable.insulinli,
        "Insulin Lispro Kiwi" to R.drawable.insulinlik,
        "inslinlli" to R.drawable.insulinlli,
        "jardiance" to R.drawable.jardiance,
        "Jaypirca" to R.drawable.jaypirca,
        "Jentadueto" to R.drawable.jentadueto,
        "Logom" to R.drawable.logom,
        "Lyumje" to R.drawable.lyumje,
        "Mounjaro" to R.drawable.mounjaro,
        "Glumiantc" to R.drawable.olumiantc,
        "Retevmo" to R.drawable.retevmo,
        "Reyvow" to R.drawable.reyvow,
        "Synjardy" to R.drawable.synjardy,
        "Synjardyxr" to R.drawable.synjardyxr,
        "Taltz" to R.drawable.taltz,
        "Tauvid" to R.drawable.tauvid,
        "Tradjenta" to R.drawable.tradjenta,
        "Trijardyxr" to R.drawable.trijardyxr,
        "Trulicity" to R.drawable.trulicity,
        "Verzenio" to R.drawable.verzenio,
        "Zyprexa" to R.drawable.zyprexa
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