package com.example.shopmobileapplication

import com.example.shopmobileapplication.ui.main.search.ProductFilter
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ProductFilterBuilderTest {

    @Test
    fun `build should return ProductFilter with correct values`() {
        // Arrange
        val builder = ProductFilter.Builder()
            .minPrice(10.0)
            .maxPrice(20.0)
            .size(listOf(1.0, 2.0, 3.0))
            .storeIds(listOf(1, 2, 3))

        // Act
        val productFilter = builder.build()

        // Assert
        assertEquals(10.0, productFilter.minPrice)
        assertEquals(20.0, productFilter.maxPrice)
        assertEquals(listOf(1.0, 2.0, 3.0), productFilter.sizes)
        assertEquals(listOf(1, 2, 3), productFilter.stores)
    }

    @Test
    fun `build should return ProductFilter with null values when not set`() {
        // Arrange
        val builder = ProductFilter.Builder()

        // Act
        val productFilter = builder.build()

        // Assert
        assertNull(productFilter.minPrice)
        assertNull(productFilter.maxPrice)
        assertNull(productFilter.sizes)
        assertNull(productFilter.stores)
    }

    @Test
    fun `addSize should add size to sizes list`() {
        // Arrange
        val builder = ProductFilter.Builder()
            .addSize(1.0)
            .addSize(2.0)

        // Act
        val productFilter = builder.build()

        // Assert
        assertEquals(listOf(1.0, 2.0), productFilter.sizes)
    }

    @Test
    fun `removeSize should remove size from sizes list`() {
        // Arrange
        val builder = ProductFilter.Builder()
            .size(listOf(1.0, 2.0, 3.0))
            .removeSize(2.0)

        // Act
        val productFilter = builder.build()

        // Assert
        assertEquals(listOf(1.0, 3.0), productFilter.sizes)
    }
}

