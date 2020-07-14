package dev.alimansour.voiceassistant.persentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.alimansour.voiceassistant.data.repository.Repository
import dev.alimansour.voiceassistant.model.Student

class MainViewModel : ViewModel() {

    /**
     * Get student data from the backend
     * @param name Student name
     * @return LiveData<Student>
     */
    fun getStudentInfo(name: String): LiveData<Student> {
        return Repository().getStudent(name)
    }
}