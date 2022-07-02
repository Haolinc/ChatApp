package com.example.chatapp.data;

import android.app.Person;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FireStorageImageService {
    private final static StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("userIcon");

    public static void setUserIcon(ImageView imageView, String documentName){
        FireStoreDataReference.getUsersReference()
                .document(documentName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String imageUID = (String)documentSnapshot.get("userIcon");
                        if (imageUID != null){
                            storageReference.child(imageUID)
                                    .getBytes(1024*1024)
                                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                }
                            });
                        }
                        else{
                            imageView.setImageResource(R.drawable.userprofileimg);
                        }
                    }
                });
    }

    public static void uploadNewUserIcon(Context context, Uri filePath){
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading Image...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            String uniqueID;
            if (PersonalInformation.userIconCode == null)
                uniqueID = UUID.randomUUID().toString();
            else
                uniqueID = PersonalInformation.userIconCode;

            UploadTask uploadTask = storageReference.child(uniqueID).putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            //update userIcon uniqueId for retrieving in firestore
                            FireStoreDataReference.getUsersReference().document(PersonalInformation.userDocument)
                                    .update("userIcon", uniqueID);
                            PersonalInformation.userIconCode = uniqueID;
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

            //when cancelling the progressDialog
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    uploadTask.cancel();
                }
            });


        }
        else
            Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show();




    }

}
