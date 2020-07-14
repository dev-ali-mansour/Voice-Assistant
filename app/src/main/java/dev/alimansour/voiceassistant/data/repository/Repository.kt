package dev.alimansour.voiceassistant.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.alimansour.voiceassistant.BuildConfig
import dev.alimansour.voiceassistant.data.remote.AssistantService
import dev.alimansour.voiceassistant.data.remote.AppConfig
import dev.alimansour.voiceassistant.model.Student
import dev.alimansour.voiceassistant.data.remote.response.StudentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository internal constructor() {
    private val getResponse = AppConfig.retrofit.create(
        AssistantService::class.java)
    private val studentLiveData = MutableLiveData<Student>()

    /**
     * Get student data from the backend
     * @param name Student name
     * @return LiveData<Student>
     */
    fun getStudent(name: String): LiveData<Student> {
        val call = getResponse.getStudent(name)
        call.enqueue(object : Callback<StudentResponse> {
            override fun onResponse(
                call: Call<StudentResponse>,
                response: Response<StudentResponse>
            ) {
                if (response.isSuccessful) {
                    val studentResponse = response.body()
                    if (studentResponse?.student != null) {
                        studentLiveData.value = studentResponse.student
                    }
                } else {
                    studentLiveData.value = null
                    Log.d(TAG, "خطأ في عملية الاستعلام عن الطالب!")
                }
            }

            override fun onFailure(call: Call<StudentResponse>, t: Throwable) {
                studentLiveData.value = null
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "خطأ في عملية الاستعلام عن الطالب!\n${t.message}")
                } else {
                    Log.d(TAG, "خطأ في عملية الاستعلام عن الطالب!")
                }
            }
        })
        return studentLiveData
    }

    companion object {
        private const val TAG = "Voice Assistant"
    }
}