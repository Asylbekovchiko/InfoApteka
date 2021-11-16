package kg.sunrise.infoapteka.ui.auth.confirm

sealed class Timer

data class TimerTick(val value: String) : Timer()
object TimerFinish : Timer()