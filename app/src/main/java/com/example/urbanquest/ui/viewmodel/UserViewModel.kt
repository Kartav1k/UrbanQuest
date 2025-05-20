package com.example.urbanquest.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.urbanquest.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserViewModel : ViewModel() {
    companion object {
        private const val TAG = "UserViewModel"
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: DatabaseReference = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app")
        .getReference("users")

    private val _userData = mutableStateOf<User?>(null)
    val userData: State<User?> = _userData

    private val _isAuthorized = mutableStateOf(false)
    val isAuthorized: State<Boolean> = _isAuthorized

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isLoginChecked = mutableStateOf(false)
    val isLoginChecked: State<Boolean> = _isLoginChecked

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null && !currentUser.isAnonymous) {
            fetchUserData(currentUser.uid)
        } else {
            _isAuthorized.value = false
            _isLoginChecked.value = true
        }
    }

    /**
     * Регистрация нового пользователя
     */
    fun register(email: String, password: String, login: String, onResult: (Boolean, String?) -> Unit) {
        _isLoading.value = true

        // Проверяем уникальность логина
        checkLoginExists(login) { loginExists ->
            if (loginExists) {
                _isLoading.value = false
                onResult(false, "Логин уже занят")
                return@checkLoginExists
            }

            // Если логин свободен, создаем нового пользователя
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            createUserInDatabase(userId, email, login, onResult)
                        } else {
                            _isLoading.value = false
                            onResult(false, "Ошибка получения ID пользователя")
                        }
                    } else {
                        _isLoading.value = false
                        handleAuthError(task.exception, onResult)
                    }
                }
        }
    }

    /**
     * Создание записи пользователя в базе данных
     */

    private fun createUserInDatabase(userId: String, email: String, login: String, onResult: (Boolean, String?) -> Unit) {
        val newUser = User(userId = userId, email = email, login = login)

        db.child(userId).setValue(newUser)
            .addOnSuccessListener {
                Log.d(TAG, "Пользователь успешно добавлен в базу данных")
                _userData.value = newUser
                _isAuthorized.value = true
                _isLoginChecked.value = true
                _isLoading.value = false
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Ошибка при добавлении пользователя в базу данных", exception)
                auth.currentUser?.delete()
                _isLoading.value = false
                onResult(false, "Ошибка сохранения данных: ${exception.message}")
            }
    }

    /**
     * Обработка ошибок авторизации Firebase
     */
    private fun handleAuthError(exception: Exception?, onResult: (Boolean, String?) -> Unit) {
        val errorMessage = when {
            exception?.message?.contains("email address is already in use", ignoreCase = true) == true ->
                "Этот email уже зарегистрирован"
            exception?.message?.contains("password is invalid", ignoreCase = true) == true ->
                "Слабый пароль. Используйте минимум 6 символов"
            exception?.message?.contains("network error", ignoreCase = true) == true ->
                "Проверьте подключение к интернету"
            else -> exception?.message ?: "Неизвестная ошибка"
        }
        Log.e(TAG, "Ошибка авторизации: $errorMessage", exception)
        onResult(false, errorMessage)
    }

    /**
     * Проверка существования логина в базе данных
     */
    private fun checkLoginExists(login: String, onResult: (Boolean) -> Unit) {
        db.orderByChild("login").equalTo(login).get()
            .addOnSuccessListener { snapshot ->
                val exists = snapshot.exists()
                Log.d(TAG, "Проверка логина '$login': существует = $exists")
                onResult(exists)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Ошибка при проверке логина", exception)
                onResult(true)
            }
    }

    /**
     * Авторизация по логину и паролю
     */
    fun loginWithCredentials(login: String, password: String, onResult: (Boolean, String?) -> Unit) {
        _isLoading.value = true
        Log.d(TAG, "Попытка входа с логином: $login")

        // Находим email по логину
        db.orderByChild("login").equalTo(login).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    try {
                        val userSnapshot = snapshot.children.first()
                        val user = userSnapshot.getValue(User::class.java)

                        if (user != null && user.email.isNotEmpty()) {
                            // Вход с найденным email и введенным паролем
                            signInWithEmail(user.email, password, onResult)
                        } else {
                            _isLoading.value = false
                            onResult(false, "Ошибка получения данных пользователя")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Ошибка при обработке данных пользователя", e)
                        _isLoading.value = false
                        onResult(false, "Ошибка обработки данных: ${e.message}")
                    }
                } else {
                    _isLoading.value = false
                    onResult(false, "Пользователь с таким логином не найден")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Ошибка при поиске пользователя по логину", exception)
                _isLoading.value = false
                onResult(false, "Ошибка проверки логина: ${exception.message}")
            }
    }

    /**
     * Вход с использованием email и пароля
     */
    private fun signInWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    fetchUserData(userId) { success ->
                        _isLoading.value = false
                        if (success) {
                            onResult(true, null)
                        } else {
                            onResult(false, "Ошибка загрузки данных пользователя")
                        }
                    }
                } else {
                    _isLoading.value = false
                    onResult(false, "Ошибка получения ID пользователя")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Ошибка входа с email", exception)
                _isLoading.value = false
                onResult(false, "Неверный логин или пароль")
            }
    }

    /**
     * Получение данных пользователя из базы данных
     */
    fun fetchUserData(userId: String, onResult: (Boolean) -> Unit = {}) {
        _isLoading.value = true
        Log.d(TAG, "Загрузка данных пользователя с ID: $userId")

        db.child(userId).get()
            .addOnSuccessListener { snapshot ->
                try {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        Log.d(TAG, "Данные пользователя загружены успешно")
                        _userData.value = user
                        _isAuthorized.value = true
                    } else {
                        Log.w(TAG, "Данные пользователя не найдены в базе")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка при обработке данных пользователя", e)
                }
                _isLoginChecked.value = true
                _isLoading.value = false
                onResult(true)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Ошибка при загрузке данных пользователя", exception)
                _isLoading.value = false
                _isLoginChecked.value = true
                onResult(false)
            }
    }

    /**
     * Обновление состояния авторизации
     */
    fun updateAuthState(currentUser: FirebaseUser?) {
        Log.d(TAG, "Обновление состояния авторизации: ${currentUser?.uid}")

        if (currentUser != null && !currentUser.isAnonymous) {
            _isAuthorized.value = true
            fetchUserData(currentUser.uid)
        } else {
            _userData.value = null
            _isAuthorized.value = false
            _isLoginChecked.value = true
        }
    }

    /**
     * Восстановление пароля
     */
    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        _isLoading.value = true
        Log.d(TAG, "Проверка наличия email в базе для восстановления пароля: $email")

        db.orderByChild("email").equalTo(email).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    auth.sendPasswordResetEmail(email)
                        .addOnSuccessListener {
                            Log.d(TAG, "Письмо для восстановления пароля отправлено")
                            _isLoading.value = false
                            onResult(true, null)
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Ошибка при отправке письма для восстановления пароля", exception)
                            _isLoading.value = false
                            onResult(false, "Ошибка: ${exception.message}")
                        }
                } else {
                    Log.w(TAG, "Email не найден в базе данных: $email")
                    _isLoading.value = false
                    onResult(false, "Пользователь с таким email не зарегистрирован")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Ошибка при проверке email в базе данных", exception)
                _isLoading.value = false
                onResult(false, "Ошибка проверки email: ${exception.message}")
            }
    }

    /**
     * Гостевой вход
     */
    fun signInAnonymously(onResult: (Boolean, String?) -> Unit) {
        _isLoading.value = true
        Log.d(TAG, "Попытка гостевого входа")

        auth.signInAnonymously()
            .addOnSuccessListener {
                Log.d(TAG, "Гостевой вход успешен")
                _isAuthorized.value = false
                _isLoginChecked.value = true
                _isLoading.value = false
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Ошибка при гостевом входе", exception)
                _isLoading.value = false

                // Более детальная обработка ошибок
                val errorMessage = when {
                    exception.message?.contains("administrators only") == true ->
                        "Анонимный вход отключен. Включите его в консоли Firebase."
                    exception.message?.contains("network") == true ->
                        "Проверьте подключение к интернету"
                    else -> "Ошибка гостевого входа: ${exception.message}"
                }

                onResult(false, errorMessage)
            }
    }

    /**
     * Добавление места в избранное
     */
    fun addToFavourites(placeId: String, onResult: (Boolean) -> Unit = {}) {
        val uid = auth.currentUser?.uid

        if (uid == null || auth.currentUser?.isAnonymous == true) {
            Log.w(TAG, "Попытка добавить в избранное без авторизации")
            onResult(false)
            return
        }

        Log.d(TAG, "Добавление места $placeId в избранное для пользователя $uid")
        db.child(uid).child("favourites").child(placeId).setValue(true)
            .addOnSuccessListener {
                Log.d(TAG, "Место успешно добавлено в избранное")
                fetchUserData(uid)
                onResult(true)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Ошибка при добавлении места в избранное", exception)
                onResult(false)
            }
    }

    /**
     * Удаление места из избранного
     */
    fun removeFromFavourites(placeId: String, onResult: (Boolean) -> Unit = {}) {
        val uid = auth.currentUser?.uid

        if (uid == null || auth.currentUser?.isAnonymous == true) {
            Log.w(TAG, "Попытка удалить из избранного без авторизации")
            onResult(false)
            return
        }

        Log.d(TAG, "Удаление места $placeId из избранного для пользователя $uid")
        db.child(uid).child("favourites").child(placeId).removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "Место успешно удалено из избранного")
                // Обновляем данные пользователя
                fetchUserData(uid)
                onResult(true)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Ошибка при удалении места из избранного", exception)
                onResult(false)
            }
    }

    /**
     * Добавление достижения
     */
    fun addAchievement(achievementId: String, onResult: (Boolean) -> Unit = {}) {
        val uid = auth.currentUser?.uid

        if (uid == null || auth.currentUser?.isAnonymous == true) {
            Log.w(TAG, "Попытка добавить достижение без авторизации")
            onResult(false)
            return
        }

        Log.d(TAG, "Добавление достижения $achievementId для пользователя $uid")
        db.child(uid).child("achievements").child(achievementId).setValue(true)
            .addOnSuccessListener {
                Log.d(TAG, "Достижение успешно добавлено")
                // Обновляем данные пользователя
                fetchUserData(uid)
                onResult(true)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Ошибка при добавлении достижения", exception)
                onResult(false)
            }
    }

    /**
     * Проверка наличия достижения
     */
    fun hasAchievement(achievementId: String): Boolean {
        return _userData.value?.achievements?.containsKey(achievementId) == true
    }

    /**
     * Проверка наличия места в избранном
     */
    fun isFavourite(placeId: String): Boolean {
        return _userData.value?.favourites?.containsKey(placeId) == true
    }

    /**
     * Получение списка избранных мест
     */
    fun getFavouritePlaces(): List<String> {
        return _userData.value?.favourites?.filter { it.value }?.keys?.toList() ?: emptyList()
    }

    /**
     * Получение списка достижений
     */
    fun getAchievements(): List<String> {
        return _userData.value?.achievements?.filter { it.value }?.keys?.toList() ?: emptyList()
    }

    /**
     * Выход из аккаунта
     */
    fun logout() {
        Log.d(TAG, "Выход из аккаунта")
        auth.signOut()
        _userData.value = null
        _isAuthorized.value = false
        _isLoginChecked.value = true
    }
}