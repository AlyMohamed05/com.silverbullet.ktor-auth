package com.silverbullet.security.hashing

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SHA256HashingServiceTest {

    private lateinit var hashingService: HashingService

    @Before
    fun setup() {
        // Initialize Hashing service
        hashingService = SHA256HashingService()
    }

    @Test
    fun `Generated Salted has is verified successfully`() {

        // Given user credentials.
        val username = "testUser"
        val password = "testPassword"

        // When Generating a hashed password with hashing service
        val saltedHash = hashingService.generateSaltedHash(value = password)

        // Then This password can be verified with hashing service
        val isVerified = hashingService
            .verify(
                value = password,
                saltedHash = saltedHash
            )
        assertTrue(isVerified)
    }
}