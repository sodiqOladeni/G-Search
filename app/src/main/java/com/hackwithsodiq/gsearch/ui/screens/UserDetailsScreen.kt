package com.hackwithsodiq.gsearch.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.Badge
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hackwithsodiq.gsearch.R
import com.hackwithsodiq.gsearch.extensions.isSuccessful
import com.hackwithsodiq.gsearch.model.GithubUser
import com.hackwithsodiq.gsearch.ui.lib.EmptyState
import com.hackwithsodiq.gsearch.ui.theme.Gray2
import com.hackwithsodiq.gsearch.ui.theme.Gray3
import com.hackwithsodiq.gsearch.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun UserDetailsScreen(navController: NavController, user: GithubUser, viewModel: UserViewModel) {

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val uiModel by viewModel.userDetailsScreenUiState.collectAsState()

    if (uiModel.errorMessage.isNotEmpty()){
        LaunchedEffect(Unit){
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = uiModel.errorMessage, duration = SnackbarDuration.Short)
                viewModel.updateUserDetailsScreenErrorMessage()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchUserRepos(user.login)
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
            }
        )
    }, backgroundColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Column(modifier = Modifier
            .padding(16.dp)
            .padding(paddingValues)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                        .data(user?.avatarUrl)
                        .crossfade(true)
                        .build(),
                        error = painterResource(R.drawable.baseline_person_outline_24),
                        placeholder = painterResource(R.drawable.baseline_person_outline_24),
                        contentDescription = "user_image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(80.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(user?.name ?: stringResource(R.string.no_name), fontWeight = FontWeight.Medium)
                    Text(user?.login.orEmpty())
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(user?.bio ?: stringResource(R.string.random_bio), fontWeight = FontWeight.Normal)

            Spacer(modifier = Modifier.height(7.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn, contentDescription = "location", Modifier.size(12.dp), tint = Gray2)
                    Spacer(Modifier.width(2.dp))
                    Text(user?.location ?: stringResource(R.string.no_location), color = Gray2, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))
                Row {
                    Icon(painter = painterResource(R.drawable.ic_clarity_link_line), contentDescription = "website_link")
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(user?.blog ?: stringResource(R.string.no_blog), color = Gray2, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(R.drawable.ic_people), contentDescription = "location", Modifier.size(12.dp), tint = Gray2)
                    Spacer(Modifier.width(4.dp))
                    Text("${user?.followers}", color = Gray2, fontSize = 12.sp)
                    Spacer(Modifier.width(2.dp))
                    Text(stringResource(R.string.followers), color = Gray2, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))
                Row {
                    Text("${user?.following}", color = Gray2, fontSize = 12.sp)
                    Spacer(Modifier.width(2.dp))
                    Text(stringResource(R.string.following), color = Gray2, fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(30.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.repositories))
                Spacer(modifier = Modifier.width(5.dp))
                Box(modifier = Modifier.background(Gray3, shape = RoundedCornerShape(8.dp))) {
                    Text("${user?.publicRepos}", Modifier.padding(horizontal = 3.dp, vertical = 1.dp), fontSize = 10.sp)
                }
            }

            Spacer(Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically,  modifier = Modifier.fillMaxWidth()) {
                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), color = Color.Black)
                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f))
            }

            Spacer(Modifier.height(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                if (uiModel.isLoading){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }else {
                    when {
                        uiModel.statusCode.isSuccessful() -> {
                            if (uiModel.userRepos.isNullOrEmpty()){
                                EmptyState(stringResource(R.string.no_user_repo_message))
                            }else {
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    items(uiModel.userRepos!!) { repo ->
                                        UserRepoCard(
                                            username = repo.fullName,
                                            starCount = repo.stargazersCount,
                                            language = repo.language ?: "NONE",
                                            description = repo.description
                                                ?: stringResource(R.string.no_desc_for_repo)
                                        )
                                    }
                                }
                            }
                        }
                        else -> {
                            EmptyState(stringResource(R.string.search_github_for_repo_message))
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun UserRepoCard(username: String, starCount: Int, language: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(5.dp)),
        backgroundColor = Color.White,
        elevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Text(username, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .background(
                                Color.Transparent,
                            )
                            .border(
                                BorderStroke(1.dp, Gray3), shape = RoundedCornerShape(
                                    corner = CornerSize(5.dp),
                                )
                            ).weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.publicc), Modifier.padding(horizontal = 5.dp), fontSize = 12.sp)
                    }
                }

                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = "repo_star"
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("$starCount", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Badge(
                            backgroundColor = Color.Green,
                            modifier = Modifier.size(8.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(language, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }

            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(description)

            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Forked from discordify", color = Gray2, fontSize = 12.sp)

                Spacer(modifier = Modifier.width(10.dp))
                Text("Updated 4 days ago", color = Gray2, fontSize = 12.sp)
            }
        }
    }
}

@Preview
@Composable
fun Preview_UserRepoCard(){
    UserRepoCard("sodiqOladeni", starCount = 9, language = "Kotlin", description = "These are random words that will be replaced in due time. Config files for my github profile")
}