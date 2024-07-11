package com.example.storeapp.data.models.profile

import com.google.gson.annotations.SerializedName

data class BodyUpdateProfile(
    @SerializedName("firstName")
    var firstName: String? = null, // api
    @SerializedName("gregorianDate")
    var gregorianDate: String? = null, // 1993-10-25
    @SerializedName("idNumber")
    var idNumber: String? = null, // 0000000000
    @SerializedName("lastName")
    var lastName: String? = null // nouri
)