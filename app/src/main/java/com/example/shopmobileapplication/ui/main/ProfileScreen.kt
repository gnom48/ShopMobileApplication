package com.example.shopmobileapplication.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.DataState
import com.example.shopmobileapplication.data.generateBarcode
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.user.UserRepositoryImpl
import com.example.shopmobileapplication.ui.main.components.BarCodeFullScreen
import com.example.shopmobileapplication.ui.main.components.CustomAlertDialog
import com.example.shopmobileapplication.ui.main.components.CustomTopAppBar
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.lightGrayBackground
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.viewmodel.SupabaseViewModel
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel
import com.example.shopmobileapplication.ui.viewmodel.UserViewModelFactory
import com.example.shopmobileapplication.utils.getBitmapFromUri
import com.google.gson.Gson
import io.github.jan.supabase.gotrue.gotrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import java.io.ByteArrayOutputStream

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(null)
}

@Composable
fun ProfileScreen(
    navController: NavController?,
    userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = UserViewModelFactory(
        UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    ),
    supabaseViewModel: SupabaseViewModel = viewModel()
) {
    var barcodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var barcodeWidth by remember { mutableIntStateOf(500) }
    var showBarCodeFullScreen by remember { mutableStateOf(false) }

    var userName by remember { mutableStateOf(UserViewModel.currentUser.name) }
    var userAddress by remember { mutableStateOf(UserViewModel.currentUser.address ?: "" ) }
    var userPhone by remember { mutableStateOf(SupabaseClient.client.gotrue.currentUserOrNull()?.phone ?: "") }

    LaunchedEffect(userViewModel.discountCard) {
        userViewModel.discountCard?.let {
            barcodeBitmap = generateBarcode(
                Gson().toJson(object {
                    @SerialName("card_id") val cardId = "d2534f56e6ge56745"
                    @SerialName("product_example_id") val userId = "4g56456g456g456g56g4"
                }),
                barcodeWidth,
                200
            )
        }
    }

    if (userViewModel.isLoading) {
//        CircularProgressIndicator()
    } else if (userViewModel.error != null) {
        CustomAlertDialog(
            imageResId = R.drawable.message_icon,
            title = stringResource(R.string.error),
            message = stringResource(R.string.data_error),
            onDismiss = {
                userViewModel.dismissError()
            },
            onConfirm = {
                userViewModel.dismissError()
            }
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(lightGrayBackground)) {
        var showFabs by remember { mutableStateOf(false) }
        var reloadAvatar by remember { mutableStateOf<Boolean>(true) }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CustomTopAppBar(
                title = "     " + stringResource(R.string.profile),
                onBackButtonClick = { },
                actionIconButton = {
                    TextButton(
                        modifier = Modifier.background(Color.Transparent),
                        onClick = {
                            userViewModel.updateUserInfo(newPhone = userPhone, newName = userName)
                        }
                    ) {
                        Text(text = stringResource(R.string.complete), style = ralewayRegular, color = blueGradientStart, modifier = Modifier.padding(5.dp))
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    color = Color.LightGray
                ) {
                    var avatarUrl by remember { mutableStateOf<String?>(null) }
                    LaunchedEffect(reloadAvatar) {
                        if (reloadAvatar) {
                            reloadAvatar = false
                            supabaseViewModel.getSignedUrlFromPrivateBucket(UserViewModel.currentUser.id, UserViewModel.currentUser.image.toString()) { url: String? ->
                                avatarUrl = url
                            }
                        }
                    }
                    AsyncImage(
                        model = avatarUrl,
                        error = painterResource(R.drawable.avatar_default_icon),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop
                    )
                }

                Text(text = UserViewModel.currentUser.name, style = ralewaySubtitle, color = Color.Black, modifier = Modifier.padding(10.dp))

                TextButton(
                    modifier = Modifier.background(Color.Transparent),
                    onClick = {
                        showFabs = !showFabs
                    }
                ) {
                    Text(text = stringResource(R.string.edit_avatar), style = ralewayRegular, color = blueGradientStart, modifier = Modifier.padding(5.dp))
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { intSize: IntSize ->
                            barcodeWidth = intSize.width
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (barcodeBitmap == null) {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = stringResource(R.string.discount_card),
                                style = ralewaySubtitle,
                                overflow = TextOverflow.Ellipsis
                            )
                        } else {
                            Image(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        showBarCodeFullScreen = true
                                    },
                                bitmap = barcodeBitmap!!.asImageBitmap(),
                                contentDescription = "Barcode",
                            )
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(R.string.your_name),
                        style = ralewaySubtitle
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(4.dp),
                                imageVector = if (!userName.isNullOrEmpty()) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = "OK",
                                tint = if (!userName.isNullOrEmpty()) blueGradientStart else Color.Red
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        placeholder = { Text(stringResource(R.string.your_name), style = ralewaySubregular) },
                        value = userName,
                        onValueChange = {
                            userName = it
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = lightGrayBackground,
                            unfocusedIndicatorColor = lightGrayBackground
                        ),
                        textStyle = ralewayRegular
                    )

                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(R.string.address),
                        style = ralewaySubtitle
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(4.dp),
                                imageVector = if (!userAddress.isNullOrEmpty()) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = "OK",
                                tint = if (!userAddress.isNullOrEmpty()) blueGradientStart else Color.Red
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        placeholder = { Text(stringResource(R.string.address), style = ralewaySubregular) },
                        value = userAddress,
                        onValueChange = {
                            userAddress = it
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = lightGrayBackground,
                            unfocusedIndicatorColor = lightGrayBackground
                        ),
                        textStyle = ralewayRegular
                    )

                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(R.string.phone),
                        style = ralewaySubtitle
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 120.dp),
                        singleLine = true,
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(4.dp),
                                imageVector = if (!userPhone.isNullOrEmpty()) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = "OK",
                                tint = if (!userPhone.isNullOrEmpty()) blueGradientStart else Color.Red
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        placeholder = { Text("8(800)555-35-35", style = ralewaySubregular) },
                        value = userPhone,
                        onValueChange = {
                            userPhone = it
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = lightGrayBackground,
                            unfocusedIndicatorColor = lightGrayBackground
                        ),
                        textStyle = ralewayRegular
                    )
                }
            }
        }

        if (showFabs) {
            FloatingActionButtons(
                onDismiss = { showFabs = false },
                onAvatarChanged = { reloadAvatar = true },
                userViewModel = userViewModel
            )
        }
    }

    if (showBarCodeFullScreen && barcodeBitmap != null) {
        BarCodeFullScreen(barcodeBitmap = barcodeBitmap) {
            showBarCodeFullScreen = false
        }
    }
}



