package dev.alimansour.voiceassistant.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("grade")
    @Expose
    var grade: String,
    @SerializedName("address")
    @Expose
    var address: String,
    @SerializedName("age")
    @Expose
    var age: String
)