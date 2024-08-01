package Inicio_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.proyecto.botndepnico.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        this.setTitle("SCHOOLISH");
        TiempoSplash()
    }
    private fun TiempoSplash(){
        object: CountDownTimer(1000, 1000){
            override fun onFinish() {
                historieta()
            }
            override fun onTick(p0 : Long) {
            }
        }.start()
    }
    private fun historieta() {
        val i = Intent(applicationContext,Historieta::class.java).apply{}
        startActivity(i)
    }
}
