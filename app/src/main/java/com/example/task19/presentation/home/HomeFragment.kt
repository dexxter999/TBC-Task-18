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
import com.example.task19.common.helper.resource.Resource
import com.example.task19.databinding.FragmentHomeBinding
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

                    adapter.submitList(
                        when (viewState.usersList) {
                            is Resource.Success -> viewState.usersList.data
                            else -> emptyList()
                        }
                    )

                    if (viewState.isError) {
                        Snackbar.make(
                            binding.root,
                            viewState.error?.message.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun listeners() {
        adapter.onItemClick { user ->
            navigateToDetails(user.id!!)
        }
    }

    private fun navigateToDetails(userId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(userId)
        findNavController().navigate(action)
    }
}