package com.example.noteapplication

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Config
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import java.util.*

// Страница настроек
class SettingsActivity : AppCompatActivity() {
    private lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        useDarkTheme(preferences.getBoolean("useDarkTheme", false))
        setLocalization(preferences.getString("localization_preference", "en"))

        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.settings)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun useDarkTheme(value:Boolean){
        when (value){
            true -> setTheme(AppCompatDelegate.MODE_NIGHT_YES)
            false -> setTheme(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    private fun setLocalization(code: String?){
        val locale: Locale = Locale(code)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.configuration.setLocale(locale)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val setThemePreference = findPreference<ListPreference>("theme_list_preferences")

            setThemePreference?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->

                    activity?.finish()
                    true
                }
        }

    }
}