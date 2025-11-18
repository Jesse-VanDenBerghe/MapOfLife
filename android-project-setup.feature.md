# Feature: Android Project Setup

**Branch**: feature/android-project-setup  
**Created**: November 18, 2025  
**Author**: GitHub Copilot  
**Jira**: N/A

## Overview
Create a modern Android application project called "Map Of Life" using Jetpack Compose for UI, Room for data persistence, and Hilt for dependency injection. This establishes the foundation for a single-module Android app following best practices.

## Problem Statement
Starting a new Android project requires careful configuration of build tools, dependencies, and architecture. A well-structured initial setup ensures maintainability, scalability, and adherence to modern Android development standards. Without proper setup, projects can become difficult to maintain and extend.

## Solution
Create a complete Android project with:
- Modern build configuration using Gradle Kotlin DSL
- Jetpack Compose for declarative UI development
- Room database for local data persistence
- Hilt for dependency injection
- MVVM architecture with clear separation of concerns
- Proper package structure following clean architecture principles

## Implementation Plan

### Phase 1: Project Structure & Configuration
- [X] Create Android project structure with Gradle (Kotlin DSL)
- [X] Configure `settings.gradle.kts` with project name
- [X] Setup root `build.gradle.kts` with:
  - Kotlin version
  - Android Gradle Plugin
  - Compose compiler version
  - Room version
  - Hilt version & plugin
- [X] Configure app module `build.gradle.kts` with:
  - Jetpack Compose dependencies
  - AndroidX core libraries
  - Room dependencies (runtime, compiler, KTX)
  - ViewModel and LiveData
  - Coroutines support
  - Hilt dependencies (android, compiler, navigation-compose)
- [X] Setup `gradle.properties` for optimization
- [X] Create `AndroidManifest.xml` with app configuration

### Phase 2: Core Application Setup
- [X] Create `Application` class annotated with `@HiltAndroidApp`
- [X] Setup package structure:
  - `data/local/` (database, entities, DAOs)
  - `data/repository/` (repository implementations)
  - `domain/repository/` (repository interfaces)
  - `ui/` (composables, screens, viewmodels)
  - `di/` (Hilt modules)
- [X] Create Room database abstract class with Hilt module
- [X] Setup main `MainActivity` with `@AndroidEntryPoint` and Compose

### Phase 3: Basic UI Foundation
- [X] Create Compose theme (Material3)
- [X] Create simple "Hello World" composable to verify setup
- [X] Test app compilation and launch

### Phase 4: Verification & Documentation
- [ ] Verify Gradle builds successfully
- [ ] Create `.gitignore` for Android
- [ ] Create `README.md` with setup instructions
- [ ] Test Room database initialization

## Acceptance Criteria
- ✓ Project builds without errors using `./gradlew build`
- ✓ App launches on emulator/device showing basic UI
- ✓ Room database initializes correctly
- ✓ Hilt dependency injection working
- ✓ All AndroidX and Compose dependencies configured
- ✓ Project follows MVVM architecture pattern
- ✓ Git repository initialized with proper `.gitignore`
- ✓ Documentation complete with setup instructions

## Side Notes
- **Min SDK**: 24 (Android 7.0) - covers ~95% of active devices
- **Target SDK**: 34 (Android 14) - latest stable release
- **Build System**: Gradle with Kotlin DSL for type-safe configuration
- **Architecture**: Single module MVVM for simplicity
- **No networking**: Can be added later when needed

### Technology Versions (to be finalized during implementation)
- Kotlin: 1.9.x
- Compose BOM: 2024.x
- Room: 2.6.x
- Hilt: 2.x
- AGP: 8.x

### Future Enhancements
- Multi-module architecture (if project grows)
- Networking layer (Retrofit/Ktor)
- Remote data synchronization
- Additional Jetpack libraries as needed
