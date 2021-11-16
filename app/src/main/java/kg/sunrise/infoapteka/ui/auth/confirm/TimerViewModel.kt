package kg.sunrise.infoapteka.ui.auth.confirm

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.sunrise.infoapteka.utils.constants.SEND_AGAIN_DURATION
import kg.sunrise.infoapteka.utils.constants.SEND_AGAIN_INTERVAL

class TimerViewModel : ViewModel() {

    private val _timerLiveData = MutableLiveData<Timer>()
    val timerLiveData: LiveData<Timer> = _timerLiveData
    private var remainedTimeInMillis: Long = 0L
    private var timer: DownTimer? = null

    fun startTimer() {
        if (remainedTimeInMillis > 0) return
        timer = DownTimer()
        timer?.start()
    }

    override fun onCleared() {
        timer?.cancel()
        timer = null
    }

    inner class DownTimer : CountDownTimer(
        SEND_AGAIN_DURATION,
        SEND_AGAIN_INTERVAL
    ) {

        override fun onTick(millisUntilFinished: Long) {
            remainedTimeInMillis = millisUntilFinished
            val remainedTimeInSec = millisUntilFinished / 1_000
            val time =
                if (remainedTimeInSec < 10) "00:0$remainedTimeInSec"
                else "00:$remainedTimeInSec"
            _timerLiveData.value = TimerTick(time)
        }

        override fun onFinish() {
            remainedTimeInMillis = 0
            _timerLiveData.value = TimerFinish
        }
    }
}