package ctbll.ctball.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class QuicklyViewModel : ViewModel() {

    var posX = MutableLiveData<Int>()
    var posY = MutableLiveData<Int>()
    var ballX = MutableLiveData<Float>()
    var ballY = MutableLiveData<Float>()
    var tm = MutableLiveData<Int>()
    var timeToLoadBan = MutableLiveData<Int>()
    var gameoverover = MutableLiveData<Boolean>()
    var directionX = 10F
    var directionY = 10F

    var widthDp = 400F
    var heightDp = 400F
    var timeD = 0L



    init {
        posY.value = 200
        posX.value = 200
        ballX.value = 40F
        ballY.value = 120F
        tm.value = 10
        gameoverover.value = false
        timeToLoadBan.value = 0

        viewModelScope.launch{
            while (true)
            {
                delay(20L)
                timeD = timeD.plus(20L)



                ballX.value = ballX.value?.plus(directionX)
                ballY.value = ballY.value?.plus(directionY)

                if (ballX.value!! > widthDp-widthDp/7) directionX = -directionX
                if (ballX.value!!<10F) directionX = -directionX
                if (ballY.value!! > heightDp-heightDp/5) directionY = -directionY
                if (ballY.value!!<10F) directionY = -directionY



                if (timeD ==1000L) {
                    timeD =0L
                    tm.value = tm.value?.plus(-1)
                    timeToLoadBan.value = timeToLoadBan.value?.plus(1)
                }
                if (tm.value!! < 1){
                    gameoverover.value = true
                }

            }




        }
    }
    fun onCatch(){
        directionX = directionX.plus(directionX/5)
        directionY = directionY.plus(directionY/5)
        tm.value = 10
    }


}