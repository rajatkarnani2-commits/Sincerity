package com.example.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun getTaskById(id: Int): Task? = taskDao.getTaskById(id)

    suspend fun getYesterdayMissedTasks(todayStart: Long): List<Task> =
        taskDao.getYesterdayMissedTasks(todayStart)

    suspend fun getTasksForLast7Days(sevenDaysAgo: Long): List<Task> =
        taskDao.getTasksForLast7Days(sevenDaysAgo)

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(id: Int) = taskDao.deleteTask(id)
}
