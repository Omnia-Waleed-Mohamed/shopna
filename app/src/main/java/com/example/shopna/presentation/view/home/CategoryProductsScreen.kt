package com.example.shopna.presentation.view.home

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.shopna.R
import com.example.shopna.data.model.CategoryDetailsResponse
import com.example.shopna.presentation.view_model.CartViewModel
import com.example.shopna.presentation.view_model.FavoriteViewModel
import com.example.shopna.presentation.view_model.HomeViewModel
import com.example.shopna.ui.theme.kPrimaryColor
import kotlinx.coroutines.flow.StateFlow

class CategoryProductsScreen(
    private val categoryProducts: StateFlow<CategoryDetailsResponse?>,
    private val nameOfCategories: String,
    private val favoriteViewModel: FavoriteViewModel,
    private val homeViewModel: HomeViewModel,
    private val cartViewModel: CartViewModel
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val categoryProducts = categoryProducts.collectAsState()

        val context = LocalContext.current
        val sharedPreferences =context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langCode = sharedPreferences.getString("langCode", "en")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .systemBarsPadding()
                ) {
                    IconButton(
                        onClick = {
                            navigator.pop()
                        },
                    ) {
                        Icon(
                            imageVector = if (langCode == "ar") Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back to Home",
                            tint = Color.Black.copy(0.7f),
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    Text(
                        text = nameOfCategories,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.interregular))
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                if (homeViewModel.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(LocalConfiguration.current.screenHeightDp.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = kPrimaryColor,
                            modifier = Modifier.align(alignment = Alignment.Center)
                        )
                    }
                } else {
                    categoryProducts.value?.data?.data?.let { products ->
                        ProductGrid(
                            products,
                            favoriteViewModel,
                            Modifier
                                .fillMaxWidth()
                                .height(LocalConfiguration.current.screenHeightDp.dp),
                            cartViewModel
                        )
                    }
                }
            }
        }
    }
}
