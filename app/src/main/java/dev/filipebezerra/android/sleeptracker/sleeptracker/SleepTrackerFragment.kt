package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.snackbar.Snackbar
import dev.filipebezerra.android.sleeptracker.ServiceLocator
import dev.filipebezerra.android.sleeptracker.databinding.SleepTrackerFragmentBinding
import dev.filipebezerra.android.sleeptracker.databinding.SleepTrackerFragmentBinding.inflate
import dev.filipebezerra.android.sleeptracker.util.event.EventObserver
import dev.filipebezerra.android.sleeptracker.util.ext.setupSnackbar

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    private val navController: NavController by lazy { findNavController() }

    private lateinit var viewBinding: SleepTrackerFragmentBinding

    private val viewModel: SleepTrackerViewModel by viewModels {
        SleepTrackerViewModelFactory(
            ServiceLocator.provideSleepNightRepository(requireContext()),
            requireActivity().application,
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflate(inflater, container, false)
            .apply {
                viewBinding = this
                with(viewBinding.sleepNightList) {
                    adapter = SleepNightAdapter()
                    itemAnimator = DefaultItemAnimator()
                }
                viewModel = this@SleepTrackerFragment.viewModel
                lifecycleOwner = viewLifecycleOwner
            }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            navigateToSleepQuality.observe(viewLifecycleOwner, EventObserver {
                navController.navigate(SleepTrackerFragmentDirections
                    .actionSleepTrackerFragmentToSleepQualityFragment(it))
            })
            view.setupSnackbar(viewLifecycleOwner, snackbarText, Snackbar.LENGTH_SHORT)
        }
    }
}
