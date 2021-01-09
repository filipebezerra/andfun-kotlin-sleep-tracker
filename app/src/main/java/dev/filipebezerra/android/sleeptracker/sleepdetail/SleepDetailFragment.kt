package dev.filipebezerra.android.sleeptracker.sleepdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dev.filipebezerra.android.sleeptracker.ServiceLocator.provideSleepNightRepository
import dev.filipebezerra.android.sleeptracker.databinding.SleepDetailFragmentBinding

class SleepDetailFragment : Fragment() {
    private val arguments: SleepDetailFragmentArgs by navArgs()

    private val viewModel: SleepDetailViewModel by viewModels {
        SleepDetailViewModel.factory(
            arguments.sleepNightId,
            provideSleepNightRepository(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SleepDetailFragmentBinding.inflate(inflater, container, false)
        .apply {
            viewModel = this@SleepDetailFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        .root
}