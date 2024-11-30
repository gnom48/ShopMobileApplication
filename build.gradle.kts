// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    kotlin("plugin.serialization") version "1.7.10"
//    id("org.jetbrains.kotlin.android") version "2.0.0-RC1" apply false
//    kotlin("plugin.serialization") version "2.0.0-RC1"
//    alias(libs.plugins.compose.compiler) apply false
}