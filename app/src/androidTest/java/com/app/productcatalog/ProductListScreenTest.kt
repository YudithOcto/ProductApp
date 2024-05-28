package com.app.productcatalog

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.productcatalog.di.AppModule
import com.app.productcatalog.domain.repository.ProductRepository
import com.app.productcatalog.mockdata.MockData
import com.app.productcatalog.scheduler.TaskSchedulerProvider
import com.app.productcatalog.ui.FilterOption
import com.app.productcatalog.ui.ProductListViewModel
import com.app.productcatalog.ui.UiEvent
import com.app.productcatalog.ui.UiState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppModule::class)
class ProductListScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @MockK(relaxed = true)
    lateinit var productRepository: ProductRepository

    private lateinit var navController: TestNavHostController
    private lateinit var productViewModel: ProductListViewModel
    private val dispatcher = TaskSchedulerProvider()

    @Before
    fun setup() {
        hiltRule.inject()
        MockKAnnotations.init(this)

        productViewModel = ProductListViewModel(productRepository, dispatcher)
        productViewModel.refreshProducts()
        productViewModel.updateViewModel(
            UiState(
                isLoading = false,
                successGetProducts = MockData.productSpecList
            )
        )
        composeRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            ProductApp(navController = navController, viewModel = productViewModel)
        }
    }

    @Test
    fun productListScreenSearchTest() {
        // search hint should displayed
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Cari Barang").assertIsDisplayed()

        // User Typing
        val searchInput = composeRule.onNodeWithTag("SearchBar")
        searchInput.performClick()
        composeRule.onNode(isFocused()).performTextInput("Smartphone")
        composeRule.onNode(isFocused()).assertTextEquals("Smartphone")

        // should display 1 product only which have name Smartphone
        productViewModel.updateViewModel(
            UiState(
                isLoading = false,
                successGetProducts = MockData.productSpecList.filter { it.name == "Smartphone" })
        )
        composeRule.onNodeWithText("Ditemukan 1 Produk").assertIsDisplayed()
        composeRule.onNodeWithTag("Smartphone").assertIsDisplayed()
    }

    @Test
    fun productListScreenFilterTest() {
        // All Filter should displays
        composeRule.onNodeWithText("Semua").assertIsDisplayed()
        composeRule.onNodeWithText("Favorit").assertIsDisplayed()
        composeRule.onNodeWithText("Tidak Favorit").assertIsDisplayed()

        // perform Favourite click and should display nothing since we don't have any favorite product in mockup
        composeRule.onNodeWithTag(FilterOption.Favourites.text).performClick()
        productViewModel.onEventChange(UiEvent.OnFavouritesFilterClick(FilterOption.Favourites))
        productViewModel.updateViewModel(UiState(isLoading = false, successGetProducts = emptyList()))
        composeRule.onNodeWithText("Ditemukan 0 Produk").assertIsDisplayed()


        // perform Not Favourite Click and should display 10 products
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(FilterOption.NotFavourites.text).performClick()
        productViewModel.onEventChange(UiEvent.OnFavouritesFilterClick(FilterOption.NotFavourites))
        productViewModel.updateViewModel(UiState(isLoading = false, successGetProducts = MockData.productSpecList))
        composeRule.onNodeWithText("Ditemukan 10 Produk").assertIsDisplayed()
    }

    @Test
    fun productListScreenAddRemoveFavouritesProductsTest() {
        // All Filter should displays
        composeRule.onNodeWithText("Semua").assertIsDisplayed()
        composeRule.onNodeWithText("Favorit").assertIsDisplayed()
        composeRule.onNodeWithText("Tidak Favorit").assertIsDisplayed()

        // Add First Product as Favourites
        composeRule.onNodeWithTag(MockData.productSpecList[0].name).performClick()
        productViewModel.onEventChange(UiEvent.OnFavouritesProductSelected(MockData.productSpecList[0]))
        val updatedProduct = MockData.productSpecList[0].copy(isFavourite = true)
        val updatedList =
            MockData.productSpecList.map { if (it.id == MockData.productSpecList[0].id) updatedProduct else it }
        productViewModel.updateViewModel(UiState(isLoading = false, successGetProducts = updatedList))
        composeRule.onNodeWithContentDescription("Unsave from favourites").assertIsDisplayed()

        // click favourite filter and it should display 1 product in the list which is the favourites product
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(FilterOption.Favourites.text).performClick()
        productViewModel.onEventChange(UiEvent.OnFavouritesFilterClick(FilterOption.Favourites))
        productViewModel.updateViewModel(UiState(isLoading = false, successGetProducts = listOf(updatedProduct)))
        composeRule.onNodeWithText("Ditemukan 1 Produk").assertIsDisplayed()
        composeRule.onNodeWithTag(updatedProduct.name).assertIsDisplayed()

        // Click Unfavourite while filter still point at Favourite and should display nothing
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(updatedProduct.name).performClick()
        productViewModel.onEventChange(UiEvent.OnFavouritesProductSelected(MockData.productSpecList[0]))
        productViewModel.updateViewModel(UiState(isLoading = false, successGetProducts = emptyList()))

    }
}