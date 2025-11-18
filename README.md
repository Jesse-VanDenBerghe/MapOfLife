# Map Of Life

Modern Android application built with Jetpack Compose, Room database, and Hilt dependency injection.

## Tech Stack

- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Repository Pattern
- **Database**: Room (SQLite)
- **Dependency Injection**: Hilt
- **Language**: Kotlin
- **Build System**: Gradle with Kotlin DSL
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Project Structure

```
app/src/main
├── java/com/mapoflife
│   ├── MyLifeApplication.kt     # Application class with @HiltAndroidApp
│   ├── data/
│   │   ├── local/              # Room database, entities, DAO
│   │   └── repository/         # Repository implementations
│   ├── domain/
│   │   └── repository/         # Repository interfaces
│   ├── ui/
│   │   ├── screen/            # Composables & screens
│   │   ├── viewmodel/         # ViewModels
│   │   └── theme/             # Material3 theme
│   └── di/                     # Hilt modules
├── res/                         # Resources (drawables, layouts, values)
└── AndroidManifest.xml
```

## Setup & Running

### Prerequisites
- Android Studio (2023.1+) or CLI tools
- JDK 21+
- Android SDK (API 34)
- Android Emulator or physical device

### Quick Commands (via Makefile)
```bash
make build    # Build project
make test     # Run unit tests
make run      # Build, install & launch app
```

### Gradle Commands
```bash
./gradlew build              # Full build
./gradlew assembleDebug      # Debug APK (faster, non-optimized)
./gradlew assembleRelease    # Release APK
./gradlew installDebug       # Install on device
./gradlew testDebugUnitTest  # Run unit tests
```

## Key Features

- **Type-safe Gradle**: Kotlin DSL + Version Catalog (`gradle/libs.versions.toml`)
- **Compose UI**: Material 3 declarative UI
- **Room Persistence**: Type-safe SQLite with compile-time verification
- **Hilt DI**: Compile-time safe dependency injection
- **Kotlin Coroutines**: Async operations
- **MVVM Architecture**: Clean UI/logic/data separation

## Development Notes

### Adding Dependencies
Edit `app/build.gradle.kts` in the `dependencies` block. Versions are centralized in `gradle/libs.versions.toml`.

### Creating Screens
1. Create composable in `ui/screen/`
2. Create ViewModel in `ui/viewmodel/` with `@HiltViewModel`
3. Inject ViewModel in composable via `hiltViewModel()`
4. Add Hilt navigation if using Jetpack Navigation

### Database Queries
1. Define entity in `data/local/entity/`
2. Create DAO in `data/local/dao/`
3. Add to `AppDatabase`
4. Create repository in `data/repository/`

## Git Workflow

```bash
# Clone and setup
git clone <repo-url>
cd MapOfLife

# Create feature branch
git checkout -b feature/your-feature

# Build and test
./gradlew build

# Commit (be concise)
git commit -m "add user feature"
```

## Troubleshooting

**Build fails with "unresolved reference"**: Run `./gradlew clean build` to regenerate Hilt & Room classes.

**Emulator won't start**: Ensure virtualization is enabled and Android SDK is up-to-date.

**Room schema validation errors**: Check `app/schemas/` for expected schema and ensure entity changes are backward compatible.

## Next Steps

- Implement application features
- Add networking layer (Retrofit/Ktor)
- Setup Jetpack Navigation
- Add unit/instrumentation tests
- Setup CI/CD with Bitrise

---

**Created**: November 18, 2025  
**Branch**: feature/android-project-setup
