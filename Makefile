.PHONY: build test run

build:
	./gradlew build

test:
	./gradlew testDebugUnitTest

run:
	./gradlew installDebug
	adb shell am start -n com.mapoflife/com.mapoflife.ui.MainActivity
