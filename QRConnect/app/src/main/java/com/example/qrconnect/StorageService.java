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

/**
 * The StorageService class provides methods to interact with Firebase Firestore for storing and retrieving data.
 */
public class StorageService {
    private FirebaseFirestore db;
    private CollectionReference collectionRef;

    /**
     * Constructs a StorageService object with the specified collection path.
     * @param collectionPath The path of the collection to interact with in Firestore.
     */
    public StorageService(String collectionPath) {
        db = FirebaseFirestore.getInstance();
        collectionRef = db.collection(collectionPath);
    }

    /**
     * Retrieves the Firestore instance associated with this storage service.
     * @return The Firestore instance.
     */
    public FirebaseFirestore getDb() {
        return db;
    }

    /**
     * Sets the Firestore instance for this storage service.
     * @param db The Firestore instance to set.
     */
    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    /**
     * Retrieves the collection reference associated with this storage service.
     * @return The collection reference.
     */
    public CollectionReference getCollectionRef() {
        return collectionRef;
    }

    /**
     * Sets the collection reference for this storage service.
     * @param collectionRef The collection reference to set.
     */
    public void setCollectionRef(CollectionReference collectionRef) {
        this.collectionRef = collectionRef;
    }
}
