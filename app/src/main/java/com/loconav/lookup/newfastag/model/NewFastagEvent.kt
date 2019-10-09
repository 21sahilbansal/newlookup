package com.loconav.lookup.newfastag.model

import com.loconav.lookup.base.PubSubEvent

class NewFastagEvent : PubSubEvent {
    constructor(message: String,vehiclebody : Any?):super (message,vehiclebody)
    constructor(message: String) : super (message)

    companion object {
        const val Truck_ID_VERIFIED = "truckid is verified"
        const val SCANNED_CORRECT_FASTAG = "Scanned correct fastag "
        const val GOT_DATA_FOR_FASTAG_PHOTOS = "Got data for fastag photos"
        const val SCANNED_WRONG_FASTAG = "Scanned wrong fastag"
        const val TRUCK_ID_NOT_VERIFIED = "Truck id not verified"
        const val DATA_FOR_FASTAG_NOT_FOUND = "Data for fastag not found"
        const val SCANNED_FASTAG ="Scanned Fastag"

    }
}