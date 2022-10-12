package com.myrgb.ledcontroller.feature.rgbalarmclock.list

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.view.ActionMode
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.MainActivity
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentRgbAlarmListBinding
import com.myrgb.ledcontroller.feature.rgbalarmclock.list.adapter.RgbAlarmListAdapter
import com.myrgb.ledcontroller.feature.rgbalarmclock.list.adapter.RgbAlarmListItemDetailsLookup
import com.myrgb.ledcontroller.feature.rgbalarmclock.list.adapter.RgbAlarmListItemKeyProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RgbAlarmListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<RgbAlarmListViewModel> { viewModelFactory }

    private lateinit var binding: FragmentRgbAlarmListBinding

    private lateinit var selectionTracker: SelectionTracker<Long>

    private var actionMode: ActionMode? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_rgb_alarm_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewAdapter()

        binding.fabAddRgbAlarm.setOnClickListener {
            val action = RgbAlarmListFragmentDirections.actionAlarmListToAlarmAddEdit()
            findNavController().navigate(action)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        selectionTracker.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        selectionTracker.onRestoreInstanceState(savedInstanceState)
        if (selectionTracker.hasSelection()) {
            actionMode =
                (requireActivity() as MainActivity).startSupportActionMode(actionModeCallback)
            actionMode?.title =
                getString(R.string.rgb_alarms_selected, selectionTracker.selection.size())
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.rgbAlarmListComponent().create()
            .inject(this)
    }

    private fun initRecyclerViewAdapter() {
        val alarmListAdapter = RgbAlarmListAdapter { rgbAlarm ->
            val action =
                RgbAlarmListFragmentDirections.actionAlarmListToAlarmAddEdit(rgbAlarm.timeMinutesOfDay)
            findNavController().navigate(action)
        }
        binding.recyclerViewAlarms.adapter = alarmListAdapter
        binding.recyclerViewAlarms.setHasFixedSize(true)

        selectionTracker = SelectionTracker.Builder(
            "rgbAlarmSelection",
            binding.recyclerViewAlarms,
            RgbAlarmListItemKeyProvider(alarmListAdapter),
            RgbAlarmListItemDetailsLookup(binding.recyclerViewAlarms),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        selectionTracker.addObserver(selectionTrackerObserver)
        alarmListAdapter.tracker = selectionTracker
    }

    private val selectionTrackerObserver = object : SelectionTracker.SelectionObserver<Long>() {
        override fun onSelectionChanged() {
            super.onSelectionChanged()
            val actionModeNotEnabled = actionMode == null
            if (actionModeNotEnabled) {
                val mainActivity = requireActivity() as MainActivity
                actionMode = mainActivity.startSupportActionMode(actionModeCallback)
            }

            val anyRgbAlarmSelected = selectionTracker.selection.size() > 0
            if (anyRgbAlarmSelected)
                actionMode?.title =
                    getString(R.string.rgb_alarms_selected, selectionTracker.selection.size())
            else
                actionMode?.finish()
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.rgb_alarm_selection_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = true

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.action_delete_rgb_alarm -> {
                    val listAdapter = binding.recyclerViewAlarms.adapter as RgbAlarmListAdapter
                    val selectedRgbAlarms = listAdapter.currentList.filter { rgbAlarm ->
                        selectionTracker.selection.contains(rgbAlarm.timeMinutesOfDay.toLong())
                    }
                    viewModel.deleteRgbAlarms(selectedRgbAlarms)
                    actionMode?.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            val listAdapter = binding.recyclerViewAlarms.adapter as RgbAlarmListAdapter
            listAdapter.tracker?.clearSelection()
        }
    }
}