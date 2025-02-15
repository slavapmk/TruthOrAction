package ru.slavapmk.truthoraction.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import ru.slavapmk.truthoraction.R
import ru.slavapmk.truthoraction.databinding.FragmentSettingsBinding
import ru.slavapmk.truthoraction.dto.game.History


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val activity by lazy {
        requireActivity() as MainActivity
    }

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
                getString(R.string.reset_short),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.buttonClearHistory.setOnLongClickListener {
            activity.shared.edit {
                putString(
                    "history",
                    activity.historyCodec.encodeHistory(
                        History()
                    )
                )
                commit()
            }
            Toast.makeText(
                requireContext(),
                getString(R.string.reset_long),
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        binding.settingsFieldEdit.setText(
            activity.shared.getString("aiSettings", "")
        )
        binding.settingsFieldEdit.addTextChangedListener({ _, _, _, _ ->
        }, { onChange, _, _, _ ->
            activity.shared.edit {
                putString("aiSettings", onChange.toString())
                commit()
            }
        }, {})

        binding.tokenFieldEdit.setText(
            activity.shared.getString("aiToken", "")
        )
        binding.tokenFieldEdit.addTextChangedListener({ _, _, _, _ ->
        }, { onChange, _, _, _ ->
            activity.shared.edit {
                putString("aiToken", onChange.toString())
                commit()
            }
        }, {
            Toast.makeText(
                requireContext(),
                getString(R.string.restart),
                Toast.LENGTH_LONG
            ).show()
        })

        return binding.root
    }
}