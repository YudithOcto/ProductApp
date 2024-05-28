package com.app.productcatalog.productmapper

import com.app.productcatalog.data.model.ProductDto
import com.app.productcatalog.domain.mapper.DefaultProductMapper
import com.app.productcatalog.domain.mapper.ProductMapper
import com.app.productcatalog.domain.model.ProductSpec
import com.app.productcatalog.mockdata.MockData
import com.app.productcatalog.rules.InstantTaskExecutorRule
import com.app.productcatalog.scheduler.TaskSchedulerProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@RunWith(JUnitPlatform::class)
class ProductMapperTest : Spek({
    InstantTaskExecutorRule(this)

    Feature("") {
        val dispatcher = TaskSchedulerProvider()
        val sut: ProductMapper = DefaultProductMapper()

        beforeEachScenario {
            Dispatchers.setMain(dispatcher.testDispatcher)
        }

        afterEachScenario {
            Dispatchers.resetMain()
        }

        Scenario("Convert Product to Spec") {
            val expectedProductDto = MockData.productDtoList.first()
            var actualProductSpec: ProductSpec? = null
            When("SUT called") {
                actualProductSpec = sut.convertToProductSpec(expectedProductDto)
            }
            Then("Assert") {
                assertEquals(expectedProductDto.id, actualProductSpec?.id)
                assertEquals(expectedProductDto.name, actualProductSpec?.name)
                assertEquals(expectedProductDto.description, actualProductSpec?.description)
                assertEquals(expectedProductDto.price, actualProductSpec?.price)
                assertEquals(expectedProductDto.imageUrl, actualProductSpec?.imageUrl)
                assertEquals(expectedProductDto.isFavourite, actualProductSpec?.isFavourite)
            }
        }

        Scenario("Convert Spec to ProductDto") {
            val expectedProductSpec = MockData.productSpecList.first()
            var actualProductDto: ProductDto? = null
            When("SUT called") {
                actualProductDto = sut.convertToProductDto(expectedProductSpec)
            }
            Then("Assert") {
                assertEquals(expectedProductSpec.id, actualProductDto?.id)
                assertEquals(expectedProductSpec.name, actualProductDto?.name)
                assertEquals(expectedProductSpec.description, actualProductDto?.description)
                assertEquals(expectedProductSpec.price, actualProductDto?.price)
                assertEquals(expectedProductSpec.imageUrl, actualProductDto?.imageUrl)
                assertEquals(expectedProductSpec.isFavourite, actualProductDto?.isFavourite)
            }
        }
    }
})