<img src="/images/ic_launcher-web.png" width="300px" />

Material Hijri Calendar View [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Material%20Hijri%20Calendar%20View-blue.svg?style=flat)](https://github.com/eltohamy/material-hijri-calendarview/)
======================

A Material 3, Jetpack Compose backport of Android's Hijri CalendarView. The goal is to have a
Material look and feel, rather than 100% parity with the platform's implementation.

<img src="/images/screencast.gif" alt="Demo Screen Capture" width="300px" />

Usage
-----

1. Add `implementation 'io.github.eltohamy:material-hijri-calendarview:2.0.1'` to your dependencies.
2. Add `implementation group: 'com.github.msarhan', name: 'ummalqura-calendar', version:'2.0.2'` to your dependencies.
3. Call the `HijriCalendarView` composable, driven by a `HijriCalendarState`.

Example:

```kotlin
val state = rememberHijriCalendarState(
    selectionMode = HijriSelectionMode.SINGLE,
)

HijriCalendarView(
    state = state,
    showOtherDates = HijriShowOtherDates.All,
)

Text("Selected: ${state.selectedDates.firstOrNull() ?: "none"}")
```

See the `sample` module for runnable examples covering decorators, disabled days,
min/max + range selection, and live-adjustable settings.

Major Change in 2.0.1
----------------------
Build-system refresh to match the current Android/Kotlin toolchain:

* All Gradle build files (root, `library`, `sample`, `settings.gradle`) converted from Groovy
  to Kotlin DSL (`build.gradle.kts` / `settings.gradle.kts`).
* Android Gradle Plugin bumped to **9.4.0**, Gradle wrapper to **9.6.0**, Kotlin to **2.4.20**.
* `compileSdk` / `targetSdk` bumped to **37** (Android 17); `minSdk` stays at **23**.
* Compose BOM bumped to **2026.09.00**, with `androidx.core-ktx` and `androidx.annotation`
  bumped to match.
* JDK target stays at **17** (still the minimum/default required by AGP 9.4.0).

No public API changes — this is purely a build-tooling update on top of the 2.0.0 Compose rewrite.

Major Change in 2.0.0
----------------------
Full rewrite from a Java `View`/XML-attribute widget to Kotlin + Jetpack Compose
(`HijriCalendarView` + `HijriCalendarState`). See "Customization" below for the new,
composable-parameter based equivalents of the old `mcv_*` XML attributes and
imperative setters.

Major Change in 1.1.2
---------------------
migrate to AndroidX

Major Change in 1.1.1
---------------------
fix issues.
add mcv_calendarDiff attribute to set difference from ummalqura-calendar in some countries.

Major Change in 1.1.0
---------------------
fix dependencies issue.

Major Change in 1.0.0
---------------------

With the implementation of multiple selection, some of the apis needed to change to support it,
namely `OnDateChangedListener` is now `OnDateSelectedListener`. There are also a bunch of new apis
for multiple selection.

Also, `showOtherDates` is now a set of flags for finer control over which extra dates are shown.

Major Change in 0.8.0
---------------------

The view now responds better to layout parameters.
The functionality is similar to how `adjustViewBounds` works with ImageView,
where the view will try and take up as much space as necessary,
but we base it on tile size instead of an aspect ratio.
The exception being that if a `tileSize` is set,
that will override everything and set the view to that size.

Customization
-------------

One of the aims of this library is to be customizable. Everything that used to be an XML
`mcv_*` attribute or an imperative setter is now a composable parameter or a field on
`HijriCalendarState`:

* Tile size &rarr; `HijriCalendarView(tileSize = 44.dp, ...)`
* Selection mode (single/multiple/range/none) &rarr; `HijriSelectionMode` passed to `rememberHijriCalendarState`
* Showing dates from other months / out of range / decorator-disabled days &rarr; `HijriShowOtherDates`
* First day of the week &rarr; `HijriCalendarState.firstDayOfWeek`
* Restricting to a range of dates &rarr; `minDate`/`maxDate` on `rememberHijriCalendarState`
* Hiding the top bar / disabling paging &rarr; `showTopBar` / `pagingEnabled`
* Custom header, weekday and day labels &rarr; `titleFormatter`, `weekDayFormatter`, `dayFormatter`
* Colors (selection, arrows, text, today indicator) &rarr; `HijriCalendarColors` / `HijriCalendarDefaults.colors()`

### Events, Highlighting, and More

All of this is done via `HijriDayDecorator`: a function `(CalendarDay) -> HijriDayStyle?` that
can disable a day, recolor its background/text, or add a small dot (e.g. for an event marker).
Pass one or more to `HijriCalendarView(decorators = listOf(...))`. See the `sample` module's
`Demos.kt` (`DecoratedScreen`, `DisableDaysScreen`) for worked examples.

> The `docs/` folder documents the pre-2.0 View/XML API and is kept for historical reference only.

Building from source
---------------------

As of 2.0.1, all build files are Kotlin DSL and target:

* Android Gradle Plugin 9.4.0 (Gradle 9.6.0, JDK 17)
* Kotlin 2.4.20
* `compileSdk` / `targetSdk` 37, `minSdk` 23
* Compose BOM 2026.09.00

If you're pulling an older Android Studio/Gradle setup, update those first — AGP 9.x made a
number of breaking changes for Kotlin projects; see JetBrains' migration notes if you hit
plugin-resolution errors going from an 8.x project.

Contributing
============

Would you like to contribute? Fork us and send a pull request! Be sure to checkout our issues first.

License
=======

>Copyright 2015 Prolific Interactive
>
>Licensed under the Apache License, Version 2.0 (the "License");
>you may not use this file except in compliance with the License.
>You may obtain a copy of the License at
>
>   http://www.apache.org/licenses/LICENSE-2.0
>
>Unless required by applicable law or agreed to in writing, software
>distributed under the License is distributed on an "AS IS" BASIS,
>WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
>See the License for the specific language governing permissions and
>limitations under the License.
