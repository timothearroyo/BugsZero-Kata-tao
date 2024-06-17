package com.adaptionsoft.games.uglytrivia

import java.util.*

enum class QuestionCategory(
    private val value: String,
) {
    POP("Pop"),
    SCIENCE("Science"),
    SPORTS("Sports"),
    ROCK("Rock"),
    ;

    override fun toString(): String = value
}

data class Question(
    val category: QuestionCategory,
    val title: String,
)

data class Player(
    val name: String,
    var place: Int,
    var purse: Int,
    var inPenaltyBox: Boolean,
) {
    fun hasWon(): Boolean = purse == 6
}

class Game(
    private val rand: Random,
    val players: List<Player>,
) {
    var currentPlayer: Player = players.first()
    val questions = mutableListOf<Question>()
    var gameOver: Boolean = false

    init {

        for (i in 0..49) {
            questions.add(Question(QuestionCategory.POP, "Pop Question $i"))
            questions.add(Question(QuestionCategory.SCIENCE, "Science Question $i"))
            questions.add(Question(QuestionCategory.SPORTS, "Sports Question $i"))
            questions.add(Question(QuestionCategory.ROCK, "Rock Question $i"))
        }
    }

    fun playTurn() {
        println("${currentPlayer.name} is the current player")

        val roll = rollDice()
        println("They have rolled a $roll")

        shouldCurrentPlayerGetOutFromPenaltyBox(roll)

        if (!currentPlayer.inPenaltyBox) {
            moveCurrentPlayer(roll)
            askQuestion()
        }

        gameOver =
            if (isWrongAnswer()) {
                onWrongAnswer()
                false
            } else {
                onCorrectAnswer()
            }
    }

    private fun shouldCurrentPlayerGetOutFromPenaltyBox(roll: Int) {
        if (currentPlayer.inPenaltyBox) {
            if (roll % 2 != 0) {
                currentPlayer.inPenaltyBox = false
                println("${currentPlayer.name} is getting out of the penalty box")
            } else {
                currentPlayer.inPenaltyBox = true
                println("${currentPlayer.name} is not getting out of the penalty box")
            }
        }
    }

    private fun isWrongAnswer() = rand.nextInt(9) == 7

    private fun rollDice(): Int = rand.nextInt(5) + 1

    private fun moveCurrentPlayer(roll: Int) {
        currentPlayer.place += roll

        if (currentPlayer.place > 11) {
            currentPlayer.place -= 12
        }

        println(currentPlayer.name + "'s new location is " + currentPlayer.place)
    }

    private fun askQuestion() {
        val currentCategory = currentCategory()
        println("The category is $currentCategory")
        val question = questions.first { it.category == currentCategory }
        println(question.title)
        questions.remove(question)
    }

    private fun currentCategory(): QuestionCategory =
        when (currentPlayer.place) {
            0, 4, 8 -> QuestionCategory.POP
            1, 5, 9 -> QuestionCategory.SCIENCE
            2, 6, 10 -> QuestionCategory.SPORTS
            else -> QuestionCategory.ROCK
        }

    private fun onCorrectAnswer(): Boolean {
        if (currentPlayer.inPenaltyBox) {
            return false
        } else {
            println("Answer was correct!!!!")
            increaseCurrentPlayerPoints()
            return currentPlayer.hasWon()
        }
    }

    private fun increaseCurrentPlayerPoints() {
        currentPlayer.purse++
        println(
            currentPlayer.name +
                " now has " +
                currentPlayer.purse +
                " Gold Coins.",
        )
    }

    private fun onWrongAnswer() {
        println("Question was incorrectly answered")
        println(currentPlayer.name + " was sent to the penalty box")
        currentPlayer.inPenaltyBox = true
    }

    fun nextPlayer() {
        currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
    }
}
