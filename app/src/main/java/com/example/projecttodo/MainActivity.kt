package com.example.projecttodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var actionBar: ActionBar? = supportActionBar
//        actionBar?.title = "todo (Project)"
//        actionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.button_background))

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navControllerFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navControllerFragment.navController
        setupWithNavController(bottomNavigationView,navController)

    }
}