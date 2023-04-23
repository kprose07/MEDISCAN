package com.example.mediscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Data.Communicator
import com.example.mediscan.Data.Medicine
import com.example.mediscan.R


class RecyclerAdapter(private var medicines: List<Medicine>, communicator: Communicator) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val comm:Communicator = communicator

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
        "Humalog KwikPen" to R.drawable.humalogkwikpen,
        "Humalog mix 50/50" to R.drawable.humalogfifty,
        "Humalog Junior KwikPen" to R.drawable.humalogjunior,
        "Humalog mix 75/25" to R.drawable.humalogsev,
        "Humatrope" to R.drawable.humatrope,
        "Humulin N" to R.drawable.humulinn,
        "Humulin R" to R.drawable.humulinr,
        "Humulin R U-500" to R.drawable.humulinr,
        "Humulin 70/30" to R.drawable.humulinsev,
        "Insulin Lispro Junior KwikPen" to R.drawable.insulinli,
        "Insulin Lispro Protamine" to R.drawable.insulinlik,
        "Insulin Lispro" to R.drawable.insulinlli,
        "Jardiance" to R.drawable.jardiance,
        "Jaypirca" to R.drawable.jaypirca,
        "Jentadueto" to R.drawable.jentadueto,
        "Jentadueto XR" to R.drawable.jentaduetoxr,
        "Logom" to R.drawable.logom,
        "Lyumjev" to R.drawable.lyumje,
        "Mounjaro" to R.drawable.mounjaro,
        "Olumiant" to R.drawable.olumianti,
        "Olumiant (COVID-19)" to R.drawable.olumiantc,
        "Retevmo" to R.drawable.retevmo,
        "Reyvow" to R.drawable.reyvow,
        "Synjardy" to R.drawable.synjardy,
        "Synjardy XR" to R.drawable.synjardyxr,
        "Taltz" to R.drawable.taltz,
        "Tauvid" to R.drawable.tauvid,
        "Tradjenta" to R.drawable.tradjenta,
        "Trijardy XR" to R.drawable.trijardyxr,
        "Trulicity" to R.drawable.trulicity,
        "Verzenio" to R.drawable.verzenio,
        "Zyprexa" to R.drawable.zyprexa
    )
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.md_title)
        val itemImage: ImageView = itemView.findViewById(R.id.md_image)
        var medicineId = String()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.medicine_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = medicines[position].name
        // TODO: what to look at for hash map
        imageMap[medicines[position].name]?.let { holder.itemImage.setImageResource(it) }
        holder.medicineId = medicines[position].id
        holder.itemView.setOnClickListener { v: View ->
            // TODO: Launch next screen for the medicines
            val position: Int = holder.adapterPosition
            comm.passDataCom(holder.itemTitle.text.toString(), holder.medicineId, medicines[position].brand_name)
        }
    }

    override fun getItemCount(): Int {
        return medicines.size
    }

}