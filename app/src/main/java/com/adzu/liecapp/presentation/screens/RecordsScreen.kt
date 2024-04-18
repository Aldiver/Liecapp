package com.adzu.liecapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adzu.liecapp.R
import com.adzu.liecapp.api.main.models.EntryInfo
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.EntryRecordViewModel
import com.adzu.liecapp.utils.ApiResponse

@Composable
fun RecordsScreen(
    recordViewModel: EntryRecordViewModel = hiltViewModel()
) {
    // Define search query state
    val searchQuery = remember { mutableStateOf("") }

    val entryInfoState by
    recordViewModel.allEntryRecordsResponse.observeAsState(ApiResponse.Loading)
    val lifecycleOwner = LocalLifecycleOwner.current
    val status = remember { mutableStateOf("No Error found") }

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val pxValue = with(LocalDensity.current) { screenHeight.toPx() }

    // Fetch list of vehicles when the screen is first shown
    LaunchedEffect(key1 = true) {
        recordViewModel.getAllEntryRecords(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                status.value = "Error! $message"
            }
        })
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Blue, Color.Blue, Color.Black),
                startY = 0f,
                endY = pxValue, // Adjust the endY value as needed
            )
        ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column{
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
            )

            when (entryInfoState) {
                is ApiResponse.Loading -> {
                    Image(
                        painter = painterResource(id = R.drawable.paper),
                        contentDescription = null,
                    )
                }

                is ApiResponse.Success -> {
                    // Show list of vehicles
                    val entryInfo = (entryInfoState as ApiResponse.Success<List<EntryInfo>>).data
                    val filteredEntries = entryInfo.filter { entry ->
                        entry.owner.contains(searchQuery.value, true) ||
                                entry.vehicle_plate_number.contains(searchQuery.value, true)
                    }
                    EntryInfoList(entryInfos = filteredEntries)
                }

                is ApiResponse.Failure -> {
                    // Show error message
                    val errorMessage = (entryInfoState as ApiResponse.Failure).errorMessage
                    Text(text = "Error: $errorMessage", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun EntryInfoList(entryInfos: List<EntryInfo>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1), // 2 columns
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(entryInfos) { record ->
            EntryInfoListItem(entryInfo = record)
        }
    }
}

@Composable
fun EntryInfoListItem(entryInfo: EntryInfo) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Owner: ${entryInfo.owner}")
            Text(text = "Timestamp: ${entryInfo.timestamp}")
            Text(text = "Plate Number: ${entryInfo.vehicle_plate_number}")
            Text(text = "Date: ${entryInfo.date}")
            // Add more text fields for other vehicle properties as needed
        }
    }
}

