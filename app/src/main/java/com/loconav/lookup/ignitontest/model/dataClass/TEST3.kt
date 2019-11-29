package com.loconav.lookup.ignitontest.model.dataClass

data class TEST3(
        override val battery: String,
        override val ignition: String,
        override val key: String,
        override val message: String,
        override val name: String,
        override val status: Int
) : Test(battery, ignition, key, message, name, status)