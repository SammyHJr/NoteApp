package com.example.notesapp
// Import statements
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.compose.material3.OutlinedTextFieldDefaults

// Data class for the notes
data class Note(
    val id: Int,        //UId number so you can itirate through the notes
    var title: String,  //Title for the Note
    var text: String    //Text for the note
)

// repository for storing the notes locally while the app is running
object NoteRepository {
    val notes = mutableStateListOf<Note>()
    private var nextId = 0

    fun addNote(title: String, text: String) {
        notes.add(Note(id = nextId++, title = title, text = text))  // using built in fucntion add to update the ID, title and text
    }

    fun updateNote(noteId: Int, title: String, text: String) {      // when edits is needed
        notes.find { it.id == noteId }?.let {                       // finds the note id and assing it
            it.title = title                                        // assign the already written title
            it.text = text                                          // assigns the already written text
        }
    }

    fun deleteNote(noteId: Int) {
        notes.removeAll { it.id == noteId }                         // find the Id and removes everything
    }

    fun getNote(noteId: Int): Note? {
        return notes.find { it.id == noteId }
    }
}

// Overview Screen: Displays all notes and a FAB to add a new note.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteOverviewScreen(navController: NavHostController) {
    Scaffold(                                               //lay out component that provides a standard structure can be top bar bottom bar etc
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("edit") }) {        // added a route that goes to edit
                Icon(Icons.Default.Add, contentDescription = "Add Note")                        // description of the edit window is Add nnote
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Notes Overview",
                        color = Color(0xFFFFA500)// sets the title text color to orange since orange is not a standard color in jetpack Color(value: xxx) is implemented
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black // sets the app bar background color
                )
            )
        }
    ) { padding ->
        LazyColumn(                 // lazy coloumn makes the the screen vertically scrollable
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
        ) {
            items(NoteRepository.notes) { note ->       // looks through the note repository and displays all the notes
                Card(                                   // is a composable that represents a material design container that can be styled.
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { navController.navigate("detail/${note.id}") },  // makes each of the notes clickable. once clicks it navigates to detail
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFA500).copy(alpha = 0.7f)
                    )

                ) {
                    Column(modifier = Modifier.padding(16.dp)) {        // creates a vertical column that displays the title and text
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = note.text,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

// Detail Screen: shows text and title with option to edit or delete.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(navController: NavHostController, noteId: Int) {
    val note = NoteRepository.getNote(noteId) // guides to the note via the note ID
    if (note == null) {
        // If the note doesn't exist,back to overview.
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }
    Scaffold(
        topBar = {
            TopAppBar(              //code below just affects the topAppBar
                title = {
                    Text("Note Detail", color = Color(0xFFFFA500))  // note header with orange text
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,    // using jetpack compose material arrow back used to popnavstack
                            contentDescription = "Back",
                            tint = Color(0xFFFFA500)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black // Sets the overall background to black.
    ) { padding ->                  // what ever is in the note.id will be displayed below
        Column(                     // using a vertical column to display note.title note.text
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFA500)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFFA500)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {                   //horisontal row to display our 2 buttons with different inputs
                OutlinedButton(
                    onClick = { navController.navigate("edit?noteId=${note.id}") },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color(0xFFFFA500)),     //border strokes is border outlines with the color orange
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFFA500)                // the text inside of the button will also be orange
                    )
                ) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        NoteRepository.deleteNote(note.id)
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFA500),         // here we make the entire container colour orange
                        contentColor = Color.Black                      // here the text colour will be black
                    )
                ) {
                    Text("Delete")
                }
            }

        }
    }
}


// Edit Screen: For creating a new note or editing an existing note.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(navController: NavHostController, noteId: Int?) {        // takes in navController and a noteId as a
    var title by remember { mutableStateOf("") }            //value is empty but is mutable
    var text by remember { mutableStateOf("") }             //value is empty but is mutable
    var errors by remember { mutableStateOf(listOf<String>()) }

    // Pre-populate fields if editing an existing note.
    LaunchedEffect(noteId) {
        if (noteId != null) {
            NoteRepository.getNote(noteId)?.let { note ->           // goes through the note repository to show us all the notes with title and text
                title = note.title
                text = note.text
            }
        }
    }

    // Validation function.
    fun validate(): Boolean {
        val errorList = mutableListOf<String>()
        if (title.length < 3) errorList.add("Title must be at least 3 characters.")         // notes have to be validated title length and text length
        if (title.length > 50) errorList.add("Title must be at most 50 characters.")
        if (text.length > 120) errorList.add("Text must be at most 120 characters.")
        errors = errorList
        return errorList.isEmpty()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (noteId != null) "Edit Note" else "Create Note",     //if the noteId is nonexistant it should sya create note but if it has an Id then we know its to be edited
                        color = Color(0xFFFFA500)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, // using jetpack material in this case. arrowBack
                            contentDescription = "Back",
                            tint = Color(0xFFFFA500)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },             // is invoked everytime the text input is changed
                label = { Text("Title", color = Color.White.copy(alpha = 0.8f)) },
                textStyle = TextStyle(color = Color(0xFFFFA500)),
                shape = RoundedCornerShape(8.dp),           // rounded corners
                modifier = Modifier.fillMaxWidth(),         // makes sure the outlined text is fills the screen doesnt matter which phone it is
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White.copy(alpha = 0.8f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.8f),
                    cursorColor = Color(0xFFFFA500)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Note Text OutlinedTextField fills available space.
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Text", color = Color.White.copy(alpha = 0.8f)) },
                textStyle = TextStyle(color = Color(0xFFFFA500)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White.copy(alpha = 0.8f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.8f),
                    cursorColor = Color(0xFFFFA500)
                )
            )
            // Display any validation errors.
            errors.forEach { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Save button.
            Button(
                onClick = {
                    if (validate()) {               // when clicked the text must be validated before it can continue
                        if (noteId == null) {
                            NoteRepository.addNote(title, text)         // if its a new text do this
                        } else {
                            NoteRepository.updateNote(noteId, title, text)      // if it is a existing text
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save")
            }
        }
    }
}


// MainActivity sets up the navigation between screens.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {                                 // used for styling componenents based on the material guidelines
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "overview") {
                    composable("overview") {
                        NoteOverviewScreen(navController)
                    }
                    composable("detail/{noteId}") { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")?.toInt() ?: -1
                        NoteDetailScreen(navController, noteId)
                    }
                    composable(
                        "edit?noteId={noteId}",
                        arguments = listOf(navArgument("noteId") {
                            defaultValue = -1       // if no note Id is provided then dfaul value is -1
                            type = NavType.IntType  // specifies thay it is a int
                        })
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt("noteId")
                        val idParam = if (noteId == -1) null else noteId    // if the noteId is -1 then it should be null or else it should be the note id.
                        NoteEditScreen(navController, idParam)              // goes to the screen where you can either create or edit screen.
                    }
                }
            }
        }
    }
}
