package com.example.shopmobileapplication

import com.example.shopmobileapplication.utils.getAllSizesRus
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GetAllSizesRusTest {

    @Test
    fun `getAllSizesRus should return correct list for positive range`() {
        // Arrange
        val start = 1.0
        val end = 5.0
        val step = 1.0
        val expected = listOf(1.0, 2.0, 3.0, 4.0, 5.0)

        // Act
        val result = getAllSizesRus(start, end, step)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getAllSizesRus should return correct list for negative range`() {
        // Arrange
        val start = -5.0
        val end = -1.0
        val step = 1.0
        val expected = emptyList<Double>()

        // Act
        val result = getAllSizesRus(start, end, step)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getAllSizesRus should return correct list for zero step`() {
        // Arrange
        val start = 1.0
        val end = 5.0
        val step = 0.0
        val expected = listOf(1.0)

        // Act
        val result = getAllSizesRus(start, end, step)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getAllSizesRus should return correct list for fractional step`() {
        // Arrange
        val start = 1.0
        val end = 2.5
        val step = 0.5
        val expected = listOf(1.0, 1.5, 2.0, 2.5)

        // Act
        val result = getAllSizesRus(start, end, step)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getAllSizesRus should return correct list for negative step`() {
        // Arrange
        val start = 5.0
        val end = 1.0
        val step = -1.0
        val expected = listOf(5.0)

        // Act
        val result = getAllSizesRus(start, end, step)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getAllSizesRus should return correct list for single element`() {
        // Arrange
        val start = 1.0
        val end = 1.0
        val step = 1.0
        val expected = listOf(1.0)

        // Act
        val result = getAllSizesRus(start, end, step)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getAllSizesRus should return correct list for empty range`() {
        // Arrange
        val start = 1.0
        val end = 0.0
        val step = 1.0
        val expected = listOf(1.0)

        // Act
        val result = getAllSizesRus(start, end, step)

        // Assert
        assertEquals(expected, result)
    }
}