@Composable
fun FloatingActionButtons(
    onDismiss: () -> Unit,
    onAvatarChanged: () -> Unit,
    modifier: Modifier = Modifier,
    supabaseViewModel: SupabaseViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    if (supabaseViewModel.isLoading) {
//        CircularProgressIndicator()
    } else {
        if (supabaseViewModel.error != null) {
            CustomAlertDialog(
                imageResId = R.drawable.message_icon,
                title = stringResource(R.string.error),
                message = supabaseViewModel.error?.message.toString(),
                onDismiss = {
                    supabaseViewModel.dismissError()
                },
                onConfirm = {
                    supabaseViewModel.dismissError()
                }
            )
        } else if (supabaseViewModel.imgState is DataState.Success)  {
            LaunchedEffect(Unit) {
                userViewModel.updateUserInfo(newImage = "av.jpg")
            }
            CustomAlertDialog(
                imageResId = R.drawable.succes_img,
                title = stringResource(R.string.complete),
                message = stringResource(R.string.avatar_uploaded),
                onDismiss = {
                    supabaseViewModel.dismissSuccess()
                },
                onConfirm = {
                    supabaseViewModel.dismissSuccess()
                }
            )
//            onAvatarChanged()
        }
    }

    val context = LocalContext.current
    val bitmapFromCameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            supabaseViewModel.uploadFileToPrivateBucket(UserViewModel.currentUser.id, "av.jpg", byteArray)
        }
    }
    val bitmapFromGalleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bitmap = getBitmapFromUri(context, it)
            if (bitmap != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                supabaseViewModel.uploadFileToPrivateBucket(UserViewModel.currentUser.id, "av.jpg", byteArray)
            }
        }
    }


    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            bitmapFromCameraLauncher.launch(null)
        }
    }


    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val animLength = 500

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x557F7F7F))
            .clickable(onClick = {
                scope.launch {
                    isVisible = false
                    delay((animLength * 0.8).toLong())
                    onDismiss()
                }
            }),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(animationSpec = tween(animLength), initialOffsetX = { it }) + fadeIn(animationSpec = tween(animLength)),
                exit = slideOutHorizontally(animationSpec = tween(animLength), targetOffsetX = { it }) + fadeOut(animationSpec = tween(animLength))
            ) {
                FloatingActionButton(
                    onClick = {
                        when {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED -> {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                            Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> {
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED -> {
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            }
                            else -> {
                                bitmapFromCameraLauncher.launch(null)
                            }
                        }
                    },
                    shape = CircleShape,
                    containerColor = blueGradientStart
                ) {
                    Icon(
                        modifier = Modifier
                            .size(36.dp, 36.dp)
                            .padding(4.dp),
                        painter = painterResource(R.drawable.baseline_camera_alt_24),
                        contentDescription = "Make photo",
                        tint = Color.White
                    )
                }
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(animationSpec = tween(animLength), initialOffsetX = { it }) + fadeIn(animationSpec = tween(animLength)),
                exit = slideOutHorizontally(animationSpec = tween(animLength), targetOffsetX = { it }) + fadeOut(animationSpec = tween(animLength))
            ) {
                FloatingActionButton(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = {
                        bitmapFromGalleryLauncher.launch("image/*")
                    },
                    shape = CircleShape,
                    containerColor = blueGradientStart
                ) {
                    Icon(
                        modifier = Modifier
                            .size(36.dp, 36.dp)
                            .padding(4.dp),
                        imageVector = Icons.Outlined.AccountBox,
                        contentDescription = "Upload photo",
                        tint = Color.White
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        isVisible = true
    }
}