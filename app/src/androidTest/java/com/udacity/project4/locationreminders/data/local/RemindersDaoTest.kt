package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun cleanUp(): Unit = database.close()

    @Test
    fun test_WithDataInsertRetrieve() = runBlockingTest {

        val dataExpected = ReminderDTO(
            "title 123",
            "description 123",
            "location 123",
            45.00,
            45.00)

        database.reminderDao().saveReminder(dataExpected)

        val dataList = database.reminderDao().getReminders()

        MatcherAssert.assertThat(dataList.size, `is`(1))

        val dataActual = dataList[0]
        MatcherAssert.assertThat(dataActual.id, `is`(dataExpected.id))
        MatcherAssert.assertThat(dataActual.title, `is`(dataExpected.title))
        MatcherAssert.assertThat(dataActual.description, `is`(dataExpected.description))
        MatcherAssert.assertThat(dataActual.location, `is`(dataExpected.location))
        MatcherAssert.assertThat(dataActual.latitude, `is`(dataExpected.latitude))
        MatcherAssert.assertThat(dataActual.longitude, `is`(dataExpected.longitude))

    }
}