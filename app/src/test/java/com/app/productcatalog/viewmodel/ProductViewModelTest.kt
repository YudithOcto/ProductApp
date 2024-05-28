package com.app.productcatalog.viewmodel

import com.app.productcatalog.domain.model.ProductSpec
import com.app.productcatalog.domain.repository.ProductRepository
import com.app.productcatalog.mockdata.MockData
import com.app.productcatalog.rules.InstantTaskExecutorRule
import com.app.productcatalog.scheduler.TaskSchedulerProvider
import com.app.productcatalog.ui.FilterOption
import com.app.productcatalog.ui.ProductListViewModel
import com.app.productcatalog.ui.UiEvent
import com.app.productcatalog.ui.UiState
import com.app.productcatalog.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnitPlatform::class)
class ProductViewModelTest : Spek({
    InstantTaskExecutorRule(this)

    Feature("Product View Model Test") {
        val dispatcher = TaskSchedulerProvider()
        lateinit var sut: ProductListViewModel
        lateinit var productRepo: ProductRepository


        beforeEachScenario {
            Dispatchers.setMain(dispatcher.testDispatcher)
            productRepo = mockk(relaxed = true)
            sut = ProductListViewModel(productRepo, dispatcher)
        }

        afterEachScenario {
            Dispatchers.resetMain()
        }

        Scenario("Refresh Product Successful") {
            val expectedResponse = Result.Success(MockData.productSpecList)
            Given("Get Product List") {
                coEvery { productRepo.getProducts(any(), any()) } returns flowOf(expectedResponse)
            }
            When("SUT called") {
                runTest {
                    sut.refreshProducts()
                }
            }
            Then("Assert result") {
                runTest {
                    assertEquals(
                        UiState(isLoading = false, successGetProducts = MockData.productSpecList),
                        sut.uiState.value
                    )
                }
            }
        }

        Scenario("Refresh Product Failed") {
            val expectedException = RuntimeException("Database error")
            Given("Get Product List") {
                coEvery { productRepo.getProducts(any(), any()) } returns flowOf(
                    Result.Error(
                        expectedException
                    )
                )
            }
            When("SUT called") {
                runTest {
                    sut.refreshProducts()
                }
            }
            Then("Assert result") {
                runTest {
                    sut.uiState.value.error?.consumeOnce {
                        assertEquals(it.message, expectedException.message)
                    }
                }
            }
        }

        Scenario("Insert Product Failed") {
            val expectedException = RuntimeException("Database error")
            Given("Get Product List") {
                coEvery { productRepo.insertProducts() } returns flowOf(
                    Result.Error(
                        expectedException
                    )
                )
            }
            When("SUT called") {
                runTest {
                    sut.insertProducts()
                }
            }
            Then("Assert result") {
                runTest {
                    sut.uiState.value.error?.consumeOnce {
                        assertEquals(it.message, expectedException.message)
                    }
                }
            }
        }

        Scenario("Change Favourite Product State Success") {
            val product = ProductSpec(
                id = "2",
                name = "Laptop",
                description = "Powerful gaming laptop",
                price = 1299.99,
                isFavourite = false,
                imageUrl = "https://images-cdn.ubuy.co.id/650188aee584193d8350a66b-2022-apple-macbook-pro-laptop-with-m2.jpg"
            )
            val modifiedProduct = product.copy(isFavourite = true)

            Given("Get Product List. The product repo should be using modified product because it is being called after modification") {
                coEvery { productRepo.updateProductFavourite(modifiedProduct) } returns flowOf(
                    Result.Success(true)
                )
                coEvery { productRepo.getProducts(any(), any()) } returns flowOf(
                    Result.Success(MockData.productSpecList)
                )
            }
            When("SUT called") {
                runTest {
                    sut.refreshProducts()
                    sut.onEventChange(UiEvent.OnFavouritesProductSelected(product))
                }
            }
            Then("Verify is Update Product getting called and result would be the same") {
                runTest {
                    coVerify { productRepo.updateProductFavourite(modifiedProduct) }
                    assertEquals(false, sut.uiState.value.isLoading)
                    coVerify { productRepo.getProducts(any(), any()) }
                }
            }
        }

        Scenario("Change Favourite Product State Failure") {
            val expectedException = RuntimeException("Database error")
            val product = ProductSpec(
                id = "2",
                name = "Laptop",
                description = "Powerful gaming laptop",
                price = 1299.99,
                isFavourite = false,
                imageUrl = "https://images-cdn.ubuy.co.id/650188aee584193d8350a66b-2022-apple-macbook-pro-laptop-with-m2.jpg"
            )
            val modifiedProduct = product.copy(isFavourite = true)

            Given("Get Product List. The product repo should be using modified product because it is being called after modification") {
                coEvery { productRepo.updateProductFavourite(modifiedProduct) } returns flowOf(
                    Result.Error(expectedException)
                )
                coEvery { productRepo.getProducts(any(), any()) } returns flowOf(
                    Result.Success(
                        MockData.productSpecList
                    )
                )
            }
            When("SUT called") {
                runTest {
                    sut.onEventChange(UiEvent.OnFavouritesProductSelected(product))
                }
            }
            Then("Verify is Update Product getting called and result would be the same") {
                runTest {
                    coVerify { productRepo.updateProductFavourite(modifiedProduct) }
                    sut.uiState.value.error?.consumeOnce {
                        assertEquals(expectedException.message, it.message)
                    }
                    coVerify { productRepo.getProducts(any(), any()) }
                }
            }
        }

        Scenario("Change Favourite Product State Success without any product List") {
            val expectedException = RuntimeException("Database error")
            val product = ProductSpec(
                id = "2",
                name = "Laptop",
                description = "Powerful gaming laptop",
                price = 1299.99,
                isFavourite = false,
                imageUrl = "https://images-cdn.ubuy.co.id/650188aee584193d8350a66b-2022-apple-macbook-pro-laptop-with-m2.jpg"
            )
            val modifiedProduct = product.copy(isFavourite = true)

            Given("Get Product List. The product repo should be using modified product because it is being called after modification") {
                coEvery { productRepo.updateProductFavourite(modifiedProduct) } returns flowOf(
                    Result.Success(true)
                )
                coEvery { productRepo.getProducts(any(), any()) } returns flowOf(
                    Result.Error(expectedException)
                )
            }
            When("SUT called") {
                runTest {
                    sut.onEventChange(UiEvent.OnFavouritesProductSelected(product))
                }
            }
            Then("Verify is Update Product getting called and result would be the same") {
                runTest {
                    coVerify { productRepo.updateProductFavourite(modifiedProduct) }
                    coVerify { productRepo.getProducts(any(), any()) }
                    assertEquals(false, sut.uiState.value.isLoading)
                }
            }
        }

        Scenario("Update Search Query") {
            When("On Search Event Triggered") {
                runTest { sut.onEventChange(UiEvent.OnUpdateSearchQuery("newQuery")) }
            }
            Then("Assert UI value and refresh product list called with correct parameter value") {
                runTest {
                    assertEquals("newQuery", sut.uiState.value.currentSearchQuery)
                    coVerify { productRepo.getProducts(any(), "newQuery") }
                }
            }
        }

        Scenario("Update Selected Filter") {
            When("On Filter Event Triggered") {
                runTest { sut.onEventChange(UiEvent.OnFavouritesFilterClick(FilterOption.Favourites)) }
            }
            Then("Assert UI value and refresh product list called with correct parameter value") {
                runTest {
                    assertEquals(FilterOption.Favourites, sut.uiState.value.activeFilter)
                    coVerify { productRepo.getProducts(1, any()) }
                }
            }
        }
    }
})