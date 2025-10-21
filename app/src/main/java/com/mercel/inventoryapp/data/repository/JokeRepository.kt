package com.mercel.inventoryapp.data.repository

import com.mercel.inventoryapp.data.api.JokeApiService
import com.mercel.inventoryapp.data.model.Joke

class JokeRepository(private val jokeApiService: JokeApiService) {
    
    suspend fun getRandomJoke(): Result<Joke> {
        return try {
            val response = jokeApiService.getRandomJoke()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch joke: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}