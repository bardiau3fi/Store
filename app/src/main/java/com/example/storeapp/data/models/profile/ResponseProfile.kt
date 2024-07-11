package com.example.storeapp.data.models.profile

import com.google.gson.annotations.SerializedName

data class ResponseProfile(
    @SerializedName("avatar")
    val avatar: String?, //
    @SerializedName("birth_date")
    val birthDate: String?, // 1371-09-29T20:34:16.000000Z
    @SerializedName("cellphone")
    val cellphone: String?, // 09120174757
    @SerializedName("email")
    val email: String?, // test2@nouri-api.ir
    @SerializedName("firstname")
    val firstname: String?, // محمد
    @SerializedName("id")
    val id: Int?, // 1
    @SerializedName("idNumber")
    val idNumber: String?, // 0965479965
    @SerializedName("job")
    val job: String?, // Mobile developer
    @SerializedName("lastname")
    val lastname: String?, // نوری
    @SerializedName("register_date")
    val registerDate: String?, // 15 ، دی ، 01
    @SerializedName("wallet")
    val wallet: String? // 450000
)