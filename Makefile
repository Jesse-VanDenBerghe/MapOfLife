.PHONY: build test coverage run

build:
	./gradlew build

test:
	./gradlew testDebugUnitTest --no-daemon

coverage:
	./gradlew testDebugUnitTest jacocoTestReport --no-daemon

run:
	./gradlew installDebug
	adb shell am start -n com.mapoflife/com.mapoflife.ui.MainActivity
