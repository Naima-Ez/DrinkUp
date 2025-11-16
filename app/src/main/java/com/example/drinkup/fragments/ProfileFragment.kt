package com.example.drinkup.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.drinkup.DrinkViewModel
import com.example.drinkup.R
import com.example.drinkup.auth.LoginActivity
import com.example.drinkup.databinding.FragmentProfileBinding
import com.example.drinkup.utils.PreferencesManager
import com.example.drinkup.workers.ReminderWorker
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DrinkViewModel by activityViewModels()
    private lateinit var prefsManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.tvUserName.text = it.nom
                binding.tvUserEmail.text = it.email
                binding.tvUserGoal.text = getString(
                    R.string.daily_goal_display,
                    String.format("%.1f", it.objectifMl / 1000f)
                )
                binding.switchNotifications.isChecked = it.notificationsEnabled
            }
        }
    }

    private fun setupListeners() {
        binding.btnEditProfile.setOnClickListener { showEditProfileDialog() }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateNotifications(isChecked)
            prefsManager.notificationsEnabled = isChecked
            if (isChecked) ReminderWorker.scheduleReminder(requireContext())
            else ReminderWorker.cancelReminder(requireContext())
        }

        binding.btnLanguage.setOnClickListener { showLanguageDialog() }

        binding.btnLogout.setOnClickListener { showLogoutConfirmation() }
    }

    private fun showEditProfileDialog() {
        val user = viewModel.currentUser.value ?: return

        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)
        val etName = dialogView.findViewById<EditText>(R.id.et_name)
        etName.setText(user.nom)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.edit_profile))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val newName = etName.text.toString().trim()
                if (newName.isNotEmpty()) {
                    user.nom = newName
                    viewModel.currentUser.value = user
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.profile_updated),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showLanguageDialog() {
        val languages = arrayOf(
            getString(R.string.french),
            getString(R.string.english),
            getString(R.string.arabic)
        )
        val languageCodes = arrayOf("fr", "en", "ar")

        val currentLanguage = viewModel.currentUser.value?.langue ?: prefsManager.language
        val currentIndex = languageCodes.indexOf(currentLanguage)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_language))
            .setSingleChoiceItems(languages, currentIndex) { dialog, which ->
                val selectedCode = languageCodes[which]
                updateLanguage(selectedCode)
                prefsManager.language = selectedCode
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun updateLanguage(languageCode: String) {
        viewModel.updateLanguage(languageCode)

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config, resources.displayMetrics)

        requireActivity().recreate()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_confirmation))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> logout() }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun logout() {
        prefsManager.currentUserId = -1L
        ReminderWorker.cancelReminder(requireContext())

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
