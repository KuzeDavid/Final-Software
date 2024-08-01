package Boton_de_panico
// ESTE ES SCHOOLISH
import EscuelaInfo.escuelainfo
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.botndepnico.R
import com.proyecto.botndepnico.btn_edit
import com.proyecto.botndepnico.datos_socio
import kotlinx.android.synthetic.main.activity_pag__principal.*
import mapa.Mapa

class Pag_Principal : AppCompatActivity() {

    private val bd = FirebaseFirestore.getInstance()
    private val code =  11
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pag__principal)
        title = "SCHOOLISH"
        tipodePerfil()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        solicitarPermisos()
        enviarSMS()
        //ubicacion()
        val bundle = intent.extras
        val ID = bundle?.getString("ID")
        val correo = bundle?.getString("correo")

        facebook(ID.toString())
        whatsApp(ID.toString())

        /* val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
         prefs.putString("ID", ID)
         prefs.apply()*/

        val IDbtn1 = bd.collection("usuarios")
            .document(ID.toString()).collection("botones")
            .document("jRHaBP85jYEEPZjHhlEP").id

        val IDbtn2 = bd.collection("usuarios")
            .document(ID.toString()).collection("botones")
            .document("l2blui94fwoJv55pk3vq").id
        mostrarDatos(ID.toString(),IDbtn1,btnMSJ1)
        mostrarDatos(ID.toString(),IDbtn2,btnMSJ2)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val bundle = intent.extras
        val ID = bundle?.getString("ID")
        val correo = bundle?.getString("correo")

        when (item.itemId){
            R.id.salir_menu -> {
                val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                prefs.clear()
                prefs.apply()
                finish()
            }
            R.id.perfil_menu -> {
                val perfil = Intent(this, datos_socio::class.java).apply{
                    putExtra("correo", correo)
                    putExtra("ID", ID)
                }
                startActivity(perfil)
            }
            R.id.conf_menu -> {
                val conf = Intent(this, btn_edit::class.java).apply{
                    putExtra("ID", ID)
                }
                startActivity(conf)
            }
            R.id.not_menu -> {
                vistaBoton(ID.toString())
            }
            R.id.irmapa -> {
                val mapas = Intent(this, Mapa::class.java)
                startActivity(mapas)
            }
            R.id.infoNav -> {
                val infoescuela = Intent(this, escuelainfo::class.java)
                startActivity(infoescuela)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tipodePerfil(){
        if (
            btnMSJ1.text.toString() == "Acoso"
            &&  btnMSJ2.text.toString() == "Robo"){

            title = "Perfil"
            Toast.makeText(
                this,
                "Perfil Prestablecido",
                Toast.LENGTH_LONG
            ).show()
        }

        else {
            title = "Personalizado"

            Toast.makeText(
                this,
                "Perfil Personalizado",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun mostrarDatos(ID: String, IDbtn: String, btn: Button) {

        bd.collection("usuarios").document(ID.toString())
            .collection("botones").document(IDbtn).get().addOnSuccessListener {
                btn.setText(it.get("Nombre") as String?)
                if (btn.text.toString() == "Acoso"){
                    title = "Preestablecido"
                }
            }

        bd.collection("usuarios").document(ID).get().addOnSuccessListener {
            tv_nombre.setText(it.get("nombre") as String?)
        }
    }

    private fun solicitarPermisos(){
        val SMS = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
        val Localizador = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if(SMS!=PackageManager.PERMISSION_GRANTED && Localizador!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION),code)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode==code && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            msjAprobado()

        } }

    // SMS

    @RequiresApi(Build.VERSION_CODES.R)
    private fun enviarSMS () {

        val bundle = intent.extras
        val ID = bundle?.getString("ID")
        val correo = bundle?.getString("correo")

        val IDbtn1 = bd.collection("usuarios")
            .document(ID.toString()).collection("botones")
            .document("jRHaBP85jYEEPZjHhlEP").id

        val IDbtn2 = bd.collection("usuarios")
            .document(ID.toString()).collection("botones")
            .document("l2blui94fwoJv55pk3vq").id

        val IDbtn3 = bd.collection("usuarios")
            .document(ID.toString()).collection("botones")
            .document("Jqji3ncjefaSHB5qUVnW").id

        val IDbtn4 = bd.collection("usuarios")
            .document(ID.toString()).collection("botones")
            .document("RaaaBnnIUawPjsiKke0k").id

        val IDcont1 = bd.collection("usuarios").document(ID.toString())
            .collection("botones").document(IDbtn1)
            .collection("contactos").document("gnAxPhqOWTeh5TxAx9z3").id

        val IDcont2 = bd.collection("usuarios").document(ID.toString())
            .collection("botones").document(IDbtn1)
            .collection("contactos").document("IqwbHuRrXp4dHkJEme3j").id

        val IDcont3 = bd.collection("usuarios").document(ID.toString())
            .collection("botones").document(IDbtn1)
            .collection("contactos").document("f9CybwrIogVICsrYNVAO").id

        val IDcont4 = bd.collection("usuarios").document(ID.toString())
            .collection("botones").document(IDbtn1)
            .collection("contactos").document("NYLVhTBV7mTYuUwkcsRE").id

        btnMSJ1.setOnClickListener {btnMSJ(ID.toString(),IDbtn1,IDcont1,IDcont2,IDcont3,IDcont4)
            ConfirmacionMensaje()
        }
        btnMSJ2.setOnClickListener { btnMSJ(ID.toString(), IDbtn2, IDcont1, IDcont2, IDcont3, IDcont4)
            ConfirmacionMensaje()
        }


    }

    private fun ConfirmacionMensaje(){
        Toast.makeText(this, "Alerta enviada", Toast.LENGTH_LONG).show()
    }

    private fun btnMSJ(ID : String, IDbtn: String, IDcont1: String, IDcont2 :String, IDcont3:String, IDcont4:String){

        bd.collection("usuarios").document(ID.toString())
            .collection("botones").document(IDbtn)
            .get().addOnSuccessListener {

                val MSJ = it.get("Mensaje")
                msjBoton(ID.toString(),IDbtn,IDcont1,IDcont2,IDcont3,IDcont4,MSJ.toString())
            }

    }

    private fun msjAprobado(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Aprobado")
        builder.setMessage("Haz permitido el envío de mensajes por parte de la app Schoolish")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun msjBoton(ID : String, IDbtn: String, IDcont1: String, IDcont2 :String, IDcont3:String, IDcont4:String, MSJ: String){

        bd.collection("usuarios").document(ID.toString())
            .collection("botones").document(IDbtn)
            .get().addOnSuccessListener {

                bd.collection("usuarios").document(ID.toString())
                    .collection("botones").document(IDbtn)
                    .collection("contactos").document(IDcont1).get().addOnSuccessListener { document ->

                        if (document.exists()) {

                            msj(MSJ.toString(), ID.toString(),IDbtn,IDcont1)
                            msj(MSJ.toString(), ID.toString(),IDbtn,IDcont2)
                            msj(MSJ.toString(), ID.toString(),IDbtn,IDcont3)
                            msj(MSJ.toString(), ID.toString(),IDbtn,IDcont4)


                        } else {


                            msj(MSJ.toString(), ID.toString(),IDbtn,IDcont2)
                            msj(MSJ.toString(), ID.toString(),IDbtn,IDcont3)
                            msj(MSJ.toString(), ID.toString(),IDbtn,IDcont4)



                        }

                    }
            }

    }

    private fun msj(MSJ:String, ID:String,IDbtn:String,IDcont:String){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    Log.d("Ubicacion", "$lat  $lon");

                    bd.collection("usuarios").document(ID)
                        .collection("botones").document(IDbtn)
                        .collection("contactos").document(IDcont).get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                if (MSJ.isEmpty()){
                                    Log.d("MSj", "El mensaje esta vacio")
                                }else{
                                    var contacto = document.getString("Telefono")
                                    var cont = SmsManager.getDefault()
                                    cont.sendTextMessage(contacto.toString(),
                                        null, MSJ + "https://maps.google.com/maps?q=${lat},${lon}" , null, null)
                                }
                            }else {
                                Log.d("Contacto","Contacto no existe")
                            }
                        }
                }else{
                    Log.d("Ubicacion", "No encontrada");
                }
            }
    }

    private fun facebook(ID: String){
        logoface.setOnClickListener{
            if(btnMSJ1.text.toString().isBlank()){
                Toast.makeText(this,"Debes configurar el boton 1, para poder hacer uso de las redes sociales",Toast.LENGTH_LONG).show()
            }else{

                val r_social = Intent(this, ConexionFacebook::class.java).apply {
                    putExtra("ID", ID)
                }
                startActivity(r_social)

            }

        }
    }

    private fun whatsApp(ID: String) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) { return }

        bd.collection("usuarios").document(ID)
            .collection("botones").document("jRHaBP85jYEEPZjHhlEP").get()
            .addOnSuccessListener { documento ->
                val mensaje="Porfavor contactame, necesito ayuda "
                logowhats.setOnClickListener {
                    if (btnMSJ1.text.toString().isBlank()) {
                        Toast.makeText(
                            this,
                            "Debes configurar el boton 1, para poder hacer uso de las redes sociales",
                            Toast.LENGTH_LONG
                        ).show()
                    } else { fusedLocationProviderClient.lastLocation
                            .addOnSuccessListener { location ->
                                if (location != null) {
                                    val lat = location.latitude
                                    val lon = location.longitude

                                    val sendIntent = Intent()
                                    sendIntent.action = Intent.ACTION_VIEW
                                    val uri =
                                        "whatsapp://send?phone=" + "&text=" + mensaje +" " +"https://maps.google.com/maps?q=${lat},${lon}"
                                    sendIntent.data = Uri.parse(uri)
                                    startActivity(sendIntent)
                                }

                            }
                    }
                }
            }
    }

    private fun vistaBoton(ID:String){

        val v_Boton = Intent(this, Vista_Boton::class.java).apply {
            putExtra("ID", ID)
        }
        startActivity(v_Boton)

    }

}
