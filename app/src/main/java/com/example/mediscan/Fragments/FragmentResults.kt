package com.example.mediscan.Fragments


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mediscan.Accordresults
import com.example.mediscan.Adapter.DescriptionAdapter
import com.example.mediscan.R
import kotlinx.android.synthetic.main.fragment_results.*
import kotlinx.android.synthetic.main.row.*
import kotlinx.android.synthetic.main.row.view.*


class ResultsFragment : Fragment() {
    val descriptionList = ArrayList<Accordresults>()
    //var filt: Boolean = true
    //var filticon: Boolean = true
    var switch  = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_results, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* val btb = view.findViewById<Button>(R.id.exb)

         btb.setOnClickListener { view ->
             filt = !filt
             filticon = !filticon
         }
 */
        //Accordian Data
        if (descriptionList.isEmpty()) initData()
        setRecyclerView()


    }

    private fun setRecyclerView() {
        val descriptionAdapter = DescriptionAdapter(descriptionList)
        recyclerView.adapter = descriptionAdapter


        recyclerView.setHasFixedSize(true)

    }


    private fun initData() {
        filter()
        descriptionList.add(
            Accordresults(
                "Summary",
                "Omeprazole is used to treat certain stomach and esophagus problems (such as acid reflux, ulcers). It works by decreasing the amount of acid your stomach makes. It relieves symptoms such as heartburn, difficulty swallowing, and cough. This medicati ...Omeprazole is used to treat certain stomach and esophagus problems (such as acid reflux, ulcers). It works by decreasing the amount of acid your stomach makes. It relieves symptoms such as heartburn, difficulty swallowing, and cough. This medicati ...",
                true
            )

        )
        descriptionList.add(
            Accordresults(
                "Medic",
                "Test loefhporihfpn"
            )
        )
        descriptionList.add(
            Accordresults(
                "Topic",
                "Test loefhporihfpn"
            )
        )
        descriptionList.add(
            Accordresults(
                "Hi",
                "Test loefhporihfpn"
            )
        )
        descriptionList.add(
            Accordresults(
                "Rose",
                "Test loefhporihfpn"
            )
        )
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