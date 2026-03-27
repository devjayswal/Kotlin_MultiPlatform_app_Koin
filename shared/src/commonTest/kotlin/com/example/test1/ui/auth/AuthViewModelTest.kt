package com.example.test1.ui.auth

import app.cash.turbine.test
import com.example.test1.core.ToastService
import com.example.test1.core.ToastType
import com.example.test1.data.local.LocalAssetDataSource
import com.example.test1.data.local.TokenManager
import com.example.test1.data.model.AuthResponse
import com.example.test1.data.model.NetworkUser
import com.example.test1.data.model.NewsItem
import com.example.test1.data.model.NewsResponse
import com.example.test1.data.remote.ApiService
import com.example.test1.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FakeToastService : ToastService {
    var lastMessage: String? = null
    var lastType: ToastType? = null

    override fun showToast(message: String, type: ToastType, detail: String?) {
        lastMessage = message
        lastType = type
    }
}

class FakeApiService : ApiService {
    var loginResult: AuthResponse? = null
    var shouldThrowError = false
    
    override suspend fun login(username: String, password: String): AuthResponse {
        if (shouldThrowError) throw Exception("Network Error")
        return loginResult ?: throw Exception("Not mocked")
    }

    override suspend fun fetchNews(limit: Int, offset: Int): NewsResponse = TODO()
    override suspend fun fetchNewsById(id: Int): NewsItem = TODO()
    override suspend fun fetchUsers(): List<NetworkUser> = TODO()
    override suspend fun refreshToken(refreshToken: String): AuthResponse = TODO()
}

class FakeTokenManager : TokenManager {
    private val _accessToken = MutableStateFlow<String?>(null)
    override val accessToken: Flow<String?> = _accessToken
    private val _refreshToken = MutableStateFlow<String?>(null)
    override val refreshToken: Flow<String?> = _refreshToken
    
    override suspend fun saveTokens(access: String, refresh: String) { 
        _accessToken.value = access 
        _refreshToken.value = refresh
    }
    override suspend fun clearTokens() { 
        _accessToken.value = null 
        _refreshToken.value = null
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AuthViewModel
    private lateinit var repository: AppRepository
    private lateinit var toastService: FakeToastService
    private lateinit var apiService: FakeApiService
    private lateinit var tokenManager: FakeTokenManager

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        apiService = FakeApiService()
        tokenManager = FakeTokenManager()
        val localAssetDataSource = object : LocalAssetDataSource {
            override suspend fun readText(path: String): String = ""
        }
        
        repository = AppRepository(apiService, localAssetDataSource, tokenManager)
        toastService = FakeToastService()
        viewModel = AuthViewModel(repository, toastService)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoginMode)
            assertFalse(initialState.isLoading)
            assertEquals("", initialState.username)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleMode changes isLoginMode`() = runTest {
        viewModel.uiState.test {
            assertEquals(true, awaitItem().isLoginMode)
            viewModel.toggleMode()
            assertEquals(false, awaitItem().isLoginMode)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login success updates state and shows toast`() = runTest {
        // Arrange
        val username = "testuser"
        val password = "password"
        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)
        
        apiService.loginResult = AuthResponse("access_token", "refresh_token")

        // Act
        viewModel.authenticate()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(null, state.error)
            assertEquals("Logged In", toastService.lastMessage)
            assertEquals(ToastType.SUCCESS, toastService.lastType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login with empty fields shows error`() = runTest {
        // Act
        viewModel.authenticate()
        
        // Assert
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Missing fields", state.error)
            assertEquals("Missing fields", toastService.lastMessage)
            assertEquals(ToastType.WARNING, toastService.lastType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login failure updates error state`() = runTest {
        // Arrange
        viewModel.onUsernameChange("user")
        viewModel.onPasswordChange("pass")
        apiService.shouldThrowError = true

        // Act
        viewModel.authenticate()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("Login Failed", state.error)
            assertEquals("Login Failed", toastService.lastMessage)
            assertEquals(ToastType.FAIL, toastService.lastType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `logout clears authentication and shows toast`() = runTest {
        // Arrange: Start as authenticated
        tokenManager.saveTokens("token", "refresh")
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isAuthenticated)
            assertEquals("Logged out", toastService.lastMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
