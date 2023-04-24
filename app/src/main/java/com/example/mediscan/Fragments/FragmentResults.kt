package com.example.mediscan.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mediscan.Accordresults
import com.example.mediscan.Adapter.DescriptionAdapter
import com.example.mediscan.Data.Communicator
import com.example.mediscan.Data.NarrowDownSearch
import com.example.mediscan.R
import kotlinx.android.synthetic.main.fragment_results.*


class ResultsFragment : Fragment() {
    val descriptionList = ArrayList<Accordresults>()
    //var filt: Boolean = true
    //var filticon: Boolean = true
    var switch  = true
    private var itemClicked: String? = null
    private var narrowData = ArrayList<NarrowDownSearch>()
    private var medicineName: String? = null
    private var brandName: String?  = null
    private var pdfLink: String? = null
    private lateinit var comm: Communicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val view = inflater.inflate(R.layout.fragment_results, container, false)

        // Narrow Data from Pills Page
        itemClicked = arguments?.getString("itemClicked").toString()
        narrowData = arguments?.getParcelableArrayList("narrowData")!!
        medicineName = arguments?.getString("medicineName")
        brandName = arguments?.getString("brandName")
        pdfLink = arguments?.getString("pdfLink")
        comm = requireActivity() as Communicator
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* val btb = view.findViewById<Button>(R.id.exb)

         btb.setOnClickListener { view ->
             filt = !filt
             filticon = !filticon
         }
 */
        setMedicineName(view)

        see_pdf.setOnClickListener{ v: View ->
            if (pdfLink.toString().isNotEmpty()){
                comm.openPdfLink(pdfLink.toString())
            }
        }

        //Accordian Data
        if (descriptionList.isEmpty()) initData(narrowData, itemClicked.toString())
        setRecyclerView()


    }


    private fun setMedicineName(view: View) {
        val medicineName1: TextView = view.findViewById(R.id.medName1)
        val medicineName2: TextView = view.findViewById(R.id.medName2)
        val brandName: TextView = view.findViewById(R.id.brandName)
        medicineName1.text = medicineName
        medicineName2.text = medicineName
        brandName.text = this.brandName

    }

    private fun setRecyclerView() {
        val descriptionAdapter = DescriptionAdapter(descriptionList)
        recyclerView.adapter = descriptionAdapter


        recyclerView.setHasFixedSize(true)

    }


    private fun initData( desc_data: ArrayList<NarrowDownSearch>, item_clicked: String) {
        filter()
        for (desc_item in desc_data) {
            descriptionList.add(
                Accordresults(
                    desc_item.title,
                    desc_item.data,
                    desc_item.title.lowercase() == item_clicked.lowercase()
                )
            )
        }

    }

    fun toggleAll(){

        if (switch) {
            expand_al.setText(getString(R.string.collapse_al))
            switch = false

        } else {
            expand_al.setText(R.string.expand_al)
            switch = true

        }

        for(describe in descriptionList){
            describe.expandable = !switch
            describe.switcharr = !switch
        }
        setRecyclerView()

    }

    fun filter() {
        expand_al.setOnClickListener {
            toggleAll()
        }

    }
}