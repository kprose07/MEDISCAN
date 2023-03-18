package com.example.mediscan.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mediscan.Accordresults
import com.example.mediscan.DescriptionAdapter
import com.example.mediscan.R
import kotlinx.android.synthetic.main.fragment_results.*



class ResultsFragment : Fragment() {
    val descriptionList = ArrayList<Accordresults>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_results, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Accordian Data
        initData()
        setRecyclerView()
        filter()
    }

    private fun setRecyclerView() {
        val descriptionAdapter = DescriptionAdapter(descriptionList)
        recyclerView.adapter = descriptionAdapter
        recyclerView.setHasFixedSize(true)

    }

    private fun initData() {
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

    private fun filter() {
        /*val textView: TextView = findViewById(R.id.expand_al) as TextView
        textView.setOnClickListener {
            textView.text = getString(R.string.collapse_al)
        }
    }*/
    }
}
