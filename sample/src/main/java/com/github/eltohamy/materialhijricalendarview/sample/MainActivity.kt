@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.eltohamy.materialhijricalendarview.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/** Demo screens shown from the sample's home list. */
private enum class Demo(val title: String, val description: String) {
    Basic("Basic", "Single-date selection with default styling."),
    Decorated("Decorators", "Highlighting weekends and marking event days."),
    DisableDays("Disabled days", "Disabling specific days (e.g. every Friday)."),
    Range("Min/max + range", "Bounding the selectable range and picking a date range."),
    DynamicSetters("Dynamic setters", "Changing selection mode, first day of week, and topbar visibility live."),
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var selected by remember { mutableStateOf<Demo?>(null) }
                    Crossfade(targetState = selected, label = "demo") { demo ->
                        if (demo == null) {
                            HomeScreen(onSelect = { selected = it })
                        } else {
                            DemoScaffold(title = demo.title, onBack = { selected = null }) {
                                when (demo) {
                                    Demo.Basic -> BasicScreen()
                                    Demo.Decorated -> DecoratedScreen()
                                    Demo.DisableDays -> DisableDaysScreen()
                                    Demo.Range -> RangeScreen()
                                    Demo.DynamicSetters -> DynamicSettersScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
private fun HomeScreen(onSelect: (Demo) -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Material Hijri CalendarView") }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(Demo.entries) { demo ->
                ListItem(
                    headlineContent = { Text(demo.title) },
                    supportingContent = { Text(demo.description) },
                    modifier = Modifier.clickable { onSelect(demo) },
                )
            }
        }
    }
}

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
private fun DemoScaffold(title: String, onBack: () -> Unit, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}
