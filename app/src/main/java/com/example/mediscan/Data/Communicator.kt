package com.example.mediscan.Data

import com.google.firebase.database.DatabaseReference

interface Communicator {
    fun passDataCom(
        medicineSelected: String,
        medicineId: String,
        brandName: String,
        pdf_link: String
    )

    fun openResultsPage(
        narrowDownSearch: List<NarrowDownSearch>,
        itemClicked: String,
        medicineName: String,
        brandName: String,
        pdfLink: String
    )

    fun deleteFromDB(
        db: DatabaseReference,
        id: String
    )

    fun openEmailClient()

    fun openPdfLink(pdfLink: String)

}