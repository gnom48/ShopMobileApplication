package com.example.shopmobileapplication.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.user.UserRepositoryImpl
import com.example.shopmobileapplication.ui.Layouts
import com.example.shopmobileapplication.ui.main.components.CustomAlertDialog
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.lightGrayBackground
import com.example.shopmobileapplication.ui.theme.ralewayOnButton
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.ralewayTitle
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel
import com.example.shopmobileapplication.ui.viewmodel.UserViewModelFactory
import com.example.shopmobileapplication.utils.SharedPreferecesHelper

@Preview
@Composable
fun SignInPreview() {
    SignIn(null)
}

@Composable
fun SignIn(
    navController: NavController?,
    userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = UserViewModelFactory(
        UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    val context = LocalContext.current
    val sharedPreferencesHelper = SharedPreferecesHelper(context)

    if (userViewModel.isLoading) {
//        CircularProgressIndicator()
    } else if (userViewModel.error != null) {
        CustomAlertDialog(
            imageResId = R.drawable.message_icon,
            title = stringResource(R.string.error),
            message = stringResource(R.string.try_enter_again),
            onDismiss = {
                userViewModel.dismissError()
            },
            onConfirm = {
                userViewModel.dismissError()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        val lastEmail = sharedPreferencesHelper.getStringData(SharedPreferecesHelper.lastEmailKey)
        val lastPassword = sharedPreferencesHelper.getStringData(SharedPreferecesHelper.lastPasswordKey)
        var userEmail by remember {
            mutableStateOf(if (lastEmail.isNullOrEmpty()) "" else lastEmail)
        }
        var userPassword by remember {
            mutableStateOf(if (lastPassword.isNullOrEmpty()) "" else lastPassword)
        }
        var passwordVisible by remember { mutableStateOf(false) }

        IconButton(
            modifier = Modifier
                .padding(10.dp),
            onClick = {
                navController!!.navigate(Layouts.ONBOARD_LAYOUT) {
                    popUpTo(Layouts.SIGN_IN_LAYOUT) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        ) {
            Surface(
                modifier = Modifier
                    .background(Color.Transparent),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp, 24.dp)
                        .background(lightGrayBackground),
                    painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                    contentDescription = "back"
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.hello),
                style = ralewayTitle
            )
            Text(
                modifier = Modifier.padding(20.dp),
                text = stringResource(R.string.sign_in_text),
                style = ralewaySubregular
            )
        }

        Text(
            modifier = Modifier.padding(10.dp),
            text = stringResource(R.string.email),
            style = ralewaySubtitle
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            placeholder = { Text("xyz@gmail.com", style = ralewaySubregular) },
            value = userEmail,
            onValueChange = {
                userEmail = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = lightGrayBackground,
                unfocusedIndicatorColor = lightGrayBackground
            ),
            textStyle = ralewayRegular
        )

        Text(
            modifier = Modifier.padding(top = 30.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
            text = stringResource(R.string.password),
            style = ralewaySubtitle
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            placeholder = { Text("••••••••", style = ralewaySubregular) },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        painter = if (passwordVisible) painterResource(R.drawable.baseline_hide_eye_24) else painterResource(R.drawable.baseline_show_eye_24),
                        modifier = if (passwordVisible) Modifier.size(36.dp, 36.dp) else Modifier.size(24.dp, 24.dp),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = userPassword,
            onValueChange = {
                userPassword = it
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = lightGrayBackground,
                unfocusedIndicatorColor = lightGrayBackground
            ),
            textStyle = ralewayRegular
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            TextButton(
                onClick = {
                    navController!!.navigate(Layouts.FORGOT_PASSWORD_LAYOUT) {
                        popUpTo(Layouts.SIGN_IN_LAYOUT) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(10.dp, 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    style = ralewaySubregular,
                    textAlign = TextAlign.Right
                )
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp, 20.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentPadding = PaddingValues(10.dp, 10.dp),
            colors = ButtonDefaults.buttonColors(blueGradientStart),
            onClick = {
                userViewModel.signIn(userEmail, userPassword)
            }
        ) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = stringResource(R.string.enter),
                style = ralewayOnButton
            )
        }

        TextButton(
            onClick = {
                navController!!.navigate(Layouts.SIGN_UP_LAYOUT) {
                    popUpTo(Layouts.SIGN_IN_LAYOUT) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Bottom)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                text = stringResource(R.string.go_to_sign_up),
                style = ralewayRegular,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            text = stringResource(R.string.or),
            style = ralewayRegular,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        TextButton(
            onClick = {
                userViewModel.anonSignIn()
            }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Bottom)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                text = stringResource(R.string.as_gueat),
                style = ralewayRegular,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    LaunchedEffect(userViewModel.user) {
        userViewModel.user?.let { user ->
            navController?.navigate(Layouts.MAIN_LAYOUT) {
                popUpTo(Layouts.SIGN_IN_LAYOUT) {
                    inclusive = true
                }
                launchSingleTop = true
            }
            SharedPreferecesHelper(context).saveStringData(SharedPreferecesHelper.seenOnBoardKey, user.id)
        }
    }


//    userViewModel.user?.let {
//        navController?.navigate(Layouts.MAIN_LAYOUT) {
//            popUpTo(Layouts.SIGN_IN_LAYOUT) {
//                inclusive = true
//            }
//            launchSingleTop = true
//        }
//    }

}