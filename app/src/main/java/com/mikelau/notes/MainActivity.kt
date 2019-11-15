package com.mikelau.notes

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.mikelau.notes.databinding.ActivityMainBinding
import com.mikelau.notes.databinding.NavHeaderMainBinding
import com.mikelau.notes.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val noteViewModel: NoteViewModel by viewModel()
    private lateinit var headerBinding: NavHeaderMainBinding
    private val navController by lazy { findNavController(R.id.nav_host_fragment) } //1
    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.noteListFragment,
                R.id.noteFragment
            ), drawerLayout
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        setSupportActionBar(toolbar)
        setupNavigation()
        setupViews()
    }

    override fun onDestroy() {
        noteViewModel.apply { closeDb() }
        super.onDestroy()
    }

    private fun setupDataBinding() {
        val activityMainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        headerBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.nav_header_main, activityMainBinding.navView, false
        )
        activityMainBinding.navView.addHeaderView(headerBinding.root)
    }

    private fun setupNavigation() {
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(R.id.noteFragment)) fab.hide() else fab.show()
        }
    }

    private fun setupViews() {
        navView.setNavigationItemSelectedListener(this)

        fab.setOnClickListener {
            navController.navigate(R.id.noteFragment)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_delete_all -> {
                noteViewModel.deleteAllNotes()
            }

            R.id.nav_info -> {
                // TODO Future Info
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
