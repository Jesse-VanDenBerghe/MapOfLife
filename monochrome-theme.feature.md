## Feature: Monochrome Theme

**Branch**: feature/monochrome-theme  
**Created**: November 18, 2025  
**Author**: GitHub Copilot  
**Jira**: N/A

## Overview
Transform app to pure monochrome aesthetic with black background, white text, and sharp rectangular corners on all UI components.

## Problem Statement
Current theme uses Material 3 default colors (purple/pink palette) with rounded corners. Need stark, minimalist monochrome design for better visual focus and modern aesthetic.

## Solution
Replace entire color palette with black/white monochrome scheme (keeping errors red for visibility). Remove all rounded corners by applying RectangleShape to Material 3 shapes. Force dark theme always.

## Implementation Plan

### Phase 1: Core Theme Changes
- [x] Update Color.kt - Replace all colors with monochrome palette (black/white) except error (red)
- [x] Create Shape.kt - Define RectangleShape (0dp corners) for all component shapes  
- [x] Update Theme.kt - Apply monochrome ColorScheme + sharp Shapes, remove system theme toggle
- [x] Test build - Verify app compiles and renders

### Phase 2: Validation & Polish
- [ ] Visual verification - Launch app, check HomeScreen renders correctly
- [ ] Component audit - Test buttons, cards, surfaces have sharp corners and correct colors

## Acceptance Criteria
- Background is pure black (#000000)
- Text is pure white (#FFFFFF)
- All rounded corners removed (buttons, cards, borders, etc)
- Errors show red (#FF0000 or similar) for visibility
- No system theme toggle (always monochrome dark mode)
- App builds and runs without crashes
- All existing UI components render correctly with new theme

## Side Notes
- Material 3 supports custom shapes via Shapes parameter in MaterialTheme
- ColorScheme supports 40+ semantic color slots - all need monochrome mapping
- Existing screens (HomeScreen) should automatically adapt via MaterialTheme tokens
