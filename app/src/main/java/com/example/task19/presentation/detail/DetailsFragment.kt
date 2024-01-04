package com.example.task19.presentation.detail

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.task19.common.base.BaseFragment
import com.example.task19.common.helper.Observers
import com.example.task19.common.helper.resource.Resource
import com.example.task19.databinding.FragmentDetailsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate),
    Observers {

    private val viewModel: DetailsFragmentViewModel by viewModels()


    override fun init() {
        observers()
        val userId = arguments?.getInt("userId")
        userId?.let {
            viewModel.getUserInfo(it)
        }
    }

    override fun observers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    binding.progressBar.isVisible = viewState.isLoading

                    viewState.user.let { userResource ->
                        if (userResource is Resource.Success) {
                            val user = userResource.data

                            with(binding) {
                                tvId.text = user.id.toString()
                                tvEmail.text = user.email
                                tvName.text = user.firstName
                                tvLastName.text = user.lastName
                                setImageResource(user.avatar!!)
                            }
                        } else if (userResource is Resource.Error) {
                            Snackbar.make(
                                binding.root,
                                userResource.errorMessage.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun setImageResource(url: String) {
        Glide.with(this)
            .load(url)
            .into(binding.ivAvatar)
    }
}