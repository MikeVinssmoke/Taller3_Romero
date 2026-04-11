package com.example.romero.taller3_romero



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // El NavHostFragment maneja toda la navegación automáticamente
        // gracias al nav_graph.xml que ya configuramos
    }
}