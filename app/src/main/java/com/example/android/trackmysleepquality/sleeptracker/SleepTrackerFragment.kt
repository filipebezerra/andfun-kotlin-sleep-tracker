/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.sleeptracker.SleepTrackerFragmentDirections.actionSleepTrackerToSleepDetail
import com.google.android.material.snackbar.Snackbar
import com.example.android.trackmysleepquality.sleeptracker.SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment as toSleepQualityFragment

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    private val navController by lazy { findNavController() }

    private val sleepTrackerViewModel by lazy { getViewModel() }

    private lateinit var sleepTrackerViewBinding: FragmentSleepTrackerBinding

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? =
            FragmentSleepTrackerBinding.inflate(inflater).apply {
                this.lifecycleOwner = viewLifecycleOwner
                this.viewmodel = sleepTrackerViewModel
                sleepTrackerViewBinding = this
                observeNavigateToSleepQuality()
                observerShowSnackbarEvent()
                observeSleepNightList()
                observeNavigateToSleepDataQuality()
            }.root

    private fun observeSleepNightList() {
        val sleepNightAdapter = createSleepNightAdapter()
        sleepTrackerViewModel.nights.observe(viewLifecycleOwner) {
            it?.let {
                sleepNightAdapter.submitList(it)
            }
        }
    }

    private fun observeNavigateToSleepDataQuality() {
        sleepTrackerViewModel.navigateToSleepDataQuality.observe(viewLifecycleOwner) { nightId ->
            nightId?.let { navController.navigate(actionSleepTrackerToSleepDetail(nightId)) }
        }
    }

    private fun createSleepNightAdapter(): SleepNightAdapter {
        val sleepNightAdapter = SleepNightAdapter(SleepNightListener { nightId ->
            sleepTrackerViewModel.onSleepNightClicked(nightId)
        })
        sleepTrackerViewBinding.sleepList.adapter = sleepNightAdapter
        sleepTrackerViewBinding.sleepList.layoutManager = GridLayoutManager(activity, 3)
        return sleepNightAdapter
    }

    private fun observeNavigateToSleepQuality() =
            sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner) { night ->
                night?.let {
                    navController.navigate(toSleepQualityFragment(it.nightId))
                    sleepTrackerViewModel.onSleepDataQualityNavigated()
                }
            }

    private fun observerShowSnackbarEvent() =
            sleepTrackerViewModel.showSnackbarEvent.observe(viewLifecycleOwner) {
                if (it == true) {
                    Snackbar.make(
                            requireView(),
                            R.string.cleared_message,
                            Snackbar.LENGTH_LONG
                    ).show()
                    sleepTrackerViewModel.doneShowignSnackbar()
                }
            }

    private fun getViewModel(): SleepTrackerViewModel =
            createViewModelProvider().get(SleepTrackerViewModel::class.java)

    private fun createViewModelProvider(): ViewModelProvider =
            ViewModelProvider(this, createViewModelFactory())

    private fun createViewModelFactory(): SleepTrackerViewModelFactory {
        val application = requireNotNull(activity).application
        val sleepDatabaseDao = SleepDatabase.getInstance(application).sleepDatabaseDao
        return SleepTrackerViewModelFactory(
                sleepDatabaseDao,
                application
        )
    }
}
