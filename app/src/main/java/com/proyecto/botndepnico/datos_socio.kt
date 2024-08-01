package com.proyecto.botndepnico

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_datos_socio.*


class datos_socio : AppCompatActivity() {
    private val bd = FirebaseFirestore.getInstance()
    lateinit var opciones:Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_socio)
        title = "Información Personal"
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.actionbar_verde)))
        val bundle = intent.extras
        val correo = bundle?.getString("correo")
        val ID = bundle?.getString("ID")
        registro(correo.toString(),ID.toString())
        mostrar_datos(ID.toString())
        btnelm.setOnClickListener {
            elm_p(ID.toString())
        }
        val lista = listOf("Femenino", "Masculino", "Otro")
        opciones = findViewById(R.id.etGenero)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        opciones.adapter = adaptador
    }

    private fun registro(correo:String, ID: String) {
        btnAct.setOnClickListener {
            if (etNombre.text!!.isNotEmpty() && etEdad.text!!.isNotEmpty() && etGenero.isClickable) {
                //var ID = bd.collection("usuarios").document().id
                bd.collection("usuarios").document(ID).set(
                    hashMapOf("nombre" to etNombre.text.toString(),
                        "edad" to etEdad.text.toString(),
                        "genero" to  opciones.selectedItem.toString(),
                            "correo" to correo
                    )
                    )
                msjAceptado()
                btnBotonEdit.setOnClickListener {
                    configuracion(correo,ID)
                    Log.d("Correo", correo)
                }
            }
            else {
                msjError()
            }
        }
    }

    private fun msjError(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Uno o todos los campos estan vacios")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

    private fun msjAceptado(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Datos Registrados")
        builder.setMessage("Se han registrado tus datos personales")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

    private fun configuracion(correo:String, ID:String) {
        val registro = Intent(this, btn_edit()::class.java).apply {
            putExtra("correo", correo)
            putExtra("ID",ID)
        }
        startActivity(registro)
    }

    private fun mostrar_datos(ID:String){
        bd.collection("usuarios").document(ID)
                .get().addOnSuccessListener { documento ->
                    if(documento.exists()){
                        var Nombre = documento.getString("nombre")
                        var Edad = documento.getString("edad")
                        var Genero = documento.getString("genero")
                        etNombre.setText(Nombre)
                        etEdad.setText(Edad)
                        etGenero2.setText(Genero)
                    }
                }
    }

    private fun elm_p(ID: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar cuenta")
        builder.setMessage("Se eliminaran tus datos de EBA y no podras volver a ingresar \n " +
                "¿Estas seguro de eliminar tu cuenta?")
        with(builder){
            setPositiveButton("Sí"){
                    dialog, which ->
                eliminar(ID)
            }
            setNegativeButton("No"){
                    dialog, which->
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun eliminar(ID:String){
        bd.collection("usuarios").document(ID).delete()
        val auth=  FirebaseAuth.getInstance()
       val user: FirebaseUser? = auth.currentUser
        user?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("ELM", "User account deleted.")
                    }
                }
        etNombre.setText("")
        etEdad.setText("")
        etGenero2.setText("")
        val Inicio = Intent(this, Autenticar()::class.java).apply {
            putExtra("ID",ID)
        }
        startActivity(Inicio)
    }
}

