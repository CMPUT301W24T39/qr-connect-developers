package com.example.qrconnect;

import android.nfc.Tag;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;

public class StorageService {
    private FirebaseFirestore db;
    private CollectionReference collectionRef;

    public StorageService(String collectionPath) {
        db = FirebaseFirestore.getInstance();
        collectionRef = db.collection(collectionPath);
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    public CollectionReference getCollectionRef() {
        return collectionRef;
    }

    public void setCollectionRef(CollectionReference collectionRef) {
        this.collectionRef = collectionRef;
    }
}
