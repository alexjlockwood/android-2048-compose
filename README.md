# compose-multiplatform-2048

A simple 2048 app written with 100% Jetpack Compose Multiplatform. It is available on the following platforms:

- Android
- iOS
- Desktop
- Web (available [here](https://alexjlockwood.github.io/compose-multiplatform-2048/))

![Screen capture of app](art/twenty-forty-eight.webm)

Use `./gradlew :composeApp:run` to run the Desktop app.

Use `./gradlew wasmJsBrowserRun -t --quiet` to run the web app.

Thank you to [Yuya](https://github.com/oikvpqya) for migrating the app to use KStore to better support Compose Multiplatform!
