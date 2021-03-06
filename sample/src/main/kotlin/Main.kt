package com.github.dimsuz.diffdispatcher.sample

import com.github.dimsuz.diffdispatcher.annotations.DiffElement
import com.github.dimsuz.diffdispatcher.sample.UserInfoViewState.Interest

@DiffElement(diffReceiver = UserInfoRenderer::class)
data class UserInfoViewState(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val address: Address,
    val friends: List<UserInfoViewState>,
    val interests: List<Interest>?,
    val age: Int,
    val popularity: Float,
    val likesCheese: Boolean,
    val isIntelligent: Boolean?,
    val isHumble: Boolean,
    val favoriteShapes: List<Shape>
) {
    data class Address(
        val street: String,
        val house: Int
    )
    data class Interest(
        val name: String,
        val tags: List<String>
    )
}

sealed class Shape {
    data class Square(val size: Int): Shape()
    data class Rectangle(val width: Int, val height: Int): Shape()
}

interface UserInfoRenderer {
    fun renderName(firstName: String, middleName: String?, lastName: String)
    fun renderFirstName(firstName: String)
    fun renderUserAddress(address: UserInfoViewState.Address)
    fun renderFriends(friends: List<UserInfoViewState>)
    fun renderFriendsAndInterests(friends: List<UserInfoViewState>, interests: List<Interest>?)
    fun renderPopularity(popularity: Float)
    fun renderAgeAndCheesePreference(age: Int, likesCheese: Boolean)
    fun renderAge(age: Int)
    fun renderHumbleness(isHumble: Boolean)
    fun renderFavoriteShapes(favoriteShapes: List<Shape>)
}

class SampleRenderer : UserInfoRenderer {

    override fun renderName(firstName: String, middleName: String?, lastName: String) {
        println("rendering name $lastName $firstName $middleName")
    }

    override fun renderFirstName(firstName: String) {
        println("rendering first name $firstName")
    }

    override fun renderUserAddress(address: UserInfoViewState.Address) {
        println("rendering address, street  ${address.street} ${address.house}")
    }

    override fun renderFriends(friends: List<UserInfoViewState>) {
        println("rendering friend list $friends")
    }

    override fun renderFriendsAndInterests(friends: List<UserInfoViewState>, interests: List<Interest>?) {
        println("rendering friend list $friends AND interest list $interests")
    }

    override fun renderPopularity(popularity: Float) {
        println("render popularity is $popularity")
    }

    override fun renderAgeAndCheesePreference(age: Int, likesCheese: Boolean) {
        println("render age and cheese: age is $age, likesCheese is $likesCheese")
    }

    override fun renderAge(age: Int) {
        println("render age: age is $age")
    }

    override fun renderHumbleness(isHumble: Boolean) {
        println("render isHumble: $isHumble")
    }

    override fun renderFavoriteShapes(favoriteShapes: List<Shape>) {
        println("render favorite shapes: $favoriteShapes")
    }

}


fun main(args: Array<String>) {
    val user1 = UserInfoViewState(
        firstName = "Dmitry",
        lastName = "Suzdalev",
        middleName = "Konstantinovich",
        address = UserInfoViewState.Address("Epronovskaya", 21),
        friends = listOf(/* too lazy to create another one here */),
        interests = listOf(Interest("Haskell", listOf("Programming language", "Functional Programming"))),
        popularity = 0.7f,
        age = 30,
        likesCheese = true,
        isIntelligent = null,
        isHumble = true,
        favoriteShapes = listOf(Shape.Square(10))
    )
    val user2 = UserInfoViewState(
        firstName = "Dmitry",
        lastName = "Suzdalev",
        middleName = "Konstantinovich",
        address = UserInfoViewState.Address("Epronovskaya", 28),
        friends = listOf(/* too lazy to create another one here */),
        interests = listOf(Interest("Kotlin", listOf("Programming language"))),
        popularity = 0.8f,
        age = 30,
        likesCheese = true,
        isIntelligent = true,
        isHumble = false,
        favoriteShapes = listOf(Shape.Rectangle(1, 2))
    )

    val dispatcher = UserInfoViewStateDiffDispatcher.Builder()
        .target(SampleRenderer())
        .build()

    dispatcher.dispatch(user2, user1)
}