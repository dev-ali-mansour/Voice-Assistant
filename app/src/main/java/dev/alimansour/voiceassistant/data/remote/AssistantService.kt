package dev.alimansour.voiceassistant.data.remote

import dev.alimansour.voiceassistant.data.remote.response.StudentResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AssistantService {
    @FormUrlEncoded
    @POST("get-student.php")
    fun getStudent(@Field("name") name: String): Call<StudentResponse>
}