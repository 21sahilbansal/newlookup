package com.loconav.lookup.ignitontest.model.dataClass

data class IgnitionTestData(
        val api_result: ApiResult,
        val ignition_tests: IgnitionTests,
        val next_test: String,
        val no_of_test_headers: Int,
        val restart_test: Boolean,
        val success: Boolean,
        val time_to_call_api: Int
)

