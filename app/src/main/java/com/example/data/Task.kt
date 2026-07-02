package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val deadlineTime: Long, // Epoch milliseconds
    val status: String, // "Pending", "Completed", "Missed", "Rollover"
    val createdAt: Long = System.currentTimeMillis()
)
