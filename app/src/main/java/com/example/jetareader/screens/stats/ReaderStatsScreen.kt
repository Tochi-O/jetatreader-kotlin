package com.example.jetareader.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetareader.components.ReaderAppBar
import com.example.jetareader.model.MBook
import com.example.jetareader.screens.home.HomeScreenViewModel
import com.example.jetareader.screens.search.BookRow
import com.google.firebase.auth.FirebaseAuth
import com.example.jetareader.model.Item
import com.example.jetareader.navigation.ReaderScreens
import com.example.jetareader.utils.formatDate

import java.util.*

@Composable
fun ReaderStatsScreen(navController: NavController, viewModel: HomeScreenViewModel= hiltViewModel()){


    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(topBar = {
        ReaderAppBar(title = "Book Stats",
            icon = Icons.Default.ArrowBack,
            navController = navController){
            navController.popBackStack()
        }
    }) {
            androidx.compose.material.Surface() {
                //only show books by this user have been read
                books = if (!viewModel.data.value.data.isNullOrEmpty()){
                    viewModel.data.value.data!!.filter { mBook ->
                        (mBook.userId == currentUser?.uid)

                    }
                }else{
                    emptyList()
                }
                Column {
                    Row {
                       Box(modifier = Modifier
                           .size(45.dp)
                           .padding(2.dp)){
                           Icon(imageVector = Icons.Sharp.Person,
                               contentDescription ="icon" )
                       }
                        //paul @ tch.com
                        Text(text = "Hi, ${currentUser?.email.toString().split("@")[0]
                            .uppercase(Locale.getDefault())}")


                    }

                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                        shape = CircleShape,
                        elevation = 5.dp
                    ) {
                        val readBooksList: List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()){
                            books.filter { mBook ->
                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)

                            }
                        }else {
                            emptyList()
                        }

                        val readingbooks=books.filter {mBook ->
                            (mBook.startedReading != null && mBook.finishedReading == null)
                        }
                        Column (modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                            horizontalAlignment = Start){
                            Text(text = "Your Stats", style = MaterialTheme.typography.h5)
                            Divider()
                            Text(text = "You're reading: ${readingbooks.size} books")
                            Text(text = "You've read: ${readBooksList.size} books")

                        }
                    }

                    if (viewModel.data.value.loading == true){
                        LinearProgressIndicator()
                    }else{
                        Divider()
                        LazyColumn(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(), contentPadding = PaddingValues(16.dp)){
                            //filter books by finished ones
                            val readBooks: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()){
                                viewModel.data.value.data!!.filter { mBook ->
                                    (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                                }
                            } else{
                                emptyList()
                            }
                            items(items = readBooks){book->
                                BookRowStats(book = book )
                            }


                        }
                    }

                }

            }


    }

}

@Composable
fun BookRowStats(
    book: MBook,
    ) {
    Card(modifier = Modifier
        .clickable {
            //               navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp) {

        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top) {
            // val imageUrl =  "http://books.google.com/books/content?id=M7ngCAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"

            val imageUrl: String = if (book.photoUrl.toString().isEmpty()){
                "http://books.google.com/books/content?id=M7ngCAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"
            } else {
                book.photoUrl.toString()
            }

            Image(painter = rememberImagePainter(data = imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp))

            Column {
                
                Row(horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(text = book.title.toString()
                        , overflow = TextOverflow.Ellipsis)
                    if(book.rating!! >= 4){
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                        Icon(imageVector = Icons.Default.ThumbUp,
                            contentDescription ="Thumbs up",
                        tint = Color.Green.copy(alpha = 0.5f))
                    }else{
                        Box{}
                    }
                }
                

                Text(text = "Author: ${book.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

                Text(text = "Started: ${formatDate(book.startedReading!!)}",
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

                Text(text = "Finished ${formatDate(book.finishedReading!!)}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)


                //todo add more fields later



            }

        }


    }

}
