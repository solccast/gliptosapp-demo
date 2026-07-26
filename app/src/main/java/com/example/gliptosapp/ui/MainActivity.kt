package com.example.gliptosapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import android.graphics.Color
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.ActivityMainBinding
import com.example.gliptosapp.ui.settings.appearance.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.navOptions

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(ThemeManager.getTheme(this))
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor("#3E2A1F")),
            navigationBarStyle = SystemBarStyle.dark(Color.parseColor("#3E2A1F"))
        )

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        manejarDestinoPendiente(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        manejarDestinoPendiente(intent)
    }


    private fun manejarDestinoPendiente(intent: Intent?) {
        val destino = intent?.getStringExtra("DESTINO_NAV") ?: return

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
            ?: return

        val navController = navHostFragment.navController

        when (destino) {
            "colectionFragment" -> {
                val opciones = navOptions {
                    popUpTo(R.id.initFragment) { inclusive = false }
                }
                navController.navigate(R.id.colectionFragment, null, opciones)
            }
        }

        intent.removeExtra("DESTINO_NAV")
    }
}