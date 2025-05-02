package com.example.gymandroid.viewmodel


import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class mainViewModel : ViewModel() {
    // DrawerState debe ser manejado directamente, no como un StateFlow
    val drawerState = DrawerState(DrawerValue.Closed)


    init {

    }



    // Acciones del drawer
    fun openDrawer() = viewModelScope.launch {
        drawerState.open()
    }

    fun closeDrawer() = viewModelScope.launch {
        drawerState.close()
    }


    companion object Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return mainViewModel() as T
        }
    }



}