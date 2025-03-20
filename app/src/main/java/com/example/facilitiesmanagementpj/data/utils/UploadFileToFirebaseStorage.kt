package com.example.facilitiesmanagementpj.data.utils
import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

import android.util.Log
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.withContext

suspend fun uploadFileToFirebaseStorage(uri: Uri, fileName: String): String? {
    return try {
        withContext(NonCancellable) {
            Log.d("UploadFileToFirebaseStorage", "Starting file upload: $fileName")
            val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child("uploads/$fileName")
            storageReference.putFile(uri).await()
            Log.d("UploadFileToFirebaseStorage", "File upload completed: $fileName")
            val downloadUrl = storageReference.downloadUrl.await()
            val filePath = downloadUrl.toString()
            Log.d("UploadFileToFirebaseStorage", "File uploaded successfully: $filePath")
            filePath
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("UploadFileToFirebaseStorage", "File upload failed", e)
        null
    }
}

