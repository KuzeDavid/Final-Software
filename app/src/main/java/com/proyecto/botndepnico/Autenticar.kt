package com.proyecto.botndepnico

import Boton_de_panico.Pag_Principal
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_autenticar.*

class Autenticar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autenticar)

        this.title = "SCHOOLISH"

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.actionbar_verde)))

        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración con Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        conf();
        sesion()

    }

    private fun sesion() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val IDnt = prefs.getString("ID", null)

        if (IDnt != null){
            pag_Principal(correo = "", ID = IDnt)
        }
    }

    private fun conf() {

        btnReg.setOnClickListener {
            if (etCorreo.text!!.isNotEmpty() && etPass.text!!.isNotEmpty()) {


                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        etCorreo.text.toString(),
                        etPass.text.toString()
                    ).addOnCompleteListener {

                        if (it.isSuccessful) {
                            //val ID = FirebaseAuth.getInstance().currentUser
                            val ID = FirebaseAuth.getInstance().uid
                            registro_datos(it.result?.user?.email?:"", ID.toString())
                        } else {

                            registroAlertaError()
                        }
                    }
            }

        }

       btnIng.setOnClickListener {
            if (etCorreo.text!!.isNotEmpty() && etPass.text!!.isNotEmpty()) {

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        etCorreo.text.toString(),
                        etPass.text.toString()
                    ).addOnCompleteListener {

                        if (it.isSuccessful) {
                            val ID = FirebaseAuth.getInstance().uid
                            pag_Principal(it.result?.user?.email?:"",ID.toString())

                        } else {

                            mostrarAlerta()
                        }
                    }
            }

        }

    }

    private fun mostrarAlerta() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No se ha podido Iniciar Sesión")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }


    private fun registroAlertaError() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error en Registro")
        builder.setMessage("Registro NO Exitoso")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun registro_datos(correo:String, ID: String) {
        val registro = Intent(this, datos_socio()::class.java).apply {
            putExtra("correo", correo)
            putExtra("ID", ID)
        }

        startActivity(registro)
    }

    private fun pag_Principal(correo:String, ID: String) {
        val registro = Intent(this, Pag_Principal::class.java).apply {
            putExtra("correo", correo)
            putExtra("ID", ID)
        }

        startActivity(registro)
    }
}