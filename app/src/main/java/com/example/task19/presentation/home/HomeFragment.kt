package com.example.task19.presentation.home

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.task19.common.base.BaseFragment
import com.example.task19.common.helper.Listeners
import com.example.task19.common.helper.Observers
import com.example.task19.databinding.FragmentHomeBinding
import com.example.task19.presentation.model.User
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), Observers,
    Listeners {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private val adapter: HomeRecyclerViewAdapter by lazy { HomeRecyclerViewAdapter() }

    override fun init() {
        binding.rvUsers.apply {
            adapter = this@HomeFragment.adapter
        }
        observers()
        listeners()
    }

    override fun observers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    binding.progressBar.isVisible = viewState.isLoading
                    binding.buttonDelete.isVisible = viewState.isMultiSelectEnabled
                    adapter.submitList(viewState.usersList)

                    if (viewState.isError) {
                        Snackbar.make(
                            binding.root,
                            viewState.error!!,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun listeners() {
        adapter.onItemClick { user ->
            if (isSelectionModeIsEnabled()) {
                viewModel.selectItem(user, !user.isSelected)
            } else {
                navigateToDetails(user.id!!)
            }
        }

        adapter.onItemLongClick { user ->
            if (!isSelectionModeIsEnabled()) {
                viewModel.selectionMode(true)
            }
            viewModel.selectItem(user, true)
        }


        binding.buttonDelete.setOnClickListener {
            viewModel.deleteItems()
        }
    }

    private fun isSelectionModeIsEnabled() = viewModel.viewState.value.isMultiSelectEnabled

    private fun navigateToDetails(userId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(userId)
        findNavController().navigate(action)
    }
}