package com.app.productcatalog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.productcatalog.domain.model.ProductSpec
import com.app.productcatalog.util.StringUtils
import kotlinx.coroutines.launch

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    snackBarState: SnackbarHostState,
    onEventChange: (UiEvent) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(FilterOption.All) }
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp
    val scope = rememberCoroutineScope()

    val productCountText = buildAnnotatedString {
        append("Ditemukan ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${uiState.successGetProducts.size}")
        }
        append(" Produk")
    }

    LaunchedEffect(key1 = uiState.error, block = {
        uiState.error?.consumeOnce { exception ->
            scope.launch {
                snackBarState.showSnackbar(
                    exception.message ?: "Telah Terjadi Kesalahan",
                )
            }
        }
    })

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onEventChange(UiEvent.OnUpdateSearchQuery(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("SearchBar"),
                placeholder = { Text("Cari Barang") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,

                )

            FilterListItem(
                selectedOption = selectedFilter,
                onOptionSelected = { filter ->
                    selectedFilter = filter
                    onEventChange(UiEvent.OnFavouritesFilterClick(filter))
                }
            )

            Text(
                text = productCountText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(
                    items = uiState.successGetProducts,
                    key = {
                        it.id
                    }
                ) { product ->
                    ProductItem(
                        modifier = Modifier
                            .height((height * 0.45).dp)
                            .testTag(product.name),
                        product,
                        onFavouriteClick = {
                            onEventChange(
                                UiEvent.OnFavouritesProductSelected(
                                    product
                                )
                            )
                        })
                }
            }
        }
        if (uiState.isLoading) {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: ProductSpec,
    onFavouriteClick: (ProductSpec) -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {

            AsyncImage(
                model = product.imageUrl, contentDescription = product.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(), contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                product.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.description, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                StringUtils.stringToRupiah(product.price),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { onFavouriteClick(product) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = if (product.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (product.isFavourite) "Unsave from favourites" else "Save to favourites",
                    tint = if (product.isFavourite) Color.Red else Color.Gray
                )
            }
        }
    }
}

@Composable
fun FilterListItem(
    selectedOption: FilterOption,
    onOptionSelected: (FilterOption) -> Unit
) {
    val options = listOf(FilterOption.All, FilterOption.Favourites, FilterOption.NotFavourites)
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(options) { option ->
            FilterChip(selected = selectedOption == option, onClick = {
                onOptionSelected(option)
            }, label = { Text(option.text) }, modifier = Modifier.testTag(option.text))
        }
    }
}

enum class FilterOption(val value: Int, val text: String) {
    All(-1, "Semua"), Favourites(1, "Favorit"), NotFavourites(0, "Tidak Favorit")
}
