package com.example.android.trackmysleepquality.sleepdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepDetailBinding
import com.example.android.trackmysleepquality.sleepdetail.SleepDetailFragmentDirections.actionSleepDetailToSleepTrackerFragment

class SleepDetailFragment : Fragment() {

    private val arguments by navArgs<SleepDetailFragmentArgs>()
    private val sleepNightId by lazy { arguments.sleepNightKey }

    private val viewModel: SleepDetailViewModel by lazy { createViewModel() }

    private val navController by lazy { findNavController() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = FragmentSleepDetailBinding.inflate(inflater).apply {
        this.sleepDetailViewModel = viewModel
        this.lifecycleOwner = viewLifecycleOwner
        observeUi()
    }.root

    private fun createViewModel(): SleepDetailViewModel {
        val sleepDatabase = SleepDatabase.getInstance(requireContext())
        val sleepDatabaseDao = sleepDatabase.sleepDatabaseDao
        return ViewModelProvider(
                this,
                SleepDetailViewModelFactory(sleepNightId, sleepDatabaseDao)
        ).get(SleepDetailViewModel::class.java)
    }

    private fun observeUi() {
        observeClose()
    }

    private fun observeClose() {
        viewModel.navigateToSleepTracker.observe(viewLifecycleOwner) {
            it?.let {
                navController.navigate(actionSleepDetailToSleepTrackerFragment())
                viewModel.onSleepTrackerNavigated()
            }
        }
    }
}