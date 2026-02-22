package com.edustocks.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    @Value("${firebase.project-id}")
    private String projectId;

    @Value("${FIREBASE_CREDENTIALS_JSON:}")
    private String firebaseCredentialsJson;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount;
            
            // Check if credentials JSON is provided via environment variable (for cloud deployment)
            if (firebaseCredentialsJson != null && !firebaseCredentialsJson.isEmpty()) {
                // Try to decode as base64 first, fallback to plain JSON
                String jsonContent;
                try {
                    jsonContent = new String(Base64.getDecoder().decode(firebaseCredentialsJson), StandardCharsets.UTF_8);
                } catch (IllegalArgumentException e) {
                    // Not base64 encoded, use as-is
                    jsonContent = firebaseCredentialsJson;
                }
                serviceAccount = new ByteArrayInputStream(jsonContent.getBytes(StandardCharsets.UTF_8));
            } else if (credentialsPath != null && !credentialsPath.isEmpty()) {
                // Fallback to file path for local development
                serviceAccount = new FileInputStream(credentialsPath);
            } else {
                throw new RuntimeException("Firebase credentials not configured. Set FIREBASE_CREDENTIALS_JSON env var or firebase.credentials.path property.");
            }
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(projectId)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Bean
    public com.google.cloud.firestore.Firestore firestore() {
        return com.google.firebase.cloud.FirestoreClient.getFirestore();
    }
}



