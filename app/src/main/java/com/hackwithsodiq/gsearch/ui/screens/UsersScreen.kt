package com.hackwithsodiq.gsearch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hackwithsodiq.gsearch.R
import com.hackwithsodiq.gsearch.extensions.isSuccessful
import com.hackwithsodiq.gsearch.extensions.notFound
import com.hackwithsodiq.gsearch.model.GithubUser
import com.hackwithsodiq.gsearch.ui.lib.EmptyState
import com.hackwithsodiq.gsearch.ui.lib.ProgressButton
import com.hackwithsodiq.gsearch.ui.theme.DeepShape
import com.hackwithsodiq.gsearch.ui.theme.Gray
import com.hackwithsodiq.gsearch.ui.theme.Gray2
import com.hackwithsodiq.gsearch.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun UsersScreen(navController: NavController, viewModel: UserViewModel = hiltViewModel(), onNavigateToDetails: (GithubUser) -> Unit ) {

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val uiModel by viewModel.usersScreenUiState.collectAsState()
    var inputQuery by rememberSaveable { mutableStateOf("") }

    if (uiModel.errorMessage.isNotEmpty()){
        LaunchedEffect(Unit){
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = uiModel.errorMessage, duration = SnackbarDuration.Short)
                viewModel.updateUserScreenErrorMessage()
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = {
                Text(stringResource(R.string.users))
            },
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colorScheme.background,
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        Icons.Outlined.ArrowBackIosNew,
                        contentDescription = "back_home",
                        tint = Color.Black
                    )
                }
            })
    }, backgroundColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                inputQuery,
                onValueChange = { inputQuery= it},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                placeholder = {
                    Text(stringResource(R.string.search_for_users), color = Gray)
                },
                trailingIcon = {
                    Row {
                        ProgressButton(text = stringResource(R.string.search),
                            isLoading = uiModel.isLoading, isEnabled = inputQuery.isNotEmpty()) {
                            viewModel.queryUsers(inputQuery)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                },
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.ic_search_unselected),
                        contentDescription = "search", tint = Gray
                    )
                },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                when {
                    uiModel.statusCode.notFound() -> {
                        EmptyState(stringResource(R.string.no_user_message))
                    }
                    uiModel.statusCode.isSuccessful() && uiModel.user != null -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(listOf(uiModel.user!!)){
                                UserCard(it.name ?: stringResource(R.string.no_name), "@${it.login}", it.bio ?: stringResource(R.string.random_bio), location = it.location ?: stringResource(R.string.random_location),
                                    email = it.email ?: stringResource(R.string.random_email), avatarUrl = it.avatarUrl.orEmpty()){
                                    onNavigateToDetails(it)
                                }
                            }
                        }
                    }
                    else -> {
                        EmptyState(stringResource(R.string.search_github_for_users_message))
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(name: String, loginName: String, bio: String, location: String, email: String, avatarUrl: String, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .height(140.dp)
            .fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(corner = CornerSize(5.dp)),
        backgroundColor = Color.White,
        elevation = 1.dp
    ) {
        Row(Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp)
                    .background(
                        Color.Transparent,
                        shape = RoundedCornerShape(
                            corner = CornerSize(100),
                        ),
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl)
                    .crossfade(true)
                    .build(),
                    error = painterResource(R.drawable.baseline_person_outline_24),
                    placeholder = painterResource(R.drawable.baseline_person_outline_24),
                    contentDescription = "user_image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape).size(80.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(name, color = DeepShape)
                Text(loginName)

                Spacer(modifier = Modifier.height(10.dp))
                Text(bio, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(location, color = Gray2, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(email, color = Gray2, fontSize = 12.sp)
                }
            }
        }
    }

}