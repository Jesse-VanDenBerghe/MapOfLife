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
├── kotlin/com/mapoflife
│   ├── MapOfLifeApp.kt          # Application class with @HiltAndroidApp
│   ├── MainActivity.kt           # Main activity with Compose
│   ├── data/
│   │   ├── local/              # Room database & entities
│   │   └── repository/         # Data repository implementations
│   ├── domain/
│   │   └── repository/         # Repository interfaces
│   ├── ui/
│   │   ├── screen/            # Composables and screens
│   │   ├── viewmodel/         # ViewModels
│   │   └── theme/             # Material3 theme
│   └── di/                     # Hilt dependency injection modules
└── AndroidManifest.xml
```

## Setup & Running

### Prerequisites
- Android Studio (2023.1+) or CLI tools
- JDK 17+
- Android SDK (API 34)
- Android Emulator or physical device

### Build
```bash
./gradlew build
```

### Run on Emulator/Device
```bash
./gradlew installDebug
```

### Development Build (faster, non-optimized)
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

## Key Features

- **Type-safe Gradle**: Kotlin DSL for build configuration
- **Compose UI**: Modern declarative UI framework
- **Room Persistence**: Type-safe database queries with compile-time verification
- **Hilt DI**: Automatic dependency management with compile-time safety
- **MVVM Architecture**: Clean separation between UI, business logic, and data

## Development Notes

### Adding Dependencies
Edit `app/build.gradle.kts` in the `dependencies` block. Versions are centralized in `gradle/libs.versions.toml`.

### Creating Screens
1. Create composable in `ui/screen/`
2. Create ViewModel in `ui/viewmodel/`
3. Add Hilt `@HiltViewModel` annotation
4. Inject in MainActivity or parent composable

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
