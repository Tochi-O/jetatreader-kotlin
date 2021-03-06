package com.example.jetareader.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetareader.components.InputField
import com.example.jetareader.components.ReaderAppBar
import com.example.jetareader.model.Item
import com.example.jetareader.navigation.ReaderScreens
import androidx.compose.ui.text.font.FontStyle


@ExperimentalComposeUiApi
//@Preview
@Composable
fun SearchScreen(navController: NavController,viewModel:BooksSearchViewModel= hiltViewModel()){
        Scaffold(topBar = {
            ReaderAppBar(title = "Search Books",
                icon = Icons.Default.ArrowBack,
                navController = navController,
            showProfile = false,){
                navController.navigate(ReaderScreens.ReaderHomeScreen.name)
            }
        }) {
            Surface() {
                Column {
                    SearchForm(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)){searchQuery ->
                        viewModel.searchBooks(query = searchQuery)

                        Log.d("TAGG", "SearchScreen: $it")
                    }

                    Spacer(modifier = Modifier.height(13.dp))
                    BookList(navController, viewModel)


                }


            }


        }
}

@Composable
fun BookList(navController: NavController,
             viewModel: BooksSearchViewModel = hiltViewModel()) {

    val listOfBooks = viewModel.list
    if (viewModel.isLoading){
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            LinearProgressIndicator()
            Text(text = "Loading...")
        }
    }else{
        LazyColumn(modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)){

            items(items= listOfBooks ){ book->
                BookRow(book, navController)
            }


        }
    }
//    if(viewModel.loadBooks.loading == true){
//        Log.d("BOO", "BookList: loading...")
//    } else {
//
//        Log.d("BOO", "BookList: ${viewModel.listOfBooks.value.data}")
//
//    }


//    val listOfBooks = listOf(
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes=null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes=null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes=null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes=null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes=null),
//
//        )


}

@Composable
fun BookRow(
    book: Item,
    navController: NavController) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
    elevation = 7.dp) {

        Row(modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.Top) {
           // val imageUrl =  "http://books.google.com/books/content?id=M7ngCAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"

            val imageUrl: String = if (book.volumeInfo.imageLinks.smallThumbnail.isEmpty()){
                "http://books.google.com/books/content?id=M7ngCAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"
            } else {
                book.volumeInfo.imageLinks.smallThumbnail
            }

            Image(painter = rememberImagePainter(data = imageUrl),
                contentDescription = "book image",
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
                .padding(end = 4.dp))
            
            Column {
                Text(text = book.volumeInfo.title
                    , overflow = TextOverflow.Ellipsis)
                Text(text = "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.caption)

                Text(text = "Date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

                Text(text = "${book.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)


                //todo add more fields later



            }

        }


    }

}

@ExperimentalComposeUiApi
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String="Search",
    onSearch: (String) -> Unit = {}
){
    Column() {

        val searchQueryState =  rememberSaveable {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current

        val valid = remember(searchQueryState.value){
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(valueState = searchQueryState, labelId = "Search", enabled = true,
        onAction = KeyboardActions(){ if(!valid) return@KeyboardActions
            onSearch(searchQueryState.value.trim())
            searchQueryState.value=""
            keyboardController?.hide()

        })


    }

}









