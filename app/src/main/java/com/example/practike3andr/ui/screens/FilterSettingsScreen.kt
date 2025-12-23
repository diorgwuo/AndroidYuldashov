package com.example.practike3andr.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practike3andr.ui.viewmodel.FilterSettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: FilterSettingsViewModel = hiltViewModel()
) {
    val filterState by viewModel.filterState.collectAsState()
    
    var nameFilter by remember { mutableStateOf(filterState.nameFilter) }
    var minAge by remember { mutableStateOf(filterState.minAge) }
    var maxAge by remember { mutableStateOf(filterState.maxAge) }
    var sexFilter by remember { mutableStateOf(filterState.sexFilter) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки фильтров") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Фильтры поиска",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Фильтр по имени
            OutlinedTextField(
                value = nameFilter,
                onValueChange = { nameFilter = it },
                label = { Text("Имя актера") },
                placeholder = { Text("Введите имя") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Фильтр по возрасту
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = minAge,
                    onValueChange = { minAge = it },
                    label = { Text("Мин. возраст") },
                    placeholder = { Text("0") },
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = maxAge,
                    onValueChange = { maxAge = it },
                    label = { Text("Макс. возраст") },
                    placeholder = { Text("100") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Фильтр по полу
            OutlinedTextField(
                value = sexFilter,
                onValueChange = { sexFilter = it },
                label = { Text("Пол") },
                placeholder = { Text("Мужской или Женский") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Кнопки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.clearFilters()
                        onBackClick()
                    },
                    modifier = Modifier.weight(1f),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text("Очистить")
                }
                
                Button(
                    onClick = {
                        viewModel.saveFilters(
                            nameFilter,
                            minAge,
                            maxAge,
                            sexFilter
                        )
                        onBackClick()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Применить")
                }
            }
        }
    }
}

