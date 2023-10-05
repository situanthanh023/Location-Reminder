package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

        repository = RemindersLocalRepository(database.reminderDao())
    }

    @After
    fun cleanUp() = database.close()
    @Test
    fun test_WithDataInsertRetrieve() = runBlocking {

        val dataExpected = ReminderDTO(
            "title 123",
            "description 123",
            "location 123",
            45.00,
            45.00)

        repository.saveReminder(dataExpected)

        val result = repository.getReminder(dataExpected.id)

        result as Result.Success
        MatcherAssert.assertThat(result.data != null, CoreMatchers.`is`(true))

        val dataActual = result.data
        MatcherAssert.assertThat(dataActual.id, CoreMatchers.`is`(dataExpected.id))
        MatcherAssert.assertThat(dataActual.title, CoreMatchers.`is`(dataExpected.title))
        MatcherAssert.assertThat(dataActual.description, CoreMatchers.`is`(dataExpected.description))
        MatcherAssert.assertThat(dataActual.location, CoreMatchers.`is`(dataExpected.location))
        MatcherAssert.assertThat(dataActual.latitude, CoreMatchers.`is`(dataExpected.latitude))
        MatcherAssert.assertThat(dataActual.longitude, CoreMatchers.`is`(dataExpected.longitude))
    }

    @Test
    fun test_WithDataNotFoundAndReturnError() = runBlocking {
        val result = repository.getReminder("456")
        val actual =  (result is Result.Error)
        MatcherAssert.assertThat(actual, CoreMatchers.`is`(true))
    }
}