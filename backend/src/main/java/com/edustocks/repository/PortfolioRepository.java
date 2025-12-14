package com.edustocks.repository;

import com.edustocks.model.Portfolio;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class PortfolioRepository {
    
    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "portfolios";

    public Portfolio findByUserId(String userId) {
        try {
            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(userId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Portfolio.class);
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to fetch portfolio", e);
        }
    }

    public void save(Portfolio portfolio) {
        try {
            ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME)
                .document(portfolio.getUserId())
                .set(portfolio);
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to save portfolio", e);
        }
    }

    public List<Portfolio> findAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Portfolio> portfolios = new ArrayList<>();
            for (DocumentSnapshot document : documents) {
                portfolios.add(document.toObject(Portfolio.class));
            }
            return portfolios;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to fetch all portfolios", e);
        }
    }

    public void delete(String userId) {
        try {
            ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(userId).delete();
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete portfolio", e);
        }
    }
}



