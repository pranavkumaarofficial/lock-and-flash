package com.screentracker.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Font placeholders — swap for actual Fraunces / DM Sans when font files are added to res/font/
private val DisplayFont = FontFamily.Serif     // Fraunces
private val UiFont = FontFamily.SansSerif      // DM Sans

// Color tokens — landing page design (source of truth)
private val Primary = Color(0xFF6750A4)
private val OnPrimary = Color(0xFFFFFFFF)
private val Container = Color(0xFFEADDFF)
private val OnContainer = Color(0xFF21005D)
private val BackgroundLight = Color(0xFFFDFBFF)
private val BackgroundDark = Color(0xFF1C1B1F)
private val SurfaceLight = Color(0xFFF7F2FA)
private val SurfaceDark = Color(0xFF2B2930)
private val OnSurfaceLight = Color(0xFF1D1B20)
private val OnSurfaceDark = Color(0xFFE6E1E5)
private val SecondaryTextLight = Color(0xFF49454F)
private val SecondaryTextDark = Color(0xFFCAC4D0)
private val OutlineVariantLight = Color(0xFFE7E0EC)
private val OutlineVariantDark = Color(0xFF49454F)

// Theme state — readable by any child composable
val LocalIsDark = staticCompositionLocalOf { false }

// ── Activity ─────────────────────────────────────────────────────────────

class MainActivity : ComponentActivity() {
    private lateinit var database: EventDatabase

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) startMonitoringService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = EventDatabase.getInstance(this)
        setContent {
            val systemDark = isSystemInDarkTheme()
            var isDark by remember { mutableStateOf(systemDark) }

            RestTheme(isDark = isDark) {
                RestApp(
                    onStartService = { checkPermissionsAndStart() },
                    onStopService = { stopMonitoringService() },
                    database = database,
                    onToggleTheme = { isDark = !isDark }
                )
            }
        }
    }

    private fun checkPermissionsAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                startMonitoringService()
            } else {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            startMonitoringService()
        }
    }

    private fun startMonitoringService() {
        val intent = Intent(this, MonitoringService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent)
        else startService(intent)
    }

    private fun stopMonitoringService() {
        stopService(Intent(this, MonitoringService::class.java))
    }
}

// ── Theme ────────────────────────────────────────────────────────────────

@Composable
fun RestTheme(isDark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val dark = isDark
    val colors = if (dark) {
        darkColorScheme(
            primary = Primary,
            onPrimary = OnPrimary,
            primaryContainer = Container,
            onPrimaryContainer = OnContainer,
            background = BackgroundDark,
            onBackground = OnSurfaceDark,
            surface = SurfaceDark,
            onSurface = OnSurfaceDark,
            onSurfaceVariant = SecondaryTextDark,
            outline = OutlineVariantDark,
            outlineVariant = OutlineVariantDark
        )
    } else {
        lightColorScheme(
            primary = Primary,
            onPrimary = OnPrimary,
            primaryContainer = Container,
            onPrimaryContainer = OnContainer,
            background = BackgroundLight,
            onBackground = OnSurfaceLight,
            surface = SurfaceLight,
            onSurface = OnSurfaceLight,
            onSurfaceVariant = SecondaryTextLight,
            outline = OutlineVariantLight,
            outlineVariant = OutlineVariantLight
        )
    }
    CompositionLocalProvider(LocalIsDark provides dark) {
        MaterialTheme(colorScheme = colors, content = content)
    }
}

// ── Navigation ───────────────────────────────────────────────────────────

private enum class Screen { Home, Logs }

