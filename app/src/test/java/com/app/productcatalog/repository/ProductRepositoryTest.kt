package com.app.productcatalog.repository

import com.app.productcatalog.data.local.ProductsDao
import com.app.productcatalog.domain.mapper.ProductMapper
import com.app.productcatalog.domain.model.ProductSpec
import com.app.productcatalog.domain.repository.DefaultProductRepository
import com.app.productcatalog.domain.repository.ProductRepository
import com.app.productcatalog.mockdata.MockData
import com.app.productcatalog.rules.InstantTaskExecutorRule
import com.app.productcatalog.scheduler.TaskSchedulerProvider
import com.app.productcatalog.util.Result
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@RunWith(JUnitPlatform::class)
class ProductRepositoryTest : Spek({
    InstantTaskExecutorRule(this)

    Feature("Get Products") {
        val dispatcher = TaskSchedulerProvider()

        lateinit var sut: ProductRepository
        lateinit var productDao: ProductsDao
        lateinit var productMapper: ProductMapper

        beforeEachScenario {
            Dispatchers.setMain(dispatcher.testDispatcher)
            productDao = mockk(relaxed = true)
            productMapper = mockk(relaxed = true)
            sut = DefaultProductRepository(
                productList = MockData.productDtoList,
                productMapper = productMapper,
                schedulerProvider = dispatcher,
                productDao = productDao,
            )
        }

        afterEachScenario {
            Dispatchers.resetMain()
        }

        Scenario("Get Products Success") {
            var actualResponse: Flow<Result<List<ProductSpec>>>? = null
            Given("Add Products to Database") {
                coEvery { productDao.getProducts(-1, "") } returns MockData.productDtoList
            }
            When("SUT called") {
                runTest {
                    actualResponse = sut.getProducts(-1, "")
                }
            }
            Then("Assert Data") {
                runTest {
                    Assert.assertEquals(
                        Result.Success(MockData.productSpecList).data.size,
                        (actualResponse?.first() as? Result.Success)?.data?.size
                    )
                    coVerify { productDao.getProducts(any(), any()) }
                }
            }
        }

        Scenario("Get Products Failed") {
            var actualResponse: Flow<Result<List<ProductSpec>>>? = null
            val expectedException = RuntimeException("Database error")

            Given("Throw Database Exception") {
                coEvery { productDao.getProducts(any(), any()) } throws expectedException
            }
            When("SUT called") {
                runTest {
                    actualResponse = sut.getProducts(-1, "")
                }
            }
            Then("Assert Error") {
                runTest {
                    val result = actualResponse?.first()
                    Assert.assertTrue(result is Result.Error)
                    Assert.assertEquals(expectedException, (result as? Result.Error)?.exception)
                    coVerify { productDao.getProducts(any(), any()) }
                }
            }
        }

        Scenario("Insert Products Successful") {
            Given("insert products") {
                coEvery { productDao.insertAllProduct(*anyVararg()) } just Runs
            }
            When("Trigger insertion to database") {
                runTest {
                    sut.insertProducts()
                }
            }
            Then("verify if dao is getting called") {
                runTest {
                    // needed to collect the result because of the flow nature
                    sut.insertProducts().first()
                }
                coVerify { productDao.insertAllProduct(*MockData.productDtoList.toTypedArray()) }
            }
        }

        Scenario("Insert Products failed") {
            val expectedException = RuntimeException("Database error")
            var actualResponse: Flow<Result<Unit>>? = null
            Given("insert products failure") {
                coEvery { productDao.insertAllProduct(*anyVararg()) } throws expectedException
            }
            When("Trigger insertion to database") {
                runTest {
                    actualResponse = sut.insertProducts()
                }
            }
            Then("verify if dao is getting called and getting throwable message") {
                runTest {
                    // needed to collect the result because of the flow nature
                    sut.insertProducts().first()
                    coVerify { productDao.insertAllProduct(*MockData.productDtoList.toTypedArray()) }
                    Assert.assertEquals(expectedException.message, (actualResponse?.first() as? Result.Error)?.exception?.message)
                }
            }
        }

        Scenario("Update Favourites Successful") {
            val data = ProductSpec(
                id = "10",
                name = "External Hard Drive",
                description = "2TB external hard drive",
                price = 99.99,
                isFavourite = false,
                imageUrl = "https://www.sweetwater.com/sweetcare/media/2019/04/External-Hard-Drive-Hero.png"
            )

            var actualResponse: Result<Boolean>? = null
            val expectedResponse = flowOf(Result.Success(true))

            Given("update products") {
                coEvery { productDao.updateProduct(productMapper.convertToProductDto(data)) } just Runs
            }
            When("Trigger update product favourites to database") {
                runTest {
                    actualResponse = sut.updateProductFavourite(data).first()
                }
            }
            Then("verify if dao is getting called") {
                runTest {
                    coVerify { productDao.updateProduct(productMapper.convertToProductDto(data))  }
                    Assert.assertEquals(expectedResponse.first().data, (actualResponse as? Result.Success)?.data)
                }
            }
        }

        Scenario("Update Favourites Failure") {
            val expectedException = RuntimeException("Database error")

            val data = ProductSpec(
                id = "10",
                name = "External Hard Drive",
                description = "2TB external hard drive",
                price = 99.99,
                isFavourite = false,
                imageUrl = "https://www.sweetwater.com/sweetcare/media/2019/04/External-Hard-Drive-Hero.png"
            )

            var actualResponse: Result<Boolean>? = null
            val expectedResponse = flowOf(Result.Error(expectedException))

            Given("update products") {
                coEvery { productDao.updateProduct(productMapper.convertToProductDto(data)) } throws expectedException
            }
            When("Trigger update product favourites to database") {
                runTest {
                    actualResponse = sut.updateProductFavourite(data).first()
                }
            }
            Then("verify if dao is getting called") {
                runTest {
                    coVerify { productDao.updateProduct(productMapper.convertToProductDto(data))  }
                    Assert.assertEquals(expectedResponse.first().exception?.message, (actualResponse as? Result.Error)?.exception?.message)
                }
            }
        }
    }
})