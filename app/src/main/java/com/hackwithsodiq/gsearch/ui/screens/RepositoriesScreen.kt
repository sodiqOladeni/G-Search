package com.hackwithsodiq.gsearch.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Badge
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.Card
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarDuration
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hackwithsodiq.gsearch.R
import com.hackwithsodiq.gsearch.extensions.isSuccessful
import com.hackwithsodiq.gsearch.model.Constants
import com.hackwithsodiq.gsearch.ui.lib.EmptyState
import com.hackwithsodiq.gsearch.ui.lib.ProgressButton
import com.hackwithsodiq.gsearch.ui.theme.ChipLightBlue
import com.hackwithsodiq.gsearch.ui.theme.DeepShape
import com.hackwithsodiq.gsearch.ui.theme.Gray
import com.hackwithsodiq.gsearch.ui.viewmodel.RepositoryViewModel
import kotlinx.coroutines.launch

@Composable
fun RepositoriesScreen(navController: NavController, viewModel: RepositoryViewModel = hiltViewModel()) {

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val uiModel by viewModel.repoScreenUiState.collectAsState()
    var inputQuery by rememberSaveable { mutableStateOf("") }

    if (uiModel.errorMessage.isNotEmpty()){
        LaunchedEffect(Unit){
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = uiModel.errorMessage, duration = SnackbarDuration.Short)
                viewModel.updateErrorMessage()
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = {
                Text(stringResource(R.string.repositories))
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
                onValueChange = {inputQuery = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                placeholder = {
                    Text(stringResource(R.string.search_for_repositories), color = Gray)
                },
                trailingIcon = {
                    Row {
                        ProgressButton(text = stringResource(R.string.search),
                            isLoading = uiModel.isLoading, isEnabled = inputQuery.isNotEmpty()) {
                            viewModel.queryRepositories(inputQuery)
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
                    uiModel.statusCode.isSuccessful() -> {
                        if (uiModel.repos.isNullOrEmpty()){
                            EmptyState(stringResource(R.string.no_repo_message))
                        }else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(uiModel.repos!!) { repo ->
                                    RepoCard(
                                        fullName = repo.fullName,
                                        avatarUrl = repo.owner?.avatarUrl.orEmpty(),
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

@Composable
fun RepoCard(fullName: String, avatarUrl: String, starCount: Int, language: String, description: String) {
    Card(
        modifier = Modifier
            .height(140.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(5.dp)),
        backgroundColor = Color.White,
        elevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
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
                    Text(fullName, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End, modifier = Modifier.weight(1f)) {
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
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(Constants.cats) {
                    CategoryChips(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryChips(text: String) {
    Chip(
        onClick = {},
        shape = RoundedCornerShape(6.dp),
        colors = ChipDefaults.chipColors(backgroundColor = ChipLightBlue, contentColor = DeepShape)
    ) {
        Text(text, fontSize = 12.sp)
    }
}

@Preview
@Composable
fun Preview_CategoryChips(){
    CategoryChips("System design")
}

@Preview
@Composable
fun Preview_RepoCard(){
    RepoCard("sodiqOladeni", "", starCount = 9, language = "Kotlin", description = "These are random words that will be replaced in due time. Config files for my github profile")
}
