package com.wnapp.trustmoney.ui.more

import android.app.Application
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wnapp.trustmoney.data.model.CompanyDocModel
import com.wnapp.trustmoney.viewmodel.AppViewModel
import com.wnapp.trustmoney.viewmodel.AuthViewModel

// আপনার প্রোজেক্টের মডেল এবং ভিউমডেল পাথ অনুযায়ী এগুলো চেক করে নিন:
// import com.your.package.name.CompanyDocModel
// import com.your.package.name.DocViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDocsScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedDoc by remember { mutableStateOf<CompanyDocModel?>(null) }
    val factory = remember { AppViewModel.AppViewModelFactory(context.applicationContext as Application) }
    val appVM: AppViewModel = viewModel(factory = factory)
    LaunchedEffect(Unit) {
        appVM.loadDocs()
    }

    // ডায়ালগ ওপেন লজিক
    selectedDoc?.let { doc ->
        DocumentViewerDialog(doc = doc, onDismiss = { selectedDoc = null })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Company Documents", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF008346))
            )
        }
    ) { padding ->
        if (appVM.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF008346))
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF5F5F5)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(appVM.docList) { doc ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { selectedDoc = doc },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (doc.fileUrl.endsWith(".pdf")) Icons.Default.Description else Icons.Default.Image,
                                contentDescription = null,
                                tint = Color(0xFF008346),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(doc.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(doc.docType, color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DocumentViewerDialog(doc: CompanyDocModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = Color(0xFF008346)) }
        },
        title = { Text(doc.title, fontWeight = FontWeight.Bold) },
        text = {
            Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                if (doc.fileUrl.endsWith(".pdf", ignoreCase = true)) {
                    // PDF দেখার জন্য WebView (Google Docs viewer ব্যবহার করে)
                    AndroidView(factory = { context ->
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            webViewClient = WebViewClient()
                            loadUrl("https://docs.google.com/gview?embedded=true&url=${doc.fileUrl}")
                        }
                    }, modifier = Modifier.fillMaxSize())
                } else {
                    // ইমেজ দেখার জন্য Coil
                    AsyncImage(
                        model = doc.fileUrl,
                        contentDescription = "Document Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    )
}