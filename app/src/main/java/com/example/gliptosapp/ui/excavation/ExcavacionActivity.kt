package com.example.gliptosapp.ui.excavation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gliptosapp.databinding.ActivityExcavacionBinding

class ExcavacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExcavacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExcavacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos los componentes y listeners de la excavación
        configurarInterfaz()
    }


    private fun configurarInterfaz() {
        // Ejemplo de control UX fundamental: Un botón para volver atrás o cerrar la actividad.
        // Los niños se frustran fácilmente si entran a una pantalla pesada y no encuentran cómo salir.
        // Asumiendo que tenés un botón de salida (ej: btnVolver o btnCerrar) en tu activity_excavacion.xml:
        /*
        binding.btnVolver.setOnClickListener {
            finish() // Cierra esta Activity y devuelve al niño automáticamente al InitFragment (Menú principal)
        }
        */

        // Aquí irá la lógica interactiva para limpiar la tierra (gestos o clicks de la pala)
    }
}