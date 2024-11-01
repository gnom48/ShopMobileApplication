package com.example.shopmobileapplication.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.shopmobileapplication.R
import java.time.format.TextStyle

val raleway = FontFamily(
    Font(R.font.raleway_bold, FontWeight.Bold),
    Font(R.font.raleway_extra_bold, FontWeight.ExtraBold),
    Font(R.font.raleway_light, FontWeight.Light),
    Font(R.font.raleway_regular, FontWeight.Normal),
    Font(R.font.raleway_medium, FontWeight.Medium),
    Font(R.font.raleway_semi_bold, FontWeight.SemiBold),
    Font(R.font.raleway_thin, FontWeight.Thin)
)

val ralewayTitle = androidx.compose.ui.text.TextStyle(
    fontFamily = raleway,
    color = Color.Black,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    textAlign = TextAlign.Center
)

val ralewaySubtitle = androidx.compose.ui.text.TextStyle(
    fontFamily = raleway,
    color = Color.Black,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    textAlign = TextAlign.Center
)

val ralewayRegular = androidx.compose.ui.text.TextStyle(
    fontFamily = raleway,
    color = Color.Black,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    textAlign = TextAlign.Center
)

val ralewayOnButton = androidx.compose.ui.text.TextStyle(
    fontFamily = raleway,
    color = Color.White,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    textAlign = TextAlign.Center
)

val ralewaySubregular = androidx.compose.ui.text.TextStyle(
    fontFamily = raleway,
    color = greyText,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    textAlign = TextAlign.Center
)