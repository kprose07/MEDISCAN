package com.example.mediscan.Data

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
}