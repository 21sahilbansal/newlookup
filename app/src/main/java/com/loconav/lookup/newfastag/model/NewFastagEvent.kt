package com.loconav.lookup.newfastag.model

import com.loconav.lookup.base.PubSubEvent

class NewFastagEvent : PubSubEvent {
    constructor(message: String,vehiclebody : Any?):super (message,vehiclebody)
    constructor(message: String) : super (message)

    companion object {
        const val Truck_ID_VERIFIED = "truckid is verified"
        const val Scanned_Correct_Fastag = "Scanned correct fastag "
        const val Got_data_for_fastag_photos = "Got data for fastag photos"
    }
}