package com.maulana.warehouse.core.response

import kotlinx.serialization.Serializable

@Serializable
abstract class BasicResponse(
    var errorMessage: String? = null
)
