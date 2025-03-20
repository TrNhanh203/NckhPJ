package com.example.facilitiesmanagementpj.data.utils

import android.util.Log
import com.google.firebase.storage.FirebaseStorage


fun deleteFileFromFirebaseStorage(filePath: String) {
    val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(filePath)
    storageReference.delete().addOnSuccessListener {
        Log.d("FirebaseStorage", "File deleted successfully: $filePath")
    }.addOnFailureListener { exception ->
        Log.e("FirebaseStorage", "Failed to delete file: $filePath", exception)
    }
}

