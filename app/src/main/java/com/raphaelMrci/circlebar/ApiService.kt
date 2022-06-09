package com.raphaelMrci.circlebar

import com.raphaelMrci.circlebar.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET(".")
    suspend fun getAvailableCocktails(): Response<MutableList<Cocktail>>

    @POST("login")
    suspend fun tryLogin(@Body post: Login): Response<Login>

    @GET("cocktails")
    suspend fun getCocktails(): Response<MutableList<Cocktail>>

    @POST("cocktails")
    suspend fun createCocktail(@Body post: Cocktail): Response<MyResponse>

    @DELETE("cocktails/{id}")
    suspend fun deleteCocktail(@Path("ic") id : Int): Response<MyResponse>

    @PUT("cocktails/{id}")
    suspend fun editCocktail(@Path("id") id : Int, @Body put: Cocktail): Response<MyResponse>

    @GET("slots")
    suspend fun getSlots(): Response<MutableList<Slot>>

    @PUT("slots/{id}")
    suspend fun editSlot(@Path("id") id : Int, @Body put : Slot): Response<MyResponse>

    @DELETE("slots/{id}")
    suspend fun deleteSlot(@Path("id") id : Int): Response<MyResponse>

    @GET("drinks")
    suspend fun getDrinks(): Response<Drink>

    @POST("drinks")
    suspend fun createDrink(@Body post: Drink): Response<MyResponse>

    @PUT("drinks/{id}")
    suspend fun editDrink(@Path("id") id : Int, @Body put: Drink): Response<MyResponse>

    @DELETE("drinks/{id}")
    suspend fun deleteDrink(@Path("id") id : Int): Response<MyResponse>

    @GET("admin")
    suspend fun getAdmin(): Response<Login>

    @PUT("admin")
    suspend fun editAdmin(@Body put: Login): Response<MyResponse>

    //@GET("comments")
    //suspend fun getCommentsByPost(@Query("postId") postId : Int): Response<MutableList<Comment>>
}