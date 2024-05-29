package com.app.productcatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.productcatalog.ui.ProductListScreen
import com.app.productcatalog.ui.ProductListViewModel
import com.app.productcatalog.ui.ScreenRoute
import com.app.productcatalog.ui.UiState
import com.app.productcatalog.ui.theme.ProductCatalogAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductCatalogAssignmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val productViewModel = hiltViewModel<ProductListViewModel>()
                    ProductApp(viewModel = productViewModel)
                }
            }
        }
    }
}

@Composable
fun ProductApp(
    navController: NavHostController = rememberNavController(),
    viewModel: ProductListViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState)}
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.ProductListScreen.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ScreenRoute.ProductListScreen.name) {
                val state: UiState by viewModel.uiState.collectAsStateWithLifecycle()
                ProductListScreen(
                    uiState = state,
                    snackBarState = snackbarHostState,
                ) {
                    viewModel.onEventChange(it)
                }
            }
        }
    }
}