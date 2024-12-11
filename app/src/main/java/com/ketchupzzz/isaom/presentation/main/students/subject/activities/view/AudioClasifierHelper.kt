package com.ketchupzzz.isaom.presentation.main.students.subject.activities.view

import android.content.Context
import android.media.AudioRecord
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.support.audio.TensorAudio
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import org.tensorflow.lite.task.core.BaseOptions
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

interface AudioClassificationListener {
    fun onError(error: String)
    fun onResult(results: List<Category>, inferenceTime: Long)
}


class AudioClassifierHelper(
    val context: Context,
    val listener: AudioClassificationListener,
    var currentModel: String = SPEECH_COMMAND_MODEL,
    var classificationThreshold: Float = DISPLAY_THRESHOLD,
    var overlap: Float = DEFAULT_OVERLAP_VALUE,
    var numOfResults: Int = DEFAULT_NUM_OF_RESULTS,
    var currentDelegate: Int = 0,
    var numThreads: Int = 2
) {
    private lateinit var classifier: AudioClassifier
    private lateinit var tensorAudio: TensorAudio
    private lateinit var recorder: AudioRecord
    private var executor: ScheduledThreadPoolExecutor? = null

    private val classifyRunnable = Runnable {
        classifyAudio()
    }

    init {
        initClassifier()
    }

    fun initClassifier() {
        // Set general detection options, e.g. number of used threads
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(numThreads)


        when (currentDelegate) {
            DELEGATE_CPU -> {
                // Default
            }
            DELEGATE_NNAPI -> {
                baseOptionsBuilder.useNnapi()
            }
        }


        val options = org.tensorflow.lite.task.audio.classifier.AudioClassifier.AudioClassifierOptions.builder()
            .setScoreThreshold(classificationThreshold)
            .setMaxResults(numOfResults)
            .setBaseOptions(baseOptionsBuilder.build())
            .build()

        try {
            // Create the classifier and required supporting objects
            classifier = AudioClassifier.createFromFileAndOptions(context, currentModel, options)
            tensorAudio = classifier.createInputTensorAudio()
            recorder = classifier.createAudioRecord()
        } catch (e: IllegalStateException) {
            listener.onError(
                "Audio Classifier failed to initialize. See error logs for details"
            )

            Log.e("AudioClassification", "TFLite failed to load with error: " + e.message)
        }
    }

    fun startAudioClassification() {
        if (recorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
            return
        }

        recorder.startRecording()
        executor = ScheduledThreadPoolExecutor(1)

        val lengthInMilliSeconds = ((classifier.requiredInputBufferSize * 1.0f) /
                classifier.requiredTensorAudioFormat.sampleRate) * 1000

        val interval = (lengthInMilliSeconds * (1 - overlap)).toLong()

        executor?.scheduleWithFixedDelay(
            classifyRunnable,
            0,
            interval,
            TimeUnit.MILLISECONDS)
    }

    private fun classifyAudio() {
        tensorAudio.load(recorder)
        var inferenceTime = SystemClock.uptimeMillis()
        val output = classifier.classify(tensorAudio)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
        listener.onResult(output[0].categories, inferenceTime)
    }

    fun stopAudioClassification() {
        executor?.let {
            recorder.stop()
            it.shutdownNow()
        }
    }
    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_NNAPI = 1
        const val DISPLAY_THRESHOLD = 0.3f
        const val DEFAULT_NUM_OF_RESULTS = 1
        const val DEFAULT_OVERLAP_VALUE = 0.5f
        const val SPEECH_COMMAND_MODEL = "isaom.tflite"
    }
}