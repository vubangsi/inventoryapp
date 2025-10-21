package com.mercel.inventoryapp.data.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mercel.inventoryapp.data.api.RetrofitClient
import com.mercel.inventoryapp.data.repository.JokeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JokeFetchWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    private val jokeRepository = JokeRepository(RetrofitClient.jokeApiService)
    
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val jokeResult = jokeRepository.getRandomJoke()
                if (jokeResult.isSuccess) {
                    Result.success()
                } else {
                    Result.retry()
                }
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}