package com.example.chatapp.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FireStorageImageService {
    private final static StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("userIcon");

    public static void getUserIcon(String id){

    }

    public static void uploadNewUserIcon(Context context, Uri filePath){
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading Image...");
            progressDialog.show();
            String uniqueID = UUID.randomUUID().toString();
            storageReference.child(uniqueID)
                    .putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            //update userIcon uniqueId for retrieving in firestore
                            FireStoreDataReference.getUsersReference().document(PersonalInformation.userDocument)
                                    .update("userIcon", uniqueID);
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
        else
            Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show();




    }

}
