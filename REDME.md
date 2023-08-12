## JOB TASK NAME: Programming Hero Quiz APP

This is an example Android project that demonstrates the integration of Hilt for dependency
injection,
following the MVVM architecture pattern, and using the Glide library for image loading. Count timer
is also used in this project. Shared preference is used to store data locally. 
And Navigation component.

## Features

- Dependency injection using Hilt
- MVVM architecture pattern
- Integration of Glide library for image loading

## Dependencies

- Dagger Hilt: 2.40.5
- Hilt Lifecycle ViewModel: 1.0.0-alpha03
- Glide: 4.12.0
- Navigation Component

## How to Run

1. Clone this repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

## Usage

In this project, i have implemented:

- A custom `Application` class annotated with `@HiltAndroidApp`.
- Dependency injection using Dagger Hilt's `@Module` and `@InstallIn` annotations.
- A ViewModel class following the MVVM architecture pattern, annotated with `@HiltViewModel`.
- A Repository class to manage data operations.
- Integration of the Glide library to load and display images.

## Project Structure

- `app` module: Contains the main application code.
    - `MyApplication.kt`: Custom Application class annotated with `@HiltAndroidApp`.
    - `MainActivity.kt`: Example activity demonstrating the usage of ViewModel and Glide.
    - `MyViewModel.kt`: ViewModel class following MVVM pattern.
- `repository` module: Contains the Repository class.
    - `Repository.kt`: Manages data operations.

## Credits

This project is created by Tanzir Hossan Ratul for assessment purposes. Feel free to use it as a
starting point for your own projects.


