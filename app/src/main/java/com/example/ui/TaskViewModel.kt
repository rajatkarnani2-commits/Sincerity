package com.example.ui

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiClient
import com.example.api.ResponseSchema
import com.example.api.SincerityReport
import com.example.api.MonthlyAnalysis
import com.example.data.AppDatabase
import com.example.data.Task
import com.example.data.TaskRepository
import com.example.receiver.AlarmReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repository: TaskRepository
    private val sharedPrefs = context.getSharedPreferences("sincerity_prefs", Context.MODE_PRIVATE)

    val allTasks: StateFlow<List<Task>>

    private val _userName = MutableStateFlow(sharedPrefs.getString("user_name", "Explorer") ?: "Explorer")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _profilePictureUri = MutableStateFlow(sharedPrefs.getString("profile_picture_uri", null))
    val profilePictureUri: StateFlow<String?> = _profilePictureUri.asStateFlow()

    private val _coachingMessage = MutableStateFlow<String?>(null)
    val coachingMessage: StateFlow<String?> = _coachingMessage.asStateFlow()

    private val _isCoachingLoading = MutableStateFlow(false)
    val isCoachingLoading: StateFlow<Boolean> = _isCoachingLoading.asStateFlow()

    private val _sincerityReport = MutableStateFlow<SincerityReport?>(null)
    val sincerityReport: StateFlow<SincerityReport?> = _sincerityReport.asStateFlow()

    private val _isInsightsLoading = MutableStateFlow(false)
    val isInsightsLoading: StateFlow<Boolean> = _isInsightsLoading.asStateFlow()

    private val _monthlyInsights = MutableStateFlow<List<MonthlyAnalysis>>(emptyList())
    val monthlyInsights: StateFlow<List<MonthlyAnalysis>> = _monthlyInsights.asStateFlow()

    private val _comparisonResult = MutableStateFlow<String?>(null)
    val comparisonResult: StateFlow<String?> = _comparisonResult.asStateFlow()

    private val _isComparisonLoading = MutableStateFlow(false)
    val isComparisonLoading: StateFlow<Boolean> = _isComparisonLoading.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(context)
        repository = TaskRepository(database.taskDao())

        allTasks = repository.allTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Load cached morning coaching message
        val cachedCoaching = sharedPrefs.getString("cached_coaching", null)
        val cachedCoachingDay = sharedPrefs.getLong("cached_coaching_day", 0L)
        val todayStart = getTodayStart()

        if (cachedCoachingDay == todayStart) {
            _coachingMessage.value = cachedCoaching
        }

        // Load cached sincerity report
        val cachedReportJson = sharedPrefs.getString("cached_report_json", null)
        if (cachedReportJson != null) {
            _sincerityReport.value = GeminiClient.parseSincerityReport(cachedReportJson)
        }

        // Load monthly insights
        loadOrCreateMonthlyInsights()

        // Trigger Morning Rollover Check
        triggerMorningRollover()
    }

    fun updateUserName(name: String) {
        sharedPrefs.edit().putString("user_name", name).apply()
        _userName.value = name
    }

    fun updateProfilePictureUri(uriString: String?) {
        sharedPrefs.edit().putString("profile_picture_uri", uriString).apply()
        _profilePictureUri.value = uriString
    }

    private fun getTodayStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun triggerMorningRollover() {
        viewModelScope.launch {
            val todayStart = getTodayStart()
            val missedTasks = repository.getYesterdayMissedTasks(todayStart)

            if (missedTasks.isNotEmpty()) {
                _isCoachingLoading.value = true
                val titles = missedTasks.map { it.title }
                val prompt = "The user failed these tasks: ${titles.joinToString(", ")}. Act as an aggressive accountability coach. Generate a firm, 2-sentence psychological motivational kick, and instruct them to finish these rolled-over items first."
                
                val coaching = GeminiClient.generateContent(
                    prompt = prompt,
                    systemInstruction = "You are a firm, no-nonsense, highly motivational psychological accountability coach. Keep your response strictly to exactly two sentences. Be direct, punchy, and firm."
                )

                if (coaching != null) {
                    _coachingMessage.value = coaching
                    sharedPrefs.edit()
                        .putString("cached_coaching", coaching)
                        .putLong("cached_coaching_day", todayStart)
                        .apply()
                }

                // Convert yesterday's missed tasks to Rollover status
                missedTasks.forEach { task ->
                    repository.updateTask(task.copy(status = "Rollover"))
                }
                _isCoachingLoading.value = false
            }
        }
    }

    private fun scheduleSingleAlarm(taskId: Int, triggerAtMillis: Long, alarmType: String, requestCode: Int) {
        if (triggerAtMillis <= System.currentTimeMillis()) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("TASK_ID", taskId)
            putExtra("ALARM_TYPE", alarmType)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun scheduleTaskAlarm(taskId: Int, triggerAtMillis: Long) {
        // 1. Exact deadline alarm (request code base suffix 0)
        scheduleSingleAlarm(taskId, triggerAtMillis, "deadline", taskId * 10)

        // 2. 15 minutes before (request code base suffix 1)
        val fifteenMinBefore = triggerAtMillis - (15 * 60 * 1000)
        scheduleSingleAlarm(taskId, fifteenMinBefore, "15_min", taskId * 10 + 1)

        // 3. 1 hour before (request code base suffix 2)
        val oneHourBefore = triggerAtMillis - (1 * 60 * 60 * 1000)
        scheduleSingleAlarm(taskId, oneHourBefore, "1_hour", taskId * 10 + 2)

        // 4. 2 hours before (request code base suffix 3)
        val twoHoursBefore = triggerAtMillis - (2 * 60 * 60 * 1000)
        scheduleSingleAlarm(taskId, twoHoursBefore, "2_hour", taskId * 10 + 3)

        // 5. 3 hours before (request code base suffix 4)
        val threeHoursBefore = triggerAtMillis - (3 * 60 * 60 * 1000)
        scheduleSingleAlarm(taskId, threeHoursBefore, "3_hour", taskId * 10 + 4)
    }

    fun cancelTaskAlarm(taskId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val suffixes = listOf(0, 1, 2, 3, 4)
        for (suffix in suffixes) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId * 10 + suffix,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }

    fun addTask(title: String, deadlineTime: Long) {
        viewModelScope.launch {
            val task = Task(
                title = title,
                deadlineTime = deadlineTime,
                status = "Pending"
            )
            val id = repository.insertTask(task)
            scheduleTaskAlarm(id.toInt(), deadlineTime)
        }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(status = "Completed"))
            cancelTaskAlarm(task.id)
        }
    }

    fun revertTaskStatus(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(status = "Pending"))
            scheduleTaskAlarm(task.id, task.deadlineTime)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task.id)
            cancelTaskAlarm(task.id)
        }
    }

    fun runWeeklySincerityAnalysis() {
        viewModelScope.launch {
            _isInsightsLoading.value = true
            val sevenDaysAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L
            val tasks = repository.getTasksForLast7Days(sevenDaysAgo)

            if (tasks.isEmpty()) {
                val emptyReport = SincerityReport(
                    sincerityScore = 0,
                    timelinessScore = 0,
                    consistencyScore = 0,
                    frictionPoints = listOf("No task history available for analysis."),
                    strategies = listOf("Create your first task", "Set deadline alerts", "Complete tasks on time"),
                    advice = "To begin analyzing your sincerity and timeliness, add and complete tasks over the week."
                )
                _sincerityReport.value = emptyReport
                _isInsightsLoading.value = false
                return@launch
            }

            val format = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
            val jsonString = tasks.joinToString(prefix = "[", postfix = "]") { task ->
                """{"title":"${task.title}","status":"${task.status}","deadline":"${format.format(java.util.Date(task.deadlineTime))}"}"""
            }

            val schema = ResponseSchema(
                type = "OBJECT",
                properties = mapOf(
                    "sincerityScore" to ResponseSchema(type = "INTEGER", description = "The overall Sincerity score out of 10"),
                    "timelinessScore" to ResponseSchema(type = "INTEGER", description = "The Timeliness sub-score out of 10"),
                    "consistencyScore" to ResponseSchema(type = "INTEGER", description = "The Consistency sub-score out of 10"),
                    "frictionPoints" to ResponseSchema(
                        type = "ARRAY",
                        items = ResponseSchema(type = "STRING"),
                        description = "List of behavioral friction points"
                    ),
                    "strategies" to ResponseSchema(
                        type = "ARRAY",
                        items = ResponseSchema(type = "STRING"),
                        description = "3 concrete strategies for next week"
                    ),
                    "advice" to ResponseSchema(type = "STRING", description = "Actionable coaching advice summarising findings")
                )
            )

            val prompt = "Analyze this data: $jsonString. Rate the user's Sincerity out of 10. List behavioral friction points and 3 concrete strategies for next week."

            val jsonResponse = GeminiClient.generateContent(
                prompt = prompt,
                systemInstruction = "You are a professional performance and sincerity analyst. You output detailed analysis of user task performance in raw JSON matching the requested schema. Ensure all fields are filled.",
                responseMimeType = "application/json",
                responseSchema = schema
            )

            if (jsonResponse != null) {
                val report = GeminiClient.parseSincerityReport(jsonResponse)
                if (report != null) {
                    _sincerityReport.value = report
                    sharedPrefs.edit().putString("cached_report_json", jsonResponse).apply()
                } else {
                    computeLocalReportFallback(tasks)
                }
            } else {
                computeLocalReportFallback(tasks)
            }
            _isInsightsLoading.value = false
        }
    }

    private fun computeLocalReportFallback(tasks: List<Task>) {
        val total = tasks.size
        val completed = tasks.count { it.status == "Completed" }
        val missed = tasks.count { it.status == "Missed" || it.status == "Rollover" }

        val ratio = if (total > 0) completed.toFloat() / total else 0f
        val sincerity = (ratio * 10).toInt()
        val timeliness = if (total > 0) ((1f - (missed.toFloat() / total)) * 10).toInt() else 0
        val consistency = if (completed > 0) 8 else 3

        val report = SincerityReport(
            sincerityScore = sincerity,
            timelinessScore = timeliness,
            consistencyScore = consistency,
            frictionPoints = listOf(
                "Completed $completed out of $total tasks.",
                "Missed $missed tasks over the past week."
            ),
            strategies = listOf(
                "Establish small, bite-sized daily objectives.",
                "Aim for consistent mid-day completions.",
                "Perform weekly task purges to remove low-priority noise."
            ),
            advice = "This is a locally generated summary based on your recent task history. Set your Gemini API key in the secrets panel to unlock full AI insights and personalized psychology coaching."
        )
        _sincerityReport.value = report
    }

    private fun loadOrCreateMonthlyInsights() {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, MonthlyAnalysis::class.java)
        val adapter = moshi.adapter<List<MonthlyAnalysis>>(type)
        val json = sharedPrefs.getString("monthly_insights", null)
        if (json != null) {
            try {
                val list = adapter.fromJson(json)
                if (list != null && list.isNotEmpty()) {
                    _monthlyInsights.value = list
                    return
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // Populate default values for June, July, August, September in order
        val defaultList = listOf(
            MonthlyAnalysis(
                monthName = "June",
                sincerityScore = 6,
                timelinessScore = 5,
                consistencyScore = 6,
                shortSummary = "Showed solid intent initially, but struggled with mid-week slumps and delayed task roll-overs.",
                positivePoint = "Strong morning task completions and excellent initial habit forming.",
                negativePoint = "High rate of task procrastination on Wednesdays and weekend slacking.",
                completedTasksCount = 18,
                totalTasksCount = 30
            ),
            MonthlyAnalysis(
                monthName = "July",
                sincerityScore = 8,
                timelinessScore = 7,
                consistencyScore = 8,
                shortSummary = "Significant improvement in task discipline with early completions and minimal delayed tasks.",
                positivePoint = "Excellent morning routine consistency and proactive task execution.",
                negativePoint = "Late evening tasks were frequently missed or rolled over to the next morning.",
                completedTasksCount = 25,
                totalTasksCount = 32
            ),
            MonthlyAnalysis(
                monthName = "August",
                sincerityScore = 5,
                timelinessScore = 4,
                consistencyScore = 5,
                shortSummary = "Summer slacking and travel disrupted daily schedule, leading to lower alarm response rates.",
                positivePoint = "Maintained a clear and transparent log of missed tasks without deleting them.",
                negativePoint = "Rolled over 40% of critical tasks and missed several consecutive weekend deadlines.",
                completedTasksCount = 12,
                totalTasksCount = 28
            ),
            MonthlyAnalysis(
                monthName = "September",
                sincerityScore = 9,
                timelinessScore = 8,
                consistencyScore = 9,
                shortSummary = "Outstanding recovery with a highly streamlined, realistic task load and zero clutter.",
                positivePoint = "Maintained a 4-day streak of zero missed deadlines and excellent weekend focus.",
                negativePoint = "Slightly over-ambitious on Mondays, causing minor evening friction.",
                completedTasksCount = 28,
                totalTasksCount = 31
            )
        )
        _monthlyInsights.value = defaultList
        saveMonthlyInsights(defaultList)
    }

    fun saveMonthlyInsights(list: List<MonthlyAnalysis>) {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, MonthlyAnalysis::class.java)
        val adapter = moshi.adapter<List<MonthlyAnalysis>>(type)
        val json = adapter.toJson(list)
        sharedPrefs.edit().putString("monthly_insights", json).apply()
    }

    fun runMonthlyComparison(selectedMonths: List<MonthlyAnalysis>) {
        if (selectedMonths.isEmpty()) {
            _comparisonResult.value = null
            return
        }
        viewModelScope.launch {
            _isComparisonLoading.value = true
            
            // Build the prompt for comparison
            val dataStr = selectedMonths.joinToString("\n") { m ->
                "- ${m.monthName}: Sincerity: ${m.sincerityScore}/10, Timeliness: ${m.timelinessScore}/10, Consistency: ${m.consistencyScore}/10, Tasks: ${m.completedTasksCount}/${m.totalTasksCount}. Summary: ${m.shortSummary}. Positive: ${m.positivePoint}. Negative: ${m.negativePoint}."
            }
            
            val prompt = """
                Compare these monthly performance reports from my task manager:
                $dataStr
                
                Please provide:
                1. A ranked list of these months (from best to worst) based on my task performance and sincerity, detailing why.
                2. A distinct breakdown of each month's major positive (+) point and negative (-) point.
                3. Actionable, psychologically sound advice on what specific behavioral friction I need to address.
                
                Keep the tone extremely professional, direct, insightful, and motivating. Keep the response concise but highly tailored. Format using markdown.
            """.trimIndent()
            
            val aiResponse = GeminiClient.generateContent(
                prompt = prompt,
                systemInstruction = "You are a senior behavioral coach and productivity analyst. Compare performance across months, rank them, analyze pros/cons, and give actionable feedback. Use markdown format."
            )
            
            if (aiResponse != null) {
                _comparisonResult.value = aiResponse
            } else {
                // High-fidelity Local Fallback comparison!
                val ranked = selectedMonths.sortedWith(
                    compareByDescending<MonthlyAnalysis> { it.sincerityScore }
                        .thenByDescending { it.completedTasksCount.toFloat() / it.totalTasksCount.coerceAtLeast(1) }
                )
                
                val builder = java.lang.StringBuilder()
                builder.append("### 📊 Sincerity & Performance Ranking (Local Analytics)\n\n")
                ranked.forEachIndexed { index, m ->
                    val completionRate = if (m.totalTasksCount > 0) (m.completedTasksCount * 100 / m.totalTasksCount) else 0
                    builder.append("${index + 1}. **${m.monthName}** (Sincerity Score: **${m.sincerityScore}/10**)\n")
                    builder.append("   - Completion rate of **$completionRate%** (${m.completedTasksCount}/${m.totalTasksCount} tasks)\n")
                    builder.append("   - *Analysis:* ${m.shortSummary}\n\n")
                }
                
                builder.append("### 🔍 Key Strengths & Friction Points\n\n")
                selectedMonths.forEach { m ->
                    builder.append("#### 🗓️ ${m.monthName}\n")
                    builder.append("🟢 **Positive Point:** ${m.positivePoint}\n")
                    builder.append("🔴 **Negative Point:** ${m.negativePoint}\n\n")
                }
                
                builder.append("### 💡 Recommended Behavioral Adjustments\n")
                builder.append("- **Schedule Consistency**: Align high-intensity tasks with your demonstrated peak periods.\n")
                builder.append("- **Friction Reduction**: Break down recurring negative points by reducing day-to-day task complexity.\n")
                builder.append("- *Enable Gemini API in the Secrets panel to unlock full AI-powered cross-month behavioral psychology feedback.*")
                
                _comparisonResult.value = builder.toString()
            }
            _isComparisonLoading.value = false
        }
    }
    
    fun clearComparison() {
        _comparisonResult.value = null
    }
}
