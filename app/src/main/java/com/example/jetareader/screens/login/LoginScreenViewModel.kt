package com.example.jetareader.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetareader.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreenViewModel: ViewModel() {

    //val loadingState = MutableStateFlow(LoadingState.IDLE)

    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit)
          = viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("FB", "createUserWithEmailAndPassword: Yayaya ${task.result.toString()}")
                          //  Todo: ("take them home")
                            home()
                        }
                        Log.d("FB", "createUserWithEmailAndPassword: ${task.result.toString()}")
                    }
            } catch (ex: Exception) {
                Log.d("FB", "createUserWithEmailAndPassword: ${ex.message}")
            }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //me
                        val displayName = task.result?.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        home()
                    }else {
                        Log.d("FB", "createUserWithEmailAndPassword: ${task.result.toString()}")

                    }
                    _loading.value = false


                }
        }


    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = MUser(userId = userId.toString(), displayName = displayName.toString(), avatarUrl = "", quote = "Life is great",
            profession = "Kotlin King cloud queen", id=null).toMap()
       // val user = mutableMapOf<String, Any>()


        FirebaseFirestore.getInstance().collection("users")
            .add(user)

    }
}