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
)

class Game(
    private val rand: Random,
    private val players: List<Player>,
) {
    private var currentPlayer: Player = players.first()
    private val questions = mutableListOf<Question>()
    private var gameOver: Boolean = false

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

        currentPlayer.shouldGetOutFromPenaltyBox(roll)

        if (!currentPlayer.inPenaltyBox) {
            currentPlayer.move(roll)
            askQuestion()
        }

        onQuestionAnswer()
        gameOver = currentPlayer.hasWon()
    }

    fun isGameOver(): Boolean = gameOver

    private fun isWrongAnswer() = rand.nextInt(9) == 7

    private fun rollDice(): Int = rand.nextInt(5) + 1

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

    private fun onQuestionAnswer() {
        if (isWrongAnswer()) {
            onWrongAnswer()
        } else {
            onCorrectAnswer()
        }
    }

    private fun onCorrectAnswer() {
        if (!currentPlayer.inPenaltyBox) {
            println("Answer was correct!!!!")
            currentPlayer.increasePoints()
        }
    }

    private fun onWrongAnswer() {
        println("Question was incorrectly answered")
        println(currentPlayer.name + " was sent to the penalty box")
        currentPlayer.inPenaltyBox = true
    }

    fun nextPlayer() {
        currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
    }

    private fun Player.hasWon(): Boolean = purse == 6
    private fun Player.increasePoints() = purse++.also {
        println("$name now has $purse Gold Coins.")
    }

    private fun Player.move(roll: Int) {
        place += roll

        if (place > 11) {
            place -= 12
        }

        println("$name's new location is $place")
    }

    private fun Player.shouldGetOutFromPenaltyBox(roll: Int) {
        if (inPenaltyBox) {
            if (roll % 2 != 0) {
                inPenaltyBox = false
                println("${name} is getting out of the penalty box")
            } else {
                inPenaltyBox = true
                println("${name} is not getting out of the penalty box")
            }
        }
    }

}

