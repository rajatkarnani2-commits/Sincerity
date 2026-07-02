package com.example

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.api.SincerityReport
import com.example.data.Task
import com.example.ui.TaskViewModel
import com.example.ui.theme.AlertBackground
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CharcoalBlack
import com.example.ui.theme.CoachBackground
import com.example.ui.theme.CompletedBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NavBarBackground
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SoftCyan
import com.example.ui.theme.SurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibrantBlue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.io.File
import android.net.Uri
import android.provider.MediaStore
import android.content.ContentUris
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.unit.Dp
import kotlin.math.absoluteValue
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutBack
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.offset

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var showSplash by remember { mutableStateOf(true) }
                if (showSplash) {
                    SplashScreen(onTimeout = { showSplash = false })
                } else {
                    MainAppScreen()
                }
            }
        }
    }
}

@Composable
fun MainAppScreen() {
    val context = LocalContext.current
    val viewModel: TaskViewModel = viewModel()
    var selectedTab by remember { mutableStateOf("tasks") }

    // Request Notification permission for Android 13+
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (!isGranted) {
            Toast.makeText(
                context,
                "Notifications help you stay accountable when deadlines are missed!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val tabs = listOf("tasks", "insights", "profile")
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        selectedTab = tabs[pagerState.currentPage]
    }

    LaunchedEffect(selectedTab) {
        val targetPage = tabs.indexOf(selectedTab)
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        containerColor = CharcoalBlack
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (tabs[page]) {
                "tasks" -> TasksTabScreen(viewModel = viewModel)
                "insights" -> InsightsTabScreen(viewModel = viewModel)
                "profile" -> ProfileTabScreen(viewModel = viewModel, hasPermission = hasNotificationPermission)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar(
        containerColor = NavBarBackground,
        tonalElevation = 8.dp,
        modifier = Modifier
            .shadow(16.dp)
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        NavigationBarItem(
            selected = selectedTab == "tasks",
            onClick = { onTabSelected("tasks") },
            label = { Text("Tasks", fontWeight = FontWeight.Medium) },
            icon = {
                Icon(
                    imageVector = Icons.Default.TaskAlt,
                    contentDescription = "Tasks"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = VibrantBlue,
                unselectedIconColor = TextSecondary,
                selectedTextColor = VibrantBlue,
                unselectedTextColor = TextSecondary,
                indicatorColor = SurfaceVariant
            ),
            modifier = Modifier.testTag("nav_tasks_tab")
        )
        NavigationBarItem(
            selected = selectedTab == "insights",
            onClick = { onTabSelected("insights") },
            label = { Text("Insights", fontWeight = FontWeight.Medium) },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = "Insights"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SoftCyan,
                unselectedIconColor = TextSecondary,
                selectedTextColor = SoftCyan,
                unselectedTextColor = TextSecondary,
                indicatorColor = SurfaceVariant
            ),
            modifier = Modifier.testTag("nav_insights_tab")
        )
        NavigationBarItem(
            selected = selectedTab == "profile",
            onClick = { onTabSelected("profile") },
            label = { Text("Profile", fontWeight = FontWeight.Medium) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NeonPurple,
                unselectedIconColor = TextSecondary,
                selectedTextColor = NeonPurple,
                unselectedTextColor = TextSecondary,
                indicatorColor = SurfaceVariant
            ),
            modifier = Modifier.testTag("nav_profile_tab")
        )
    }
}

@Composable
fun UserProfileAvatar(
    userName: String,
    profilePictureUri: String?,
    size: Dp,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .then(clickModifier),
        contentAlignment = Alignment.Center
    ) {
        if (profilePictureUri != null && !profilePictureUri.startsWith("preset:")) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(File(profilePictureUri))
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                error = painterResource(android.R.drawable.ic_menu_gallery),
                fallback = painterResource(android.R.drawable.ic_menu_gallery)
            )
        } else {
            val presetKey = if (profilePictureUri?.startsWith("preset:") == true) {
                val suffix = profilePictureUri.substringAfter("preset:")
                when (suffix) {
                    "1", "fox" -> "fox"
                    "2", "panda" -> "panda"
                    "3", "cat" -> "cat"
                    "4", "koala" -> "koala"
                    "5", "tiger" -> "tiger"
                    "6", "lion" -> "lion"
                    "7", "frog" -> "frog"
                    "8", "unicorn" -> "unicorn"
                    else -> "fox"
                }
            } else {
                val list = listOf("fox", "panda", "cat", "koala", "tiger", "lion", "frog", "unicorn")
                list[(userName.hashCode().absoluteValue % list.size)]
            }

            val (emoji, brush) = when (presetKey) {
                "fox" -> Pair("🦊", Brush.linearGradient(colors = listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6)))) // Cosmic Purple to Blue
                "panda" -> Pair("🐼", Brush.linearGradient(colors = listOf(Color(0xFF0D9488), Color(0xFF10B981)))) // Dark Teal to Emerald
                "cat" -> Pair("🐱", Brush.linearGradient(colors = listOf(Color(0xFFF59E0B), Color(0xFFEF4444)))) // Amber to Hot Red
                "koala" -> Pair("🐨", Brush.linearGradient(colors = listOf(Color(0xFF06B6D4), Color(0xFF2563EB)))) // Cyan to Royal Blue
                "tiger" -> Pair("🐯", Brush.linearGradient(colors = listOf(Color(0xFFF97316), Color(0xFFEA580C)))) // Orange to Dark Orange
                "lion" -> Pair("🦁", Brush.linearGradient(colors = listOf(Color(0xFFF43F5E), Color(0xFFBE123C)))) // Warm Rose to Deep Ruby
                "frog" -> Pair("🐸", Brush.linearGradient(colors = listOf(Color(0xFF84CC16), Color(0xFF15803D)))) // Lime to Forest Green
                "unicorn" -> Pair("🦄", Brush.linearGradient(colors = listOf(Color(0xFFEC4899), Color(0xFF7C3AED)))) // Pink Spark to Indigo
                else -> Pair("🦊", Brush.linearGradient(colors = listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6))))
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = (size.value * 0.52f).sp,
                    lineHeight = (size.value * 0.52f).sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun TopHeaderArea(userName: String, profilePictureUri: String?) {
    val currentDateStr = remember {
        val sdf = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
        sdf.format(Date())
    }

    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = currentDateStr.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$greeting, $userName",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = TextPrimary
            )
        }

        UserProfileAvatar(
            userName = userName,
            profilePictureUri = profilePictureUri,
            size = 44.dp,
            modifier = Modifier.border(2.dp, CharcoalBlack, CircleShape)
        )
    }
}

