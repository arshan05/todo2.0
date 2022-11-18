package com.example.projecttodo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
//    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    Handler().postDelayed({
        val i = Intent(
            this@MainActivity,
            MainActivity2::class.java
        )
        startActivity(i)
        finish()
    }, 1000)

//        var actionBar: ActionBar? = supportActionBar
//        actionBar?.title = "todo (Project)"
//        actionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.button_background))

//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//
//        val navControllerFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
//        navController = navControllerFragment.navController
//        setupWithNavController(bottomNavigationView,navController)

    }
}