package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.DisasterRepository
import com.example.data.local.AppDatabase
import com.example.data.local.DefaultPreparednessTasks
import com.example.data.local.PreparednessTaskDao
import com.example.data.local.PreparednessTaskEntity
import com.example.ui.DisasterViewModel
import com.example.ui.components.DisasterPreparednessChecklist
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PreparednessChecklistTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: PreparednessTaskDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.preparednessTaskDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `Room DAO inserts and updates tasks offline`() = runBlocking {
        dao.insertAll(DefaultPreparednessTasks.items)
        val initialCount = dao.getTotalCount()
        assertEquals(DefaultPreparednessTasks.items.size, initialCount)

        // Toggle task completion
        val firstTask = DefaultPreparednessTasks.items.first()
        dao.updateTaskCompletion(firstTask.id, false)
        val updatedTasks = dao.getAllTasks().first()
        val found = updatedTasks.find { it.id == firstTask.id }
        assertFalse(found!!.isCompleted)

        // Set all completed
        dao.setAllCompleted(true)
        val allCompletedTasks = dao.getAllTasks().first()
        assertTrue(allCompletedTasks.all { it.isCompleted })
    }

    @Test
    fun `DisasterViewModel toggles safety tasks and preserves state`() {
        val viewModel = DisasterViewModel()
        val initialTasks = viewModel.preparednessTasks.value
        assertTrue("Default tasks should be populated", initialTasks.isNotEmpty())

        val targetId = "prep_documents"
        val initialStatus = initialTasks.find { it.id == targetId }?.isCompleted ?: false

        viewModel.togglePreparednessTask(targetId)
        val toggledStatus = viewModel.preparednessTasks.value.find { it.id == targetId }?.isCompleted
        assertEquals(!initialStatus, toggledStatus)

        // Reset all
        viewModel.resetAllPreparednessTasks()
        assertTrue(viewModel.preparednessTasks.value.none { it.isCompleted })

        // Complete all
        viewModel.completeAllPreparednessTasks()
        assertTrue(viewModel.preparednessTasks.value.all { it.isCompleted })
    }

    @Test
    fun `DisasterPreparednessChecklist renders correctly and responds to clicks`() {
        val viewModel = DisasterViewModel()

        composeTestRule.setContent {
            DisasterPreparednessChecklist(viewModel = viewModel)
        }

        // Verify checklist card and offline badge
        composeTestRule.onNodeWithTag("preparedness_checklist_card").assertIsDisplayed()
        composeTestRule.onNodeWithTag("offline_status_badge").assertIsDisplayed()
        composeTestRule.onNodeWithTag("preparedness_progress_bar").assertIsDisplayed()

        // Verify critical task exists and can be clicked
        composeTestRule.onNodeWithTag("task_toggle_prep_go_bag").assertExists()
        composeTestRule.onNodeWithTag("task_toggle_prep_go_bag").performClick()

        // Verify category chip click
        composeTestRule.onNodeWithTag("prep_category_CRITICAL").performClick()
        assertEquals("CRITICAL", viewModel.selectedPrepCategory.value)

        // Verify reset button click
        composeTestRule.onNodeWithTag("reset_checklist_button").performClick()
        assertTrue(viewModel.preparednessTasks.value.none { it.isCompleted })
    }
}
