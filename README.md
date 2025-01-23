# Monolithic_Application_Transaction_Processing

1. Custom Annotation for ID: Generate unique ID with String datatype.
2. @CreationTimestamp & @UpdateTimestamp: Auto store creation and update timestamps.
3. @PrePersist: Generate ID if null before persisting.
4. Custom ID Field: Define String field for custom-generated ID.
5. Pincode API (RestTemplate): Fetch city, state, and country using pincode.
6. Insert into Multiple Tables: Use flush() to save data in two tables.
7. JWT (Spring Boot Latest): Implement JWT authentication for secure communication.
8. Custom Parameters in JWT: Store and use custom data (roles, permissions) for CRUD operations.
9. @Builder & @JsonInclude: Build response objects and exclude null values in JSON.
10. OTP for Password (JavaMailSender): Send OTP for password setting/reset.
11. OTP Expiry: Set expiry with LocalDateTime.now().plusMinutes(2).
12. Gmail Mail Sender: Configure Gmail with auth token for sending emails.
13. Global Exception Handler: Handle errors globally with generic/custom responses.
14. Image Renaming: Rename images uniquely to avoid conflicts.
