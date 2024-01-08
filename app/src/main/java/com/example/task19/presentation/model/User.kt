package com.example.task19.presentation.model

data class User(
    val avatar: String?,
    val email: String?,
    val firstName: String?,
    val id: Int?,
    val lastName: String?,
    var isSelected: Boolean = false,
    var itemState: ItemState = ItemState.NORMAL_STATE
) {
    enum class ItemState {
        NORMAL_STATE,
        MULTI_SELECTION_STATE
    }
}
