package Inicio_Sesion_User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proyecto.botndepnico.R


enum class ProviderType {
    BASIC
}

class Mostrar_Datos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar__datos)
    }
}