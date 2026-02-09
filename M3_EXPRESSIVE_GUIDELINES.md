# Material 3 Expressive Design Guidelines (2026)

## Core Philosophy
Material 3 Expressive makes interfaces more **colorful, emotive, and personal** while improving usability. Research shows users spot key elements **4x faster** with M3 Expressive.

## Key Design Principles

### 1. CONTAINMENT (Most Important for Android Apps)
- Use **rounded containers** (16-24dp radius) to group related elements
- Each card/section gets its own container with subtle elevation
- Containers use surface color variations (not just borders)
- Example: Each event in your list should be in a rounded container

### 2. STRONGER COLORS
- Use **bolder accent colors** for primary actions
- Primary button: Vibrant color (not muted)
- Background hierarchy: Use tonal surface colors
- Color roles: Primary, Secondary, Tertiary + Surface variants

### 3. EXPRESSIVE SHAPES
- **Fully rounded** corners for pills/FABs (cornerRadius = full)
- **Large corner radius** for cards (20-28dp)
- **Asymmetric shapes** for visual interest (optional)
- Shape tokens: small (8dp), medium (16dp), large (28dp), full (999dp)

### 4. SPRING ANIMATIONS
- Replace linear transitions with **spring physics**
- Animations feel bouncy and alive
- Use `spring()` damping in Compose
- Haptic feedback on key interactions

### 5. VISUAL HIERARCHY
- Make important actions **larger and bolder**
- Use size variation (not just color) for hierarchy
- Key action buttons: Minimum 56dp height
- Icon size variation: 24dp (normal) vs 32dp (emphasis)

### 6. DEPTH & ELEVATION
- Subtle blur effects on overlays
- Tonal elevation (color-based, not shadow-based)
- Surface containers at different tonal levels
- Use `surfaceVariant`, `surfaceContainerHigh`, etc.

## Component Guidelines

### Buttons
- **Primary (Filled):** Bold color, high contrast, 16dp corner radius
- **Secondary (Outlined):** Border only, medium corner radius
- **Tertiary (Text):** No background, accent color text
- Minimum tap target: 48x48dp

### Cards
- Background: `surfaceContainer` or `surfaceContainerHigh`
- Corner radius: 20-28dp (large)
- Padding: 16-24dp internal
- Elevation: Tonal (not shadow)

### FABs (Floating Action Buttons)
- Shape: Fully rounded (cornerRadius = full)
- Size: Large (96x96dp) or Small (56x56dp)
- Color: Primary container color
- Icon size: 24dp

### List Items
- Each item in rounded container (16dp radius)
- 8dp spacing between items
- Min height: 72dp (with icon)
- Leading icon: 40x40dp container, 24dp icon

### Bottom Bars
- Shorter height: 80dp → 72dp
- Icons: 24dp with labels
- Active indicator: Pill shape behind icon

### Typography
- Use **Google Sans** or **Roboto** font family
- Display (Large headlines): 57sp
- Headline: 32sp
- Title: 22sp
- Body: 16sp
- Label: 14sp

### Motion
- Duration: Fast (150-200ms) for micro-interactions
- Easing: Spring (damping 0.7-0.8)
- Haptic feedback on swipes, taps, confirmations

## Color System (Dynamic)

### Surface Tiers (Dark Theme)
```
background: #1C1B1F
surface: #1C1B1F
surfaceVariant: #49454F
surfaceContainerLowest: #0F0D13
surfaceContainerLow: #1D1A22
surfaceContainer: #211F26
surfaceContainerHigh: #2B2930
surfaceContainerHighest: #36343B
```

### Primary Colors
```
primary: #D0BCFF (Purple in dark theme)
onPrimary: #381E72
primaryContainer: #4F378B
onPrimaryContainer: #EADDFF
```

### Accent Colors
```
secondary: #CCC2DC
tertiary: #EFB8C8
error: #F2B8B5
```

## Google Pixel Style Specifics

### Visual Identity
- **Purple-forward** color scheme (primary)
- **Playful gradients** on hero elements
- **Asymmetric layouts** for visual interest
- **Bold typography** on important text

### Animations
- **Spring-based** motion (not linear)
- **Haptic rumble** on confirmations
- **Morph transitions** between states
- **Parallax scrolling** effects

### Iconography
- **Filled icons** for active states
- **Outlined icons** for inactive
- **Google Material Symbols** (2024 set)
- Icon weight: 400 (regular) or 500 (medium)

## Common Mistakes to Avoid

❌ Flat white/gray cards with no container
❌ Linear animations (boring)
❌ Small tap targets (<48dp)
❌ Low contrast text
❌ No visual hierarchy (everything same size)
❌ Sharp corners (too harsh)
❌ Muted colors (too boring)

✅ Rounded containers with tonal colors
✅ Spring animations with haptics
✅ Large, clear tap targets
✅ Bold primary actions
✅ Size + color hierarchy
✅ 20-28dp corner radius
✅ Vibrant accent colors

## Implementation in Jetpack Compose
```kotlin
// M3 Expressive Card
Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(24.dp), // Large radius
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ),
    elevation = CardDefaults.cardElevation(0.dp) // Tonal, not shadow
) {
    // Content
}

// M3 Expressive Button
Button(
    onClick = { },
    modifier = Modifier.height(56.dp), // Larger
    shape = RoundedCornerShape(16.dp),
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary
    )
) {
    Text("Action", style = MaterialTheme.typography.titleMedium)
}

// Spring Animation
val offsetX by animateFloatAsState(
    targetValue = if (expanded) 100f else 0f,
    animationSpec = spring(
        dampingRatio = 0.7f,
        stiffness = Spring.StiffnessMedium
    )
)
```

## Research Findings
- Users find key actions **4x faster** with M3 Expressive
- **87% of 18-24 year olds** prefer expressive over flat design
- **Older users** spot elements just as fast as young users
- Interaction time **reduced by seconds** across tasks

## References
- Official: https://m3.material.io/
- Research: https://design.google/library/expressive-material-design-google-research
- Figma Kit: Search "Material 3 Design Kit" on Figma Community