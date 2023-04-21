package com.example.mediscan.Data

import com.google.firebase.database.DatabaseReference

interface Communicator {
    fun passDataCom(
        medicineSelected: String,
        medicineId: String,
        brandName: String
    )

    fun openResultsPage(
        narrowDownSearch: List<NarrowDownSearch>,
        itemClicked: String,
        medicineName: String,
        brandName: String
    )

    fun deleteFromDB(
        db: DatabaseReference,
        id: String
    )

    fun openEmailClient()


}