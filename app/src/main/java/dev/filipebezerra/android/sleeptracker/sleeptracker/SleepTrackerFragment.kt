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
                SleepNightAdapter(
                    SleepNightListener {
                        viewModel?.onSleepNightSelected(it)
                    }
                ).apply {
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
            navigateToSleepDetail.observe(viewLifecycleOwner, EventObserver {
                navController.navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepDetailFragment(it)
                )
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.sleep_tracker_menu, menu)

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (listAdapter.listViewStyle == ListViewStyle.LIST)
            menu.findItem(R.id.view_as_list_action).isVisible = false
        else if (listAdapter.listViewStyle == ListViewStyle.GRID)
            menu.findItem(R.id.view_as_grid_action).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.view_as_list_action -> changeListAdapterViewStyle(ListViewStyle.LIST)
            .also { (activity as AppCompatActivity).invalidateOptionsMenu() }
            .run { true }
        R.id.view_as_grid_action -> changeListAdapterViewStyle(ListViewStyle.GRID)
            .also { (activity as AppCompatActivity).invalidateOptionsMenu() }
            .run { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun changeListAdapterViewStyle(listViewStyle: ListViewStyle) {
        if (listViewStyle == ListViewStyle.LIST) {
            viewBinding.sleepNightList.layoutManager = LinearLayoutManager(context)
            listAdapter.changeViewStyle(ListViewStyle.LIST)
        } else if (listViewStyle == ListViewStyle.GRID) {
            viewBinding.sleepNightList.layoutManager = GridLayoutManager(
                context,
                SLEEP_NIGHT_GRID_SPAN_COUNT
            ).withSpanSizeLookupForHeaderAndList()
            listAdapter.changeViewStyle(ListViewStyle.GRID)
        }
    }
}
