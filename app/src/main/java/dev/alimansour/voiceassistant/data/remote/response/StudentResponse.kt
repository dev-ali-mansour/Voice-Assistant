package dev.alimansour.voiceassistant.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dev.alimansour.voiceassistant.model.Student

class StudentResponse {
    @SerializedName("student")
    @Expose
    var student: Student? = null
}