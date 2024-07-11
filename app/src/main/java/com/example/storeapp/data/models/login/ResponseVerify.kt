package com.example.storeapp.data.models.login


import com.google.gson.annotations.SerializedName

data class ResponseVerify(
    @SerializedName("access_token")
    val accessToken: String?, // eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianRpIjoiNTNkNmVlZjI5YzhhNmRlYWIzNTRhZjIwYzA3NGZhYjQ4NWZkODczZjJiY2FjZWU3MGE4MjdlNTM1MWYxZTFlYzY4MmUwMTYyZjllMGQxY2UiLCJpYXQiOjE2ODY3NDk0NjYuOTIwNzM0LCJuYmYiOjE2ODY3NDk0NjYuOTIwNzM2LCJleHAiOjE2OTk5Njg2NjYuODQ5MzksInN1YiI6IjEiLCJzY29wZXMiOltdfQ.oQATpcsBFK2mzIkTYQdSbXXXbJL4TrfWpQrWoZ9TSeX5sMrWdP1VVx3VdCjrym5DQv8lrxoB3JtQmuexOEroQwf8uJxISCr6EjaFUaSSX0cvicKZ8IzxToqELCjnWvMTGB47B9kJZJAklmcRkltg0_0qLMy7sbB6afyC41sBh7IxFepWWYH64tJ3qpbgI4vU78JL2_4hCy6MH0KQ_f3fOl-jOApJ7dUlaEipPGIro5FNOSvrwtMVYIr9X-E9AmFAQl7U6xwUtJPfzz7nfIWt6G9wN5w-S68PN4dr4R9EIeTEB4VEv40pfZ39_yhO97Rwubx32yZRUx9exJ-CVbBjWIuUN3PVPetcg68So__MZ0N1IDMQ9zODy8OAk3WaPeThWJK9VyZZ87wSdTk7Sq-TM3W_LYHKGuPebpwDarkIqmSnjZzhl8OI26vMpNb0sqD_CYgs07oKwStBPtShCLMFXjQQNXAObzkk-lKURdOtKFUe_hyqS_IPViwKZ1u64GD4YMcM6FApzHLWtzToh3IymQNg7lfrXaoUVh-cfg_Kq9BEEJM5mssGAeEawPj_x46DgoawJCAxlUxvT0VgxQ43zFuGA59STbdKy-BA_WZfZYUtq-ZcvazoE0JPrxG26WI4D_wlYnt_Agycc3jd7olowziU3muRQNTfNHLGbLHeTx8
    @SerializedName("expires_in")
    val expiresIn: Int?, // 13219200
    @SerializedName("refresh_token")
    val refreshToken: String?, // def5020059cf76171961b0e4284a0ee095a763cf9c2d9e29ecf9fd93f617ec8e7756b7c9746c360abbf1b978be57a25e902cc61be2db4499a727eada219bdbdc812d4952edc30467d5d7a3c024d9fae64057e1a741016017cd260efb568542c86d3e9b8eff52cb7867ad6859d6dd1092144058afedb2dcf9ffe97a9d007f7b5c2dc98c1dc79a5c8370fbd5913954f802c88aa2ad34e773ff827e602f263f69c9a68574b7c83080ad740c2edf5da8ce16f4ff598906514e7401bb51cf3a97226a74b16a76fd75a462c101d9c7fdba9ed73599f5ed39a2353d93edb061c84311c387350134f3dc8931e85c63b832b1d3708c77c28dedc4146adc9da6455ad646b6c97e8b89ca2de5bcbfc3fa87c34880d4cbc1a2aaefdf34718a2741fdc833aa5877f7fdaf3b15b6d31aec8f00f5402264d82568d6023941b6febae4b280244b33e89f81f5e16912b6d51d91f58bbc507fd26bf2632934e7d3985001ce87e73b7adc
    @SerializedName("token_type")
    val tokenType: String?, // Bearer
    @SerializedName("user_detail")
    val userDetail: UserDetail?
) {
    data class UserDetail(
        @SerializedName("avatar")
        val avatar: String?, // avatar/1.jpg?168596283357
        @SerializedName("bank_account")
        val bankAccount: Any?, // null
        @SerializedName("birth_date")
        val birthDate: String?, // 1992-12-20T20:30:00.000000Z
        @SerializedName("cellphone")
        val cellphone: String?, // 09120174757
        @SerializedName("email")
        val email: String?, //
        @SerializedName("email_verified_at")
        val emailVerifiedAt: Any?, // null
        @SerializedName("firstname")
        val firstname: String?, // بردیا
        @SerializedName("id")
        val id: Int?, // 1
        @SerializedName("id_number")
        val idNumber: String?, // 0965479965
        @SerializedName("job_title")
        val jobTitle: String?, // Mobile developer
        @SerializedName("lastname")
        val lastname: String?, // یوسفی
        @SerializedName("type")
        val type: String?, // admin
        @SerializedName("updated")
        val updated: String?, // الان
        @SerializedName("wallet")
        val wallet: String? // 450000
    )
}