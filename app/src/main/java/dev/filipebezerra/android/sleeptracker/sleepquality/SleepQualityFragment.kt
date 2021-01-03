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

package dev.filipebezerra.android.sleeptracker.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.filipebezerra.android.sleeptracker.database.SleepTrackerDatabase
import dev.filipebezerra.android.sleeptracker.databinding.SleepQualityFragmentBinding
import dev.filipebezerra.android.sleeptracker.databinding.SleepQualityFragmentBinding.inflate
import dev.filipebezerra.android.sleeptracker.util.event.EventObserver

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : BottomSheetDialogFragment() {

    private val arguments: SleepQualityFragmentArgs by navArgs()

    private val viewModel: SleepQualityViewModel by viewModels{
        SleepQualityViewModelFactory(
            arguments.sleepNight,
            SleepTrackerDatabase.getDatabase(requireContext()).sleepNightDao
        )
    }

    private lateinit var viewBinding: SleepQualityFragmentBinding

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflate(inflater, container, false)
        .apply {
            viewBinding = this
            viewModel = this@SleepQualityFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            navigateToSleepTracker.observe(viewLifecycleOwner, EventObserver {
                dismiss()
            })
        }
    }
}
