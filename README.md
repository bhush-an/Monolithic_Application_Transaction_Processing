# Monolithic_Transaction_Processing

1. Custom annotation - to generate custom ID with datatype as String
2. @CreationTimestamp and @UpdateTimeStsmp - automatically stores this date and time
3. @PrePersist - will generate ID if null
4. Declare field to generate custom ID with datatype as String
5. Hit pincode API to store city, state, country in DB - RestTemplate
6. Insert data into 2 tables using only one method - using flush() since data is stored at the end of method
7. JWT as per new springboot version
8. Store custom parameters in JWT token and use it to get/post/put/delete different operations
9. @Builder -- @JsonInclude(JsonInclude.Include.NON_NULL) -- using builder pattern to send response
10. OTP based password setting - JavaMailSender dependency - SimpleMailMessage class
11. Setting OTP expiry - expiresAt(LocalDateTime.now().plusMinutes(2))
12. Gmail based mail sender - auth token required to do this
13. Global Exception Handler - generic and custom
14. Image handling - renaming each image with unique name to avoid discrepancy
