package com.example.shopmobileapplication.ui.greetings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.ui.theme.blueGradientEnd
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.raleway
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Preview
@Composable
fun OnboardPreview() {
    Onboard {

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Onboard(goToAuth: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(blueGradientStart, blueGradientEnd))),
    ) {
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()
        val currentPage by rememberUpdatedState(newValue = pagerState.currentPage)

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalPager(
                count = 3,
                state = pagerState,
                modifier = Modifier.fillMaxHeight()
            ) { page ->
                when (page) {
                    0 -> FirstFragment()
                    1 -> SecondFragment()
                    2 -> ThirdFragment()
                }
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(bottom = 35.dp),
                activeColor = Color.White,
                inactiveColor = Color.Gray
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp, 10.dp)
                .clip(RoundedCornerShape(13.dp)),
            contentPadding = PaddingValues(10.dp, 10.dp),
            colors = ButtonDefaults.buttonColors(Color.White),
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage < 2) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        goToAuth()
                    }
                }
            }
        ) {
            Text (
                text = if (currentPage == 0) stringResource(R.string.lets_start) else stringResource(R.string.next),
                color = Color.Black,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }
}

@Preview
@Composable
fun FirstFragment() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            modifier = Modifier.padding(top = 30.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.welcome),
            fontFamily = raleway,
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 35.sp,
            lineHeight = 40.sp
        )
        Image(
            modifier = Modifier.fillMaxHeight(),
            contentScale = ContentScale.FillHeight,
            painter = painterResource(R.drawable.onboard_1_all),
            contentDescription = stringResource(id = R.string.welcome)
        )
    }
}

@Preview
@Composable
fun SecondFragment() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            painter = painterResource(R.drawable.onboard_2_all),
            contentDescription = stringResource(id = R.string.welcome)
        )
        Column(
            modifier = Modifier.padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(R.string.lets_start_trip),
                fontFamily = raleway,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp
            )
            Text(
                modifier = Modifier.padding(top = 30.dp),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.on_board_2_text),
                fontFamily = raleway,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun ThirdFragment() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            painter = painterResource(R.drawable.onboard_3_all),
            contentDescription = stringResource(id = R.string.welcome)
        )
        Column(
            modifier = Modifier.padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(R.string.you_have_force),
                fontFamily = raleway,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp
            )
            Text(
                modifier = Modifier.padding(top = 30.dp),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.onboard_3_text),
                fontFamily = raleway,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}