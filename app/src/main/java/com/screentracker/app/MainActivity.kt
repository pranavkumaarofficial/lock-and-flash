package com.screentracker.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var database: EventDatabase
    
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startMonitoringService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        database = EventDatabase.getInstance(this)
        
        setContent {
            ScreenTrackerTheme {
                MainScreen(
                    onStartService = { checkPermissionsAndStart() },
                    onStopService = { stopMonitoringService() },
                    database = database
                )
            }
        }
    }

    private fun checkPermissionsAndStart() {
        // Check notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startMonitoringService()
                }
                else -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            startMonitoringService()
        }
    }

    private fun startMonitoringService() {
        val intent = Intent(this, MonitoringService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopMonitoringService() {
        val intent = Intent(this, MonitoringService::class.java)
        stopService(intent)
    }
}

@Composable
fun ScreenTrackerTheme(content: @Composable () -> Unit) {
    // M3 Expressive Color Scheme (2026 Pixel Style)
    MaterialTheme(
        colorScheme = darkColorScheme(
            // Primary Colors (Purple-forward)
            primary = Color(0xFFD0BCFF),
            onPrimary = Color(0xFF381E72),
            primaryContainer = Color(0xFF4F378B),
            onPrimaryContainer = Color(0xFFEADDFF),

            // Secondary Colors
            secondary = Color(0xFFCCC2DC),
            onSecondary = Color(0xFF332D41),
            secondaryContainer = Color(0xFF4A4458),
            onSecondaryContainer = Color(0xFFE8DEF8),

            // Tertiary Colors
            tertiary = Color(0xFFEFB8C8),
            onTertiary = Color(0xFF492532),
            tertiaryContainer = Color(0xFF633B48),
            onTertiaryContainer = Color(0xFFFFD8E4),

            // Error Colors
            error = Color(0xFFF2B8B5),
            onError = Color(0xFF601410),
            errorContainer = Color(0xFF8C1D18),
            onErrorContainer = Color(0xFFF9DEDC),

            // Background & Surface (Tonal Elevation)
            background = Color(0xFF1C1B1F),
            onBackground = Color(0xFFE6E1E5),
            surface = Color(0xFF1C1B1F),
            onSurface = Color(0xFFE6E1E5),
            surfaceVariant = Color(0xFF49454F),
            onSurfaceVariant = Color(0xFFCAC4D0),

            // Surface Containers (Tonal Hierarchy)
            surfaceContainerLowest = Color(0xFF0F0D13),
            surfaceContainerLow = Color(0xFF1D1A22),
            surfaceContainer = Color(0xFF211F26),
            surfaceContainerHigh = Color(0xFF2B2930),
            surfaceContainerHighest = Color(0xFF36343B),

            // Outline
            outline = Color(0xFF938F99),
            outlineVariant = Color(0xFF49454F)
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    database: EventDatabase
) {
    var isServiceRunning by remember { mutableStateOf(false) }
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var selectedTab by remember { mutableStateOf(0) }
    var showClearDataDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    
    // Load events
    LaunchedEffect(Unit) {
        scope.launch {
            database.eventDao().getAllEvents().collect { eventList ->
                events = eventList
            }
        }
    }

    // Clear Data Confirmation Dialog
    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = {
                Text(
                    "Clear All Data?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "This will permanently delete all ${events.size} recorded events. This action cannot be undone.",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            database.eventDao().deleteAllEvents()
                        }
                        showClearDataDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Delete All", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearDataDialog = false },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Cancel", fontWeight = FontWeight.Medium)
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ScreenTracker",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Clear data button in app bar
                    IconButton(onClick = { showClearDataDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear all data",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Control Card (M3 Expressive: 28dp radius, tonal surface)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(28.dp), // Large radius
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(0.dp) // Tonal, not shadow
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ServiceStatusIndicator(isServiceRunning)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // M3 Expressive: Bold, larger status text
                    Text(
                        text = if (isServiceRunning) "Monitoring Active" else "Monitoring Stopped",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // M3 Expressive: Larger, bolder button (60dp height)
                    Button(
                        onClick = {
                            if (isServiceRunning) {
                                onStopService()
                                isServiceRunning = false
                            } else {
                                onStartService()
                                isServiceRunning = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(20.dp), // Larger radius
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isServiceRunning)
                                MaterialTheme.colorScheme.errorContainer
                            else MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = if (isServiceRunning) Icons.Default.Close else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp) // Larger icon
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (isServiceRunning) "Stop Monitoring" else "Start Monitoring",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Stats Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatsCard(
                    title = "Screen Unlocks",
                    count = events.count { it.type == EventType.SCREEN_ON },
                    icon = Icons.Default.Phone,
                    color = MaterialTheme.colorScheme.primary, // Use theme color
                    modifier = Modifier.weight(1f)
                )

                StatsCard(
                    title = "Flashlight",
                    count = events.count { it.type == EventType.FLASHLIGHT_ON },
                    icon = Icons.Default.Lightbulb,
                    color = MaterialTheme.colorScheme.tertiary, // Use theme color
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Events List (M3 Expressive: 28dp radius, tonal surface)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("All Events") }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Screen") }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("Flashlight") }
                        )
                    }
                    
                    val filteredEvents = when (selectedTab) {
                        1 -> events.filter { it.type == EventType.SCREEN_ON || it.type == EventType.USER_PRESENT }
                        2 -> events.filter { it.type == EventType.FLASHLIGHT_ON }
                        else -> events
                    }
                    
                    if (filteredEvents.isEmpty()) {
                        // M3 Expressive: Enhanced empty state with icon and larger text
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Category,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                )
                                Text(
                                    "No events recorded yet",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "Start monitoring to see events",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp) // More spacing
                        ) {
                            items(
                                items = filteredEvents.reversed(),
                                key = { it.id }
                            ) { event ->
                                EventItem(event)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceStatusIndicator(isRunning: Boolean) {
    // M3 Expressive: Spring Animation (not linear)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = EaseInOutCubic
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Larger, more prominent indicator (96dp)
    Box(
        modifier = Modifier
            .size(96.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = if (isRunning)
                        listOf(Color(0xFFD0BCFF), Color(0xFF6750A4)) // Purple gradient
                    else
                        listOf(Color(0xFF938F99), Color(0xFF49454F))
                ),
                shape = RoundedCornerShape(999.dp) // Fully rounded
            )
            .then(if (isRunning) Modifier.scale(scale) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isRunning) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = null,
            tint = if (isRunning) Color(0xFF381E72) else Color.White,
            modifier = Modifier.size(48.dp) // Larger icon
        )
    }
}

@Composable
fun StatsCard(
    title: String,
    count: Int,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    // M3 Expressive: 24dp radius, tonal surface
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp), // Larger radius
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp), // More padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon container with color background
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = color.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Larger, bolder count
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EventItem(event: Event) {
    val dateFormat = SimpleDateFormat("MMM dd, hh:mm:ss a", Locale.getDefault())

    // M3 Expressive: 20dp radius, tonal surface, larger icons
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp), // Larger radius
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp), // More padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container with theme colors
            Box(
                modifier = Modifier
                    .size(56.dp) // Larger container
                    .background(
                        color = when (event.type) {
                            EventType.SCREEN_ON -> MaterialTheme.colorScheme.primaryContainer
                            EventType.USER_PRESENT -> MaterialTheme.colorScheme.secondaryContainer
                            EventType.FLASHLIGHT_ON -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (event.type) {
                        EventType.SCREEN_ON -> Icons.Default.PhoneAndroid
                        EventType.USER_PRESENT -> Icons.Default.LockOpen
                        EventType.FLASHLIGHT_ON -> Icons.Default.Lightbulb
                        else -> Icons.Default.Circle
                    },
                    contentDescription = null,
                    tint = when (event.type) {
                        EventType.SCREEN_ON -> MaterialTheme.colorScheme.onPrimaryContainer
                        EventType.USER_PRESENT -> MaterialTheme.colorScheme.onSecondaryContainer
                        EventType.FLASHLIGHT_ON -> MaterialTheme.colorScheme.onTertiaryContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(28.dp) // Larger icon
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = when (event.type) {
                        EventType.SCREEN_ON -> "Screen Turned On"
                        EventType.USER_PRESENT -> "Device Unlocked"
                        EventType.FLASHLIGHT_ON -> "Flashlight Activated"
                        EventType.FLASHLIGHT_OFF -> "Flashlight Deactivated"
                        else -> "Unknown Event"
                    },
                    style = MaterialTheme.typography.titleMedium, // Larger text
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateFormat.format(Date(event.timestamp)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
