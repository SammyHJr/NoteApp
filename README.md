# 📝 Note-Taking App with Jetpack Compose

This **Note-Taking App** is built using **Jetpack Compose** and **Jetpack Navigation** for Android. It allows users to create, edit, view, and delete notes. The app demonstrates how to handle simple CRUD operations with Jetpack Compose's modern UI and navigation handling.

---

## 🚀 Features

- **Create** new notes.
- **Edit** existing notes.
- **View** a list of all notes.
- **Delete** notes.
- **Input validation** for note title and text.
- **Navigation** with argument passing (e.g., `noteId` for editing).

---

## 📲 Screenshots

Here are some screenshots of the app in action:

![Main Screen](https://via.placeholder.com/400x800?text=Main+Screen)
![Edit Screen](https://via.placeholder.com/400x800?text=Edit+Screen)

---

## 🛠 Tech Stack

- **Kotlin**: Primary language for Android development.
- **Jetpack Compose**: UI toolkit for building modern UIs with Kotlin.
- **Jetpack Navigation**: Handling navigation between different screens and passing arguments.
- **Material Design**: Standard UI components and design patterns.

---

## ⚡ Getting Started

### Prerequisites

1. **Android Studio** installed on your machine.
2. Android emulator or a physical device to run the app.

### Steps to Get Started

1. Clone this repository:

    ```bash
    git clone https://github.com/yourusername/notes-app.git
    ```

2. Open the project in **Android Studio**.

3. Make sure your environment is set up with Kotlin and Jetpack Compose enabled.

4. Sync the Gradle files by clicking `Sync Now` in Android Studio.

5. Run the app on an **emulator** or **physical device**.

---

## 📝 App Usage

### Main Screen
- Displays all notes with their titles and short text.
- Tap on a note to view its details or edit it.
- Use the floating action button (FAB) to create a new note.

### Edit Screen
- Allows creating a new note or editing an existing one.
- **Input validation** ensures the title is between 3-50 characters and the text is no more than 120 characters.
- If no `noteId` is provided, a new note will be created.

### Note Detail Screen
- Displays the full details of a note.
- Includes an option to delete the note.

---

## 🚨 Code Explanation

### Key Screens in the App:

1. **MainScreen**: 
   - Displays the list of notes.
   - Uses `LazyColumn` for displaying a scrollable list.
   - Provides a floating action button (FAB) for creating new notes.

2. **Create/Update Screen**:
   - Handles the creation of a new note and the editing of an existing one.
   - Validation logic checks the title and text inputs before saving.

3. **Note Detail Screen**:
   - Displays the details of a selected note.
   - Allows the user to delete the note.

#### Example Code Snippet:

Here’s a quick look at how notes are added or updated in the app:
