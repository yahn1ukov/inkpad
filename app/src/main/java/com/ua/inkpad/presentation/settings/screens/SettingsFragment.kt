package com.ua.inkpad.presentation.settings.screens

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ua.inkpad.Inkpad
import com.ua.inkpad.R
import com.ua.inkpad.databinding.FragmentSettingsBinding
import com.ua.inkpad.presentation.notes.viewModels.NoteViewModel
import javax.inject.Inject

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Inkpad.appComponent.inject(this@SettingsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.databaseBtn.setOnClickListener { confirmDeleteAll() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.settings_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == android.R.id.home) {
                    requireActivity().onBackPressed()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun confirmDeleteAll() {
        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Remove all?")
            setMessage("Are you sure that you want to remove all?")
            setIcon(R.drawable.ic_bin)
            setPositiveButton("Yes") { _, _ -> deleteAll() }
            setNegativeButton("No") { _, _ -> }
        }
        builder.create().show()
    }

    private fun deleteAll() {
        noteViewModel.deleteAll()
        Toast.makeText(
            requireContext(),
            "Notes have been successfully removed!",
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigate(R.id.action_settingsFragment_to_noteListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}