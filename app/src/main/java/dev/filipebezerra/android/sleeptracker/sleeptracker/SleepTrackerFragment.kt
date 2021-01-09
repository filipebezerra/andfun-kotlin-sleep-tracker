package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.filipebezerra.android.sleeptracker.R
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

    private lateinit var listAdapter: SleepNightAdapter

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
                SleepNightAdapter().apply {
                    listAdapter = this
                    viewBinding.sleepNightList.adapter = listAdapter
                }
                viewModel = this@SleepTrackerFragment.viewModel
                lifecycleOwner = viewLifecycleOwner
                setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.sleep_tracker_menu, menu)

    override fun onPrepareOptionsMenu(menu: Menu) =
        when(listAdapter.getViewStyle()) {
            ViewStyle.LIST -> menu.findItem(R.id.view_as_list_action).isVisible = false
            ViewStyle.GRID -> menu.findItem(R.id.view_as_grid_action).isVisible = false
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.view_as_list_action -> changeListAdapterViewStyle(ViewStyle.LIST)
            .also { (activity as AppCompatActivity).invalidateOptionsMenu() }
            .run { true }
        R.id.view_as_grid_action -> changeListAdapterViewStyle(ViewStyle.GRID)
            .also { (activity as AppCompatActivity).invalidateOptionsMenu() }
            .run { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun changeListAdapterViewStyle(viewStyle: ViewStyle) =
        when(viewStyle) {
            ViewStyle.LIST -> {
                viewBinding.sleepNightList.layoutManager = LinearLayoutManager(context)
                listAdapter.changeViewStyle(ViewStyle.LIST)
            }
            ViewStyle.GRID -> {
                viewBinding.sleepNightList.layoutManager = GridLayoutManager(context, 3)
                listAdapter.changeViewStyle(ViewStyle.GRID)
            }
        }
}
