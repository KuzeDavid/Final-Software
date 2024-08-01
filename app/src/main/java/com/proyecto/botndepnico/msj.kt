package com.proyecto.botndepnico

import Boton_de_panico.Pag_Principal
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_msj.*

class msj : AppCompatActivity() {

    private val bd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msj)

        title = "Configuración Aplicativo 3/4"

        val bundle = intent.extras
        val correo = bundle?.getString("correo")
        val ID = bundle?.getString("ID")
        //val contacto = bundle?.getCharSequence("contacto1")
        etMsjPred.setText(ID)
        mensaje(correo.toString(),ID.toString())

        btnSigPrincipal.setOnClickListener{

            //sigPrincipal(correo.toString(),ID.toString(),IDmsj.toString());//,nombre.toString());

        }

    }

    private fun mensaje(correo:String, ID:String){

        btnAñadirMsj.setOnClickListener{
            if (etMsjPred.text.isNotEmpty()) {
                var IDmsj = bd.collection("usuarios").document().id
                bd.collection("usuarios").document(ID)
                    .collection("mensaje").document(IDmsj)
                    .set(
                    hashMapOf("mensaje" to etMsjPred.text.toString()
                    )

                )
                btnSigPrincipal.setOnClickListener{

                    sigPrincipal(correo,ID,IDmsj);//,nombre.toString());

                }
                msjAceptado()

            }
            else
            {
                msjError()
            }
        }

    }

    private fun msjAceptado(){

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Datos Registrados")
        builder.setMessage("Mensaje Guardado")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()

    }
    private fun msjError(){

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Campo vacio")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()

    }

    private fun sigPrincipal(correo:String,ID: String, IDmsj:String){//, nombre:String) {
        val CorreoPrincipal = Intent(this, Pag_Principal::class.java).apply {
            putExtra("correo", correo)
            putExtra("ID", ID)
            putExtra("IDmsj", IDmsj)
            //putExtra("nombre", nombre)
        }

        startActivity(CorreoPrincipal)
    }
}