@Composable
fun TasksTabScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.allTasks.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val profilePictureUri by viewModel.profilePictureUri.collectAsState()
    val coachingMessage by viewModel.coachingMessage.collectAsState()
    val isCoachingLoading by viewModel.isCoachingLoading.collectAsState()

    val missedTasks = remember(tasks) { tasks.filter { it.status == "Missed" } }
    val regularTasks = remember(tasks) { tasks.filter { it.status != "Missed" } }

    var newTaskTitle by remember { mutableStateOf("") }
    var selectedDeadline by remember { mutableStateOf<Long?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. Top Header Area
        TopHeaderArea(userName = userName, profilePictureUri = profilePictureUri)

        // 2. Main Content Area (Scrollable Tasks List)
        Box(
            modifier = Modifier
                .weight(1.8f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp) // Leave space for Floating Input Area
            ) {
                // Warning alert at the very top of dashboard if missed tasks exist
                if (missedTasks.isNotEmpty()) {
                    item {
                        GlossyAlertCard(missedCount = missedTasks.size)
                    }
                }

                // Gemini Morning Rollover coaching banner
                if (isCoachingLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CardBackground)
                                .border(1.dp, SurfaceVariant, RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(color = SoftCyan, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Analyzing missed tasks...",
                                    color = TextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                } else if (coachingMessage != null) {
                    item {
                        CoachingBannerCard(message = coachingMessage!!)
                    }
                }

                if (tasks.isEmpty()) {
                    item {
                        EmptyStatePrompt()
                    }
                } else {
                    items(tasks, key = { it.id }) { task ->
                        TaskItemCard(
                            task = task,
                            onCompleteToggle = {
                                if (task.status == "Completed") {
                                    viewModel.revertTaskStatus(task)
                                } else {
                                    viewModel.completeTask(task)
                                }
                            },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                }
            }

            // 3. Floating Action Area (Minimalist sticky input field at bottom of list)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, CharcoalBlack.copy(alpha = 0.95f), CharcoalBlack)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                FloatingActionInputArea(
                    taskTitle = newTaskTitle,
                    onTitleChange = { newTaskTitle = it },
                    selectedDeadline = selectedDeadline,
                    onSelectTimeClick = {
                        val calendar = Calendar.getInstance()
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                val targetCal = Calendar.getInstance()
                                targetCal.set(Calendar.HOUR_OF_DAY, hour)
                                targetCal.set(Calendar.MINUTE, minute)
                                targetCal.set(Calendar.SECOND, 0)
                                targetCal.set(Calendar.MILLISECOND, 0)

                                // If time selected is in the past of today, make it tomorrow's alarm
                                if (targetCal.timeInMillis < System.currentTimeMillis()) {
                                    targetCal.add(Calendar.DAY_OF_YEAR, 1)
                                }
                                selectedDeadline = targetCal.timeInMillis
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                        ).show()
                    },
                    onAddTaskClick = {
                        if (newTaskTitle.isNotBlank()) {
                            val deadline = selectedDeadline ?: (System.currentTimeMillis() + 60 * 60 * 1000L) // Default 1hr from now
                            viewModel.addTask(newTaskTitle.trim(), deadline)
                            newTaskTitle = ""
                            selectedDeadline = null
                            Toast.makeText(context, "Task created with deadline alert!", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyStatePrompt() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp, horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.TaskAlt,
            contentDescription = "No tasks",
            tint = TextMuted,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No tasks registered yet.",
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Use the minimalist input field below to type a task and tap the clock icon to configure a native alarm.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun GlossyAlertCard(missedCount: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .testTag("alert_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AlertBackground),
        border = BorderStroke(1.dp, AlertRed.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(AlertRed)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Action Required",
                    tint = AlertRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "DEADLINE MISSED",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = AlertRed,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "You missed $missedCount deadline${if (missedCount > 1) "s" else ""}. Your integrity and sincerity scores are affected.",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}



@Composable
fun CoachingBannerCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .testTag("coaching_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CoachBackground),
        border = BorderStroke(1.dp, NeonPurple.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(NeonPurple)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ACCOUNTABILITY COACH",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = NeonPurple,
                    letterSpacing = 1.sp
                )
            }
            Text(
                text = "\"$message\"",
                color = TextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TaskItemCard(
    task: Task,
    onCompleteToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
    val timeStr = formatter.format(Date(task.deadlineTime))

    val (bgColor, borderColor, cardAlpha) = remember(task.status) {
        when (task.status) {
            "Completed" -> Triple(CompletedBackground, SoftCyan.copy(alpha = 0.2f), 0.7f)
            "Missed" -> Triple(AlertBackground, AlertRed.copy(alpha = 0.2f), 1.0f)
            "Rollover" -> Triple(CardBackground, NeonPurple.copy(alpha = 0.2f), 1.0f)
            else -> Triple(CardBackground, Color.White.copy(alpha = 0.05f), 1.0f)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .alpha(cardAlpha)
            .testTag("task_card_${task.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox status indicator
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (task.status == "Completed") SoftCyan.copy(alpha = 0.15f) else Color.Transparent
                        )
                        .border(
                            width = 1.5.dp,
                            color = if (task.status == "Completed") SoftCyan else borderColor,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { onCompleteToggle() }
                        .testTag("checkbox_${task.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    if (task.status == "Completed") {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = SoftCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (task.status == "Completed") TextDecoration.LineThrough else null
                        ),
                        color = if (task.status == "Completed") TextMuted else TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Time pill metadata
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceVariant)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Alert Time",
                            tint = TextMuted,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$timeStr | ${task.status}",
                            fontSize = 11.sp,
                            color = when (task.status) {
                                "Completed" -> SoftCyan
                                "Missed" -> AlertRed
                                "Rollover" -> NeonPurple
                                else -> TextSecondary
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("delete_${task.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete task",
                    tint = TextMuted
                )
            }
        }
    }
}

@Composable
fun FloatingActionInputArea(
    taskTitle: String,
    onTitleChange: (String) -> Unit,
    selectedDeadline: Long?,
    onSelectTimeClick: () -> Unit,
    onAddTaskClick: () -> Unit
) {
    val timeFormatter = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(CardBackground)
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.1f), shape = CircleShape)
            .padding(start = 16.dp, end = 6.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = taskTitle,
            onValueChange = onTitleChange,
            placeholder = { Text("Declare your next action...", color = TextMuted, fontSize = 14.sp) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = VibrantBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier
                .weight(1f)
                .testTag("task_input_field"),
            maxLines = 1
        )

        // Time Picker Trigger Button
        Button(
            onClick = onSelectTimeClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedDeadline != null) SoftCyan.copy(alpha = 0.15f) else SurfaceVariant,
                contentColor = if (selectedDeadline != null) SoftCyan else TextSecondary
            ),
            shape = CircleShape,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.testTag("time_picker_button")
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "Select alarm time",
                modifier = Modifier.size(16.dp)
            )
            if (selectedDeadline != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = timeFormatter.format(Date(selectedDeadline)),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Create Task Button
        IconButton(
            onClick = onAddTaskClick,
            enabled = taskTitle.isNotBlank(),
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (taskTitle.isNotBlank()) VibrantBlue else SurfaceVariant
                )
                .testTag("add_task_button")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Submit task",
                tint = if (taskTitle.isNotBlank()) Color.White else TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun InsightsTabScreen(viewModel: TaskViewModel) {
    val report by viewModel.sincerityReport.collectAsState()
    val isInsightsLoading by viewModel.isInsightsLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Weekly Integrity Report",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "Gemini powered sincerity metrics",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Button(
                onClick = { viewModel.runWeeklySincerityAnalysis() },
                colors = ButtonDefaults.buttonColors(containerColor = SoftCyan),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.testTag("refresh_insights_button"),
                enabled = !isInsightsLoading
            ) {
                Text("Analyze", color = CharcoalBlack, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isInsightsLoading) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = SoftCyan)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Deconstructing actions with Gemini...", color = TextSecondary, fontWeight = FontWeight.Medium)
                }
            }
        } else if (report != null) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    SincerityProgressChartCard(report = report!!)
                }

                item {
                    SubscoresCard(timeliness = report!!.timelinessScore, consistency = report!!.consistencyScore)
                }

                item {
                    FrictionPointsCard(points = report!!.frictionPoints)
                }

                item {
                    StrategiesCard(strategies = report!!.strategies)
                }

                item {
                    ActionableAdviceCard(advice = report!!.advice)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Analysis",
                        tint = TextMuted,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sincerity Report Not Compiled",
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tap the 'Analyze' button above to request Gemini to parse your last 7 days of performance details.",
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SincerityProgressChartCard(report: SincerityReport) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("sincerity_chart_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SINCERITY INDEX",
                fontSize = 12.sp,
                color = SoftCyan,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Colorful Progress Ring Chart
            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                val scoreFraction = report.sincerityScore / 10f
                Canvas(modifier = Modifier.size(140.dp)) {
                    // Draw background track arc
                    drawArc(
                        color = SurfaceVariant,
                        startAngle = -220f,
                        sweepAngle = 260f,
                        useCenter = false,
                        style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Draw active arc with blue to purple gradient
                    drawArc(
                        brush = Brush.linearGradient(
                            colors = listOf(VibrantBlue, NeonPurple)
                        ),
                        startAngle = -220f,
                        sweepAngle = 260f * scoreFraction,
                        useCenter = false,
                        style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Overall score centered inside
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${report.sincerityScore}",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "/10",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = when (report.sincerityScore) {
                    in 8..10 -> "Exceptional integrity and focus."
                    in 5..7 -> "Moderate consistency. Room to grow."
                    else -> "Warning: high rollover/missed rates."
                },
                fontSize = 14.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SubscoresCard(timeliness: Int, consistency: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("TIMELINESS", fontSize = 11.sp, color = VibrantBlue, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$timeliness/10", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                // Simple horizontal indicator
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(SurfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(timeliness / 10f)
                            .fillMaxHeight()
                            .background(VibrantBlue)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .background(SurfaceVariant)
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("CONSISTENCY", fontSize = 11.sp, color = NeonPurple, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$consistency/10", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                // Simple horizontal indicator
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(SurfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(consistency / 10f)
                            .fillMaxHeight()
                            .background(NeonPurple)
                    )
                }
            }
        }
    }
}

@Composable
fun FrictionPointsCard(points: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, AlertRed.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = AlertRed, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BEHAVIORAL FRICTION POINTS",
                    fontSize = 12.sp,
                    color = AlertRed,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            points.forEach { point ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "•", color = AlertRed, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                    Text(text = point, color = TextPrimary, fontSize = 14.sp, lineHeight = 18.sp)
                }
            }
        }
    }
}

@Composable
fun StrategiesCard(strategies: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SoftCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ACTIONABLE STRATEGIES",
                    fontSize = 12.sp,
                    color = SoftCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            strategies.forEachIndexed { index, strategy ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(SoftCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            color = SoftCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = strategy, color = TextPrimary, fontSize = 14.sp, lineHeight = 18.sp)
                }
            }
        }
    }
}

@Composable
fun ActionableAdviceCard(advice: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, NeonPurple.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "COACH'S DIRECTIVE",
                fontSize = 12.sp,
                color = NeonPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = advice,
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun saveSelectedImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.filesDir, "profile_picture.png")
        file.outputStream().use { outputStream ->
            inputStream.use { it.copyTo(outputStream) }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun loadLocalImages(context: Context): List<Uri> {
    val images = mutableListOf<Uri>()
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
    try {
        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                images.add(contentUri)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return images
}

fun cropAndSaveImage(
    context: Context,
    uri: Uri,
    scale: Float,
    offsetX: Float,
    offsetY: Float
): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return null
        
        // Circular center cropping square
        val cropSize = originalBitmap.width.coerceAtMost(originalBitmap.height)
        val output = Bitmap.createBitmap(cropSize, cropSize, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(output)
        val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
        
        // Incorporate offset & scale
        val maxOffset = (cropSize * (scale - 1) / 2).toInt()
        val dx = (offsetX / 2f).toInt().coerceIn(-maxOffset, maxOffset)
        val dy = (offsetY / 2f).toInt().coerceIn(-maxOffset, maxOffset)
        
        val centerLeft = (originalBitmap.width - cropSize) / 2
        val centerTop = (originalBitmap.height - cropSize) / 2
        
        // Adjust left and top by moving inverse direction of panning
        val adjustedLeft = (centerLeft - dx).coerceIn(0, originalBitmap.width - cropSize)
        val adjustedTop = (centerTop - dy).coerceIn(0, originalBitmap.height - cropSize)
        
        val finalSrcRect = Rect(
            adjustedLeft,
            adjustedTop,
            adjustedLeft + cropSize,
            adjustedTop + cropSize
        )
        
        val destRect = Rect(0, 0, cropSize, cropSize)
        
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(cropSize / 2f, cropSize / 2f, cropSize / 2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(originalBitmap, finalSrcRect, destRect, paint)
        
        val file = File(context.filesDir, "profile_picture.png")
        file.outputStream().use { out ->
            output.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun ProfileTabScreen(viewModel: TaskViewModel, hasPermission: Boolean) {
    val userName by viewModel.userName.collectAsState()
    val profilePictureUri by viewModel.profilePictureUri.collectAsState()
    var editingName by remember { mutableStateOf(userName) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showAvatarOptions by remember { mutableStateOf(false) }
    var showPresetSelector by remember { mutableStateOf(false) }

    // Custom Photo Browser & Cropper states
    var showSecureGallery by remember { mutableStateOf(false) }
    var showCropScreen by remember { mutableStateOf(false) }
    var selectedGalleryImageUri by remember { mutableStateOf<Uri?>(null) }
    var galleryImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // Past Monthly insights and comparison state
    val monthlyInsights by viewModel.monthlyInsights.collectAsState()
    val comparisonResult by viewModel.comparisonResult.collectAsState()
    val isComparisonLoading by viewModel.isComparisonLoading.collectAsState()
    var selectedMonthsForComparison by remember { mutableStateOf(setOf<com.example.api.MonthlyAnalysis>()) }

    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galleryImages = loadLocalImages(context)
            showSecureGallery = true
        } else {
            Toast.makeText(context, "Storage permission is required to browse your device photos.", Toast.LENGTH_LONG).show()
        }
    }

    if (showAvatarOptions) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showAvatarOptions = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Customize Avatar",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Choose a high-fidelity preset or upload your own image.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.03f))
                            .clickable {
                                showAvatarOptions = false
                                val isPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    androidx.core.content.ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.READ_MEDIA_IMAGES
                                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                } else {
                                    androidx.core.content.ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                }
                                if (isPermissionGranted) {
                                    galleryImages = loadLocalImages(context)
                                    showSecureGallery = true
                                } else {
                                    permissionLauncher.launch(storagePermission)
                                }
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = SoftCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Upload custom photo", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Choose any photo from your gallery", color = TextSecondary, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.03f))
                            .clickable {
                                showAvatarOptions = false
                                showPresetSelector = true
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NeonPurple,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Choose premium preset", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Select from beautiful designer styles", color = TextSecondary, fontSize = 12.sp)
                        }
                    }

                    if (profilePictureUri != null) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.03f))
                                .clickable {
                                    showAvatarOptions = false
                                    viewModel.updateProfilePictureUri(null)
                                    Toast.makeText(context, "Avatar reset to default.", Toast.LENGTH_SHORT).show()
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = AlertRed,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Remove current picture", color = AlertRed, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("Restore default dynamic initials", color = TextSecondary, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "CANCEL",
                        color = TextMuted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        modifier = Modifier
                            .clickable { showAvatarOptions = false }
                            .padding(8.dp)
                    )
                }
            }
        }
    }

    if (showPresetSelector) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showPresetSelector = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Choose Premium Avatar",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Select one of our high-fidelity, playful cartoon animal avatars.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )

                    val presets = listOf(
                        "preset:fox" to "Foxy Fox",
                        "preset:panda" to "Panda Roll",
                        "preset:cat" to "Neko Cat",
                        "preset:koala" to "Chill Koala",
                        "preset:tiger" to "Apex Tiger",
                        "preset:lion" to "King Leo",
                        "preset:frog" to "Zen Froggy",
                        "preset:unicorn" to "Cosmic Uni"
                    )

                    for (row in 0 until 4) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            for (col in 0 until 2) {
                                val idx = row * 2 + col
                                if (idx < presets.size) {
                                    val (presetUri, presetName) = presets[idx]
                                    val isSelected = profilePictureUri == presetUri

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 6.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) VibrantBlue.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.02f))
                                            .border(
                                                width = 1.dp,
                                                color = if (isSelected) VibrantBlue else Color.White.copy(alpha = 0.06f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                viewModel.updateProfilePictureUri(presetUri)
                                                showPresetSelector = false
                                                Toast.makeText(context, "Preset applied: $presetName", Toast.LENGTH_SHORT).show()
                                            }
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            UserProfileAvatar(
                                                userName = userName,
                                                profilePictureUri = presetUri,
                                                size = 48.dp,
                                                modifier = Modifier.border(1.5.dp, CharcoalBlack, CircleShape)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = presetName,
                                                color = if (isSelected) VibrantBlue else TextPrimary,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 11.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "BACK",
                        color = TextMuted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        modifier = Modifier
                            .clickable {
                                showPresetSelector = false
                                showAvatarOptions = true
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }

    // --- SECURE GALLERY VAULT DIALOG ---
    if (showSecureGallery) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showSecureGallery = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Secure",
                                tint = SoftCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Private Media Vault",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }
                        IconButton(onClick = { showSecureGallery = false }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }
                    
                    // Security Banner
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AlertBackground),
                        border = BorderStroke(1.dp, SoftCyan.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Shield",
                                tint = SoftCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "100% Secure & On-Device:\nAll photos are listed directly from your Android MediaStore ContentResolver. No images leave this device. Google or external servers have zero access to your photos.",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                    
                    if (galleryImages.isEmpty()) {
                        Box(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No photos found on your device.", color = TextMuted, fontSize = 14.sp)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 12.dp)
                        ) {
                            items(galleryImages) { uri ->
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.02f))
                                        .clickable {
                                            selectedGalleryImageUri = uri
                                            showSecureGallery = false
                                            showCropScreen = true
                                        }
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(uri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Local Image",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- SECURE CROPPER DIALOG ---
    if (showCropScreen && selectedGalleryImageUri != null) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showCropScreen = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Crop Profile Picture",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    
                    Text(
                        text = "Drag to pan. Slide to zoom. Circle shows cropped avatar area.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )
                    
                    var zoomScale by remember { mutableStateOf(1.0f) }
                    var panX by remember { mutableStateOf(0f) }
                    var panY by remember { mutableStateOf(0f) }
                    
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CharcoalBlack)
                            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        panX += dragAmount.x
                                        panY += dragAmount.y
                                    }
                                }
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(selectedGalleryImageUri)
                                    .build(),
                                contentDescription = "Adjust Photo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer(
                                        scaleX = zoomScale,
                                        scaleY = zoomScale,
                                        translationX = panX,
                                        translationY = panY
                                    ),
                                contentScale = ContentScale.Fit
                            )
                        }
                        
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokePx = 3.dp.toPx()
                            drawCircle(
                                color = VibrantBlue,
                                radius = (size.minDimension / 2.3f) - strokePx,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                    width = strokePx,
                                    pathEffect = null
                                )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ZoomOut, contentDescription = "Zoom Out", tint = TextMuted)
                        Slider(
                            value = zoomScale,
                            onValueChange = { zoomScale = it },
                            valueRange = 1.0f..3.0f,
                            colors = SliderDefaults.colors(
                                thumbColor = VibrantBlue,
                                activeTrackColor = VibrantBlue,
                                inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Zoom In", tint = VibrantBlue)
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showCropScreen = false },
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = TextPrimary)
                        }
                        
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val filePath = cropAndSaveImage(
                                        context = context,
                                        uri = selectedGalleryImageUri!!,
                                        scale = zoomScale,
                                        offsetX = panX,
                                        offsetY = panY
                                    )
                                    if (filePath != null) {
                                        viewModel.updateProfilePictureUri(filePath)
                                        showCropScreen = false
                                        Toast.makeText(context, "Avatar cropped and updated!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Error saving cropped image.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VibrantBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Crop & Save", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Profile Settings",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )
        Text(
            text = "Configure your identity and view security guidelines",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Avatar with Edit Badge
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    UserProfileAvatar(
                        userName = userName,
                        profilePictureUri = profilePictureUri,
                        size = 100.dp,
                        modifier = Modifier
                            .border(3.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                            .shadow(8.dp, CircleShape),
                        onClick = { showAvatarOptions = true }
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(VibrantBlue)
                            .border(2.dp, CharcoalBlack, CircleShape)
                            .clickable { showAvatarOptions = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Change Picture",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name Input field
                OutlinedTextField(
                    value = editingName,
                    onValueChange = { editingName = it },
                    label = { Text("Your Name", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults_colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = VibrantBlue,
                        unfocusedBorderColor = SurfaceVariant,
                        cursorColor = VibrantBlue,
                        focusedLabelColor = VibrantBlue,
                        unfocusedLabelColor = TextSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("name_edit_field"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (editingName.isNotBlank()) {
                            viewModel.updateUserName(editingName.trim())
                            Toast.makeText(context, "Name updated successfully!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_name_button")
                ) {
                    Text("Save Identity", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- MONTHLY INSIGHTS & COMPARISON SECTION ---
        Text(
            text = "Monthly Insights & Comparisons",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )
        Text(
            text = "Select 2 or more months to rank and run deep Gemini comparison.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Compare Button
        Button(
            onClick = {
                viewModel.runMonthlyComparison(selectedMonthsForComparison.toList())
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonPurple,
                disabledContainerColor = Color.White.copy(alpha = 0.04f)
            ),
            enabled = selectedMonthsForComparison.size >= 2 && !isComparisonLoading,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("run_comparison_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (selectedMonthsForComparison.size < 2) "Select 2+ Months to Compare" else "Compare Selected (${selectedMonthsForComparison.size}) Months",
                color = if (selectedMonthsForComparison.size >= 2) Color.White else TextMuted,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI Comparison Result display card
        if (isComparisonLoading || comparisonResult != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .testTag("comparison_result_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CoachBackground),
                border = BorderStroke(1.dp, NeonPurple.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI",
                                tint = SoftCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cross-Month Behavioral Comparison",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = SoftCyan
                            )
                        }
                        IconButton(
                            onClick = { viewModel.clearComparison() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isComparisonLoading) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = NeonPurple, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Calculating sincerity indicators across months...",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else if (comparisonResult != null) {
                        MarkdownText(text = comparisonResult!!)
                    }
                }
            }
        }

        // Chronological month cards (June, July, August, September in order)
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            monthlyInsights.forEach { month ->
                val isSelected = selectedMonthsForComparison.contains(month)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedMonthsForComparison = if (isSelected) {
                                selectedMonthsForComparison - month
                            } else {
                                selectedMonthsForComparison + month
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) VibrantBlue.copy(alpha = 0.08f) else CardBackground
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) VibrantBlue else Color.White.copy(alpha = 0.05f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Month & checkbox Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { checked ->
                                        selectedMonthsForComparison = if (checked == true) {
                                            selectedMonthsForComparison + month
                                        } else {
                                            selectedMonthsForComparison - month
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = VibrantBlue,
                                        uncheckedColor = Color.White.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = month.monthName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                            }
                            
                            // Compact Sincerity score
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(VibrantBlue.copy(alpha = 0.1f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Sincerity: ${month.sincerityScore}/10",
                                    color = VibrantBlue,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Brief analysis description
                        Text(
                            text = month.shortSummary,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Strengths & Growth Areas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Plus Point
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.03f)),
                                border = BorderStroke(1.dp, Color.Green.copy(alpha = 0.1f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("🟢 STRENGTHS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Green)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(month.positivePoint, fontSize = 11.sp, color = TextSecondary, lineHeight = 14.sp)
                                }
                            }

                            // Minus Point
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = AlertRed.copy(alpha = 0.03f)),
                                border = BorderStroke(1.dp, AlertRed.copy(alpha = 0.1f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("🔴 GROWTH AREAS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AlertRed)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(month.negativePoint, fontSize = 11.sp, color = TextSecondary, lineHeight = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // System Settings Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Text(
                    text = "SYSTEM ENGINE DIAGNOSTICS",
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (hasPermission) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                            contentDescription = null,
                            tint = if (hasPermission) SoftCyan else AlertRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("System Notifications", color = TextPrimary, fontSize = 14.sp)
                    }
                    Text(
                        text = if (hasPermission) "ACTIVE" else "DISABLED",
                        color = if (hasPermission) SoftCyan else AlertRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = SoftCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Database Encryption", color = TextPrimary, fontSize = 14.sp)
                    }
                    Text(
                        text = "ROOM LOCAL",
                        color = SoftCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Security Warn Alert
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AlertBackground),
            border = BorderStroke(1.dp, AlertRed.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = AlertRed, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("API SECURITY POLICY", fontSize = 11.sp, color = AlertRed, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Security Warning: I have included your API keys in the generated APK file for this prototype. Please be aware that Android APKs can be easily decompiled, and these keys can be extracted by anyone who has access to the file. Do not share this APK file publicly or with unauthorized individuals to prevent potential misuse.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

// Helpers for OutlinedTextField colors in M3 to keep code extremely clean and simple
@Composable
fun OutlinedTextFieldDefaults_colors(
    focusedTextColor: Color,
    unfocusedTextColor: Color,
    focusedBorderColor: Color,
    unfocusedBorderColor: Color,
    cursorColor: Color,
    focusedLabelColor: Color,
    unfocusedLabelColor: Color
) = OutlinedTextFieldDefaults_colors_impl(
    focusedTextColor = focusedTextColor,
    unfocusedTextColor = unfocusedTextColor,
    focusedBorderColor = focusedBorderColor,
    unfocusedBorderColor = unfocusedBorderColor,
    cursorColor = cursorColor,
    focusedLabelColor = focusedLabelColor,
    unfocusedLabelColor = unfocusedLabelColor
)

@Composable
fun OutlinedTextFieldDefaults_colors_impl(
    focusedTextColor: Color,
    unfocusedTextColor: Color,
    focusedBorderColor: Color,
    unfocusedBorderColor: Color,
    cursorColor: Color,
    focusedLabelColor: Color,
    unfocusedLabelColor: Color
): androidx.compose.material3.TextFieldColors {
    return TextFieldDefaults.colors(
        focusedTextColor = focusedTextColor,
        unfocusedTextColor = unfocusedTextColor,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        cursorColor = cursorColor,
        focusedIndicatorColor = focusedBorderColor,
        unfocusedIndicatorColor = unfocusedBorderColor,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor
    )
}

@Composable
fun MarkdownText(text: String) {
    val lines = text.split("\n")
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        lines.forEach { line ->
            val trimmed = line.trim()
            if (trimmed.startsWith("###")) {
                Text(
                    text = trimmed.substring(3).replace("**", "").trim(),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = SoftCyan,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
            } else if (trimmed.startsWith("##")) {
                Text(
                    text = trimmed.substring(2).replace("**", "").trim(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NeonPurple,
                    modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
                )
            } else if (trimmed.startsWith("*") || trimmed.startsWith("-")) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 2.dp, bottom = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("• ", color = SoftCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        text = trimmed.substring(1).trim(),
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            } else if (trimmed.isNotBlank()) {
                // Parse standard inline bold text **xyz**
                val boldRegex = "\\*\\*(.*?)\\*\\*".toRegex()
                val parsedText = trimmed.replace(boldRegex) { matchResult ->
                    matchResult.groupValues[1]
                }
                Text(
                    text = parsedText,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOutCubic),
        label = "alpha"
    )
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1200, easing = EaseOutBack),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2200) // Display splash for 2.2 seconds
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CharcoalBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .graphicsLayer(
                    alpha = alphaAnim.value,
                    scaleX = scaleAnim.value,
                    scaleY = scaleAnim.value
                )
        ) {
            // Icon Container (Draw the beautiful 3D icon in Compose!)
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(Color(0xFF06080C))
                    .border(1.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(36.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // 1. Arc Ring
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            brush = Brush.linearGradient(
                                colors = listOf(SoftCyan, VibrantBlue, NeonPurple)
                            ),
                            startAngle = -210f,
                            sweepAngle = 240f,
                            useCenter = false,
                            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // 2. White Checkmark in Center
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(54.dp)
                            .align(Alignment.Center)
                    )

                    // 3. Mini Document Checklist Card on the left
                    Box(
                        modifier = Modifier
                            .size(width = 36.dp, height = 48.dp)
                            .align(Alignment.CenterStart)
                            .offset(x = (-4).dp, y = (-10).dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF10141F))
                            .border(1.5.dp, Color(0xFF2C374E), RoundedCornerShape(6.dp))
                            .padding(4.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Checked item
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(RoundedCornerShape(1.5.dp))
                                        .background(VibrantBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(5.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(3.dp))
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height(2.dp)
                                        .background(TextSecondary)
                                )
                            }
                            // Empty items
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .border(0.8.dp, TextMuted, RoundedCornerShape(1.5.dp))
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height(2.dp)
                                        .background(TextMuted)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .border(0.8.dp, TextMuted, RoundedCornerShape(1.5.dp))
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height(2.dp)
                                        .background(TextMuted)
                                )
                            }
                        }
                    }

                    // 4. Glowing bell on the bottom right
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = NeonPurple,
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 2.dp, y = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Name
            Text(
                text = "SINCERITY",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = Color.White,
                letterSpacing = 3.sp
            )
        }
    }
}
