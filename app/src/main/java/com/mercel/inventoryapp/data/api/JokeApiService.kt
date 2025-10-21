package com.mercel.inventoryapp.data.api

import com.mercel.inventoryapp.data.model.Joke
import retrofit2.Response
import retrofit2.http.GET

interface JokeApiService {
    
    @GET("random_joke")
    suspend fun getRandomJoke(): Response<Joke>
}