package com.example.urbanquest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.urbanquest.SearchScreens.ItemFromDBViewModel
import com.example.urbanquest.SearchScreens.data.ItemFromDB
import com.example.urbanquest.SearchScreens.data.WorkingTime
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ItemFromDBViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testSelectPlace() {
        val viewModel = ItemFromDBViewModel()

        val testWorkingTime = mapOf(
            "Monday" to WorkingTime(true, "09:00", "18:00"),
            "Tuesday" to WorkingTime(false, "", "")
        )

        val testTags = mapOf("park" to "true", "museum" to "false")

        val testPlace = ItemFromDB(
            name = "Test Place",
            address = "123 Test Street",
            timeOpen = "09:00",
            timeClose = "18:00",
            rate = 4.5,
            description = "A test place for unit testing",
            geopointLatitude = "55.7558",
            geopointLongtitude = "37.6173",
            imageURL = "https://example.com/image.jpg",
            tags = testTags,
            workingTime = testWorkingTime,
            matchCount = 2
        )

        val observer = Observer<ItemFromDB?> {}

        try {
            viewModel.selectedPlace.observeForever(observer)
            viewModel.selectPlace(testPlace)

            val selected = viewModel.selectedPlace.value
            assertEquals(testPlace, selected)
        } finally {
            viewModel.selectedPlace.removeObserver(observer)
        }
    }
}