package Inicio_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import com.proyecto.botndepnico.Autenticar
import com.proyecto.botndepnico.R
import kotlinx.android.synthetic.main.activity_historieta.*

class Historieta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historieta)
        this.setTitle("SCHOOLISH boton de panico")
        TiempoHisto()
        ivHistorieta.setOnClickListener {
            sigPag()
        }
    }
   private fun TiempoHisto(){
        object: CountDownTimer(15000, 1000){
            override fun onFinish() {
               sigPag()
            }
            override fun onTick(p0 : Long) {
            }
        }.start()
    }
    private fun sigPag (){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro")
        builder.setMessage("¿Estas registrado en la plataforma SCHOOLISH?")
        with(builder){
            setPositiveButton("Sí"){
                dialog, which ->
                user_registrado()
            }
            setNegativeButton("No"){
                dialog, which->
                user_No_registrado()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
    private fun user_registrado(){
        val registrado = Intent(this, Autenticar::class.java).apply {
        }
        startActivity(registrado)
    }
    private fun user_No_registrado(){
        val Noregistrado = Intent(this, Tratamiento_Datos::class.java).apply {
        }
        startActivity(Noregistrado)
    }
}