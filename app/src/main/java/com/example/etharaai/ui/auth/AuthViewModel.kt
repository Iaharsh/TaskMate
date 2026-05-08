package com.example.etharaai.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etharaai.data.local.entities.UserEntity
import com.example.etharaai.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableLiveData<AuthResult>()
    val authState: LiveData<AuthResult> = _authState

    fun login(email: String, password: String, role: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserByEmail(email)
                if (user == null) {
                    _authState.value = AuthResult.Error("User not registered. Please sign up first.")
                } else if (user.password != password) {
                    _authState.value = AuthResult.Error("Invalid password.")
                } else if (user.role != role) {
                    _authState.value = AuthResult.Error("Role mismatch. You are registered as ${user.role}.")
                } else {
                    _authState.value = AuthResult.Success(user)
                }
            } catch (e: Exception) {
                _authState.value = AuthResult.Error(e.message ?: "Login failed")
            }
        }
    }

    fun signup(name: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            try {
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    _authState.value = AuthResult.Error("Email already registered. Please login.")
                } else {
                    val newUser = UserEntity(
                        id = java.util.UUID.randomUUID().toString(),
                        name = name,
                        email = email,
                        password = password,
                        role = role
                    )
                    userRepository.saveUser(newUser)
                    _authState.value = AuthResult.Success(newUser)
                }
            } catch (e: Exception) {
                _authState.value = AuthResult.Error(e.message ?: "Registration failed")
            }
        }
    }
}

sealed class AuthResult {
    data class Success(val user: UserEntity) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