@Composable
fun RestApp(
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    database: EventDatabase,
    onToggleTheme: () -> Unit = {}
) {
    var screen by remember { mutableStateOf(Screen.Home) }
    var isRunning by remember { mutableStateOf(false) }
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        database.eventDao().getAllEvents().collect { events = it }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative glow — from landing page design (primary/5 light, primary/10 dark)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .offset(y = (-60).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Primary.copy(alpha = if (LocalIsDark.current) 0.10f else 0.05f),
                            Color.Transparent
                        ),
                        radius = 900f
                    )
                )
        )

        when (screen) {
            Screen.Home -> HomeScreen(
                isRunning = isRunning,
                onToggle = {
                    if (isRunning) { onStopService(); isRunning = false }
                    else { onStartService(); isRunning = true }
                },
                unlocks = events.count { it.type == EventType.USER_PRESENT },
                flashlight = events.count { it.type == EventType.FLASHLIGHT_ON }
            )
            Screen.Logs -> LogsScreen(
                events = events,
                onClearData = { scope.launch { database.eventDao().deleteAllEvents() } },
                onToggleTheme = onToggleTheme
            )
        }

        BottomNav(
            current = screen,
            onSelect = { screen = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ── Home Screen ──────────────────────────────────────────────────────────

@Composable
private fun HomeScreen(
    isRunning: Boolean,
    onToggle: () -> Unit,
    unlocks: Int,
    flashlight: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .padding(top = 56.dp, bottom = 96.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // Wordmark — 72sp Fraunces italic, light weight
        Text(
            text = "rest",
            fontSize = 72.sp,
            fontFamily = DisplayFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Light,
            color = if (LocalIsDark.current) Container else Primary,
            letterSpacing = (-1.8).sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Play / Stop — 128dp circular, icon only
        PlayButton(isPlaying = isRunning, onClick = onToggle)

        Spacer(modifier = Modifier.weight(1f))

        // Stats — flat, quiet, no cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatColumn(Icons.Default.LockOpen, unlocks, "Unlocks")
            StatColumn(Icons.Default.FlashlightOn, flashlight, "Flashlight")
        }
    }
}

@Composable
private fun PlayButton(isPlaying: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    // Press: scale(0.92) with cubic-bezier(0.2, 0, 0, 1) over 400ms — from design
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        animationSpec = tween(400, easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)),
        label = "press"
    )

    Box(
        modifier = Modifier
            .size(128.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(Primary)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
            contentDescription = null,
            tint = OnPrimary,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun StatColumn(icon: ImageVector, count: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = count.toString(),
            fontSize = 36.sp,
            fontFamily = UiFont,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label.uppercase(),
            fontSize = 12.sp,
            fontFamily = UiFont,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            letterSpacing = 1.5.sp
        )
    }
}

// ── Logs Screen ──────────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LogsScreen(events: List<Event>, onClearData: () -> Unit, onToggleTheme: () -> Unit = {}) {
    var tab by remember { mutableStateOf(0) }
    var menuExpanded by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }

    val tabs = listOf("Today", "Yesterday", "Last 7 Days", "Custom")

    val todayStart = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val yesterdayStart = todayStart - 86_400_000L
    val weekStart = todayStart - 7 * 86_400_000L

    val filtered = when (tab) {
        0 -> events.filter { it.timestamp >= todayStart }
        1 -> events.filter { it.timestamp in yesterdayStart until todayStart }
        2 -> events.filter { it.timestamp >= weekStart }
        else -> events
    }.sortedByDescending { it.timestamp }

    val grouped = filtered.groupBy { ev ->
        Calendar.getInstance().apply {
            timeInMillis = ev.timestamp
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }.toSortedMap(compareByDescending { it })

    val dateFmt = SimpleDateFormat("MMM d", Locale.getDefault())
    val timeFmt = SimpleDateFormat("hh:mm a", Locale.getDefault())

    // Clear data confirmation
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            confirmButton = {
                TextButton(onClick = { onClearData(); showClearDialog = false }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
            },
            title = { Text("Clear all data?") },
            text = { Text("All recorded events will be permanently deleted.") }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Header — "Logs" + filter / more_horiz
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 56.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Logs",
                fontSize = 48.sp,
                fontFamily = DisplayFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 43.sp
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onToggleTheme, modifier = Modifier.size(40.dp)) {
                    Icon(
                        if (LocalIsDark.current) Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
                        null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = {}, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.FilterList, null, tint = MaterialTheme.colorScheme.onSurface)
                }
                Box {
                    IconButton(onClick = { menuExpanded = true }, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.MoreHoriz, null, tint = MaterialTheme.colorScheme.onSurface)
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Clear all data") },
                            onClick = { menuExpanded = false; showClearDialog = true }
                        )
                    }
                }
            }
        }

        // Tab bar
        ScrollableTabRow(
            selectedTabIndex = tab,
            containerColor = Color.Transparent,
            edgePadding = 24.dp,
            divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) },
            indicator = { positions ->
                if (tab < positions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(positions[tab]),
                        color = MaterialTheme.colorScheme.primary,
                        height = 2.dp
                    )
                }
            }
        ) {
            tabs.forEachIndexed { i, title ->
                Tab(
                    selected = tab == i,
                    onClick = { tab = i },
                    text = {
                        Text(
                            title,
                            fontSize = 14.sp,
                            fontFamily = UiFont,
                            fontWeight = FontWeight.Medium,
                            color = if (tab == i) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        // Event list
        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No events",
                    fontSize = 14.sp,
                    fontFamily = UiFont,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                grouped.forEach { (day, dayEvents) ->
                    stickyHeader(key = day) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        ) {
                            val prefix = when {
                                day >= todayStart -> "Today"
                                day >= yesterdayStart -> "Yesterday"
                                else -> null
                            }
                            val date = dateFmt.format(Date(day))
                            Text(
                                text = (if (prefix != null) "$prefix, $date" else date).uppercase(),
                                fontSize = 12.sp,
                                fontFamily = UiFont,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    items(dayEvents, key = { it.id }) { event ->
                        EventRow(event, timeFmt)
                    }
                }
            }
        }
    }
}

@Composable
private fun EventRow(event: Event, timeFmt: SimpleDateFormat) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Icon — bare, no container, 80% opacity
            Icon(
                imageVector = when (event.type) {
                    EventType.SCREEN_ON -> Icons.Default.PhoneAndroid
                    EventType.USER_PRESENT -> Icons.Default.LockOpen
                    EventType.FLASHLIGHT_ON -> Icons.Default.FlashlightOn
                    EventType.FLASHLIGHT_OFF -> Icons.Default.FlashlightOff
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Title — 16sp, normal weight, tracking-wide
                    Text(
                        text = when (event.type) {
                            EventType.SCREEN_ON -> "Screen On"
                            EventType.USER_PRESENT -> "Screen Unlocked"
                            EventType.FLASHLIGHT_ON -> "Flashlight On"
                            EventType.FLASHLIGHT_OFF -> "Flashlight Off"
                        },
                        fontSize = 16.sp,
                        fontFamily = UiFont,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.3.sp,
                        modifier = Modifier.weight(1f)
                    )

                    // Timestamp — 12sp, tabular nums
                    Text(
                        text = timeFmt.format(Date(event.timestamp)),
                        fontSize = 12.sp,
                        fontFamily = UiFont,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Divider
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

// ── Bottom Nav ───────────────────────────────────────────────────────────

@Composable
private fun BottomNav(current: Screen, onSelect: (Screen) -> Unit, modifier: Modifier) {
    val dark = LocalIsDark.current
    val activeColor = if (dark) Container else Primary
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.90f))
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home — filled circle when active, outlined when inactive
            IconButton(
                onClick = { onSelect(Screen.Home) },
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                NavCircleIcon(
                    filled = current == Screen.Home,
                    color = if (current == Screen.Home) activeColor else inactiveColor,
                    size = 28.dp
                )
            }

            // Logs — history icon
            IconButton(
                onClick = { onSelect(Screen.Logs) },
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = if (current == Screen.Logs) activeColor else inactiveColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
private fun NavCircleIcon(filled: Boolean, color: Color, size: Dp) {
    if (filled) {
        Box(
            modifier = Modifier
                .size(size)
                .background(color, CircleShape)
        )
    } else {
        Box(
            modifier = Modifier
                .size(size)
                .border(2.dp, color, CircleShape)
        )
    }
}
