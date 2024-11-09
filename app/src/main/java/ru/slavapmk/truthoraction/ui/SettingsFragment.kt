package ru.slavapmk.truthoraction.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.slavapmk.truthoraction.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        binding.settingsFieldLayout.isEndIconVisible = false
        binding.settingsFieldEdit.setOnFocusChangeListener { _, hasFocus ->
            binding.settingsFieldLayout.isEndIconVisible = hasFocus
        }
        binding.settingsFieldLayout.setEndIconOnClickListener {
            val inputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
            binding.settingsFieldEdit.clearFocus()
        }
        binding.buttonClearHistory.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Используйте длительное нажатие для сброса истории вопросов",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.buttonClearHistory.setOnLongClickListener {
            Toast.makeText(
                requireContext(),
                "Типа сброс",
                Toast.LENGTH_LONG
            ).show()
            true
        }

        return binding.root
    }
}