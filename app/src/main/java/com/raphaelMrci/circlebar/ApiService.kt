package com.raphaelMrci.circlebar

import com.raphaelMrci.circlebar.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET(".")
    suspend fun getAvailableCocktails(): Response<MutableList<Cocktail>>

    @POST("login")
    suspend fun tryLogin(@Body post: Login): Response<LoginToken>

    @GET("cocktails")
    suspend fun getCocktails(@Header("Authorization") token: String): Response<MutableList<Cocktail>>

    @POST("cocktails")
    suspend fun createCocktail(
        @Body post: Cocktail,
        @Header("Authorization") token: String
    ): Response<MyResponse>

    @DELETE("cocktails/{id}")
    suspend fun deleteCocktail(
        @Path("id") id : Int,
        @Header("Authorization") token: String
    ): Response<MyResponse>

    @PUT("cocktails/{id}")
    suspend fun editCocktail(
        @Path("id") id : Int,
        @Body put: Cocktail,
        @Header("Authorization") token: String
    ): Response<MyResponse>

    @GET("slots")
    suspend fun getSlots(@Header("Authorization") token: String): Response<MutableList<Slot>>

    @PUT("slots/{id}")
    suspend fun editSlot(
        @Path("id") id : Int,
        @Body put : Slot,
        @Header("Authorization") token: String
    ): Response<MyResponse>

    @DELETE("slots/{id}")
    suspend fun deleteSlot(
        @Path("id") id : Int,
        @Header("Authorization") token: String
    ): Response<MyResponse>

    @GET("drinks")
    suspend fun getDrinks(@Header("Authorization") token: String): Response<Drink>

    @POST("drinks")
    suspend fun createDrink(
        @Body post: Drink,
        @Header("Authorization") token: String
    ): Response<MyResponse>

    @PUT("drinks/{id}")
    suspend fun editDrink(
        @Path("id") id : Int,
        @Body put: Drink,
        @Header("Authorization") token: String
    ): Response<MyResponse>

    @DELETE("drinks/{id}")
    suspend fun deleteDrink(
        @Path("id") id : Int,
        @Header("Authorization") token: String
    ): Response<MyResponse>

    @GET("admin")
    suspend fun getAdmin(@Header("Authorization") token: String): Response<Login>

    @PUT("admin")
    suspend fun editAdmin(
        @Body put: Login,
        @Header("Authorization") token: String
    ): Response<MyResponse>

    //@GET("comments")
    //suspend fun getCommentsByPost(@Query("postId") postId : Int): Response<MutableList<Comment>>
}