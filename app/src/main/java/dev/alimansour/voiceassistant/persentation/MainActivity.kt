package dev.alimansour.voiceassistant.persentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.text.format.DateUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import dev.alimansour.voiceassistant.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var recognizerIntent: Intent
    private lateinit var mTextToSpeech: TextToSpeech
    private lateinit var mSpeechRecognizer: SpeechRecognizer
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        try {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

            checkPermissions()

            recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            recognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)

            fab.setOnClickListener {
                mSpeechRecognizer.startListening(recognizerIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        try {
            if (requestCode == PERMISSIONS_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000L)
                        checkPermissions()
                    }
                    Toast.makeText(
                        this,
                        "The application needs recording permission to work!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Check required permissions
     */
    private fun checkPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.RECORD_AUDIO
                        ),
                        PERMISSIONS_REQUEST_CODE
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Initialize SpeechRecognizer
     */
    private fun initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            mSpeechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}

                override fun onRmsChanged(rmsdB: Float) {}

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onPartialResults(partialResults: Bundle?) {}

                override fun onEvent(eventType: Int, params: Bundle?) {}

                override fun onBeginningOfSpeech() {
                    recordingImageView.setImageResource(R.drawable.ic_mic_black_24dp)
                }

                override fun onEndOfSpeech() {
                    recordingImageView.setImageResource(R.drawable.ic_mic_off_black_24dp)
                }

                override fun onError(error: Int) {
                    speak("Sorry! I can't hear from you")
                }

                override fun onResults(results: Bundle?) {
                    val result: String =
                        results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!![0]
                    processResult(result)
                }
            })
        }
    }


    private fun processResult(result: String) {
        try {
            val command = result.toLowerCase(Locale.US)
            when {
                command.indexOf("your name") != -1 -> {
                    speak("My name is Alexa")
                }
                command.indexOf("your job") != -1 -> {
                    speak("I am your personal assistant to help you know students grades")
                }
                command.indexOf("time") != -1 -> {
                    val now = Date()
                    val time = DateUtils.formatDateTime(
                        this, now.time,
                        DateUtils.FORMAT_SHOW_TIME
                    )
                    speak("The time now is $time")
                }
                command.indexOf("student") != -1 -> {
                    speak("Please tell me the student name by saying name is")
                }
                command.indexOf("name is") != -1 -> {
                    val name = command.split("name is ")[1]
                    viewModel.getStudentInfo(name).observe(this, androidx.lifecycle
                        .Observer {
                            if (it == null) {
                                speak("Sorry I didn't find student with name $name")
                            } else {
                                speak(
                                    "The student ${it.name} His ID is ${it.id}" +
                                            " His grade is ${it.grade}" +
                                            " and he lives in ${it.address}"
                                )
                            }
                        })
                }
                command.indexOf("open my website") != -1 -> {
                    speak("Sure! Opening your personal website")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.alimansour.dev"))
                    startActivity(intent)
                }
                command.indexOf("creator") != -1 -> {
                    speak("I am smart Android application developed by Ali Mansour")
                }
                command.indexOf("thanks") != -1 -> {
                    speak("Ok Good Bye!")
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000L)
                        finish()
                    }
                }
                else -> {
                    speak("Un recognized command! $command")
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message!!)
            speak("Sorry it is failed! ${e.message}")
        }
    }

    /**
     * Initialize TextToSpeech feature
     */
    private fun initializeTextToSpeech() {
        try {
            mTextToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener {
                if (mTextToSpeech.engines.size == 0) {
                    Toast.makeText(
                        this,
                        "There is no TextToSpeech engine on your device!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    mTextToSpeech.language = Locale.US
                    speak("Hello! How can I help you?")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Convert text to speech
     * @param message Message
     */
    @Suppress("DEPRECATION")
    private fun speak(message: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mTextToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                mTextToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        mTextToSpeech.shutdown()
    }

    override fun onResume() {
        super.onResume()
        initializeTextToSpeech()
        initializeSpeechRecognizer()
        speak("Welcome back! How can I help you?")
    }

    companion object {
        private const val TAG = "Voice Assistant"
        private const val PERMISSIONS_REQUEST_CODE = 1
    }
}