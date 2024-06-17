package com.adaptionsoft.games.uglytrivia

import java.util.*

enum class QuestionCategory(private val value: String) {
    POP("Pop"),
    SCIENCE("Science"),
    SPORTS("Sports"),
    ROCK("Rock");

    override fun toString(): String {
        return value
    }
}

data class Question(
    val category: QuestionCategory,
    val title: String
)

class Game(private val rand: Random) {
    var players = mutableListOf<String>()
    var places = IntArray(6)
    var purses = IntArray(6)
    var inPenaltyBox = BooleanArray(6)

    val questions = mutableListOf<Question>()

    var currentPlayerIndex = 0

    var gameOver: Boolean = false

    init {

        for (i in 0..49) {
            questions.add(Question(QuestionCategory.POP, "Pop Question $i"))
            questions.add(Question(QuestionCategory.SCIENCE, "Science Question $i"))
            questions.add(Question(QuestionCategory.SPORTS, "Sports Question $i"))
            questions.add(Question(QuestionCategory.ROCK, "Rock Question $i"))
        }
    }

    fun addPlayer(playerName: String): Boolean {
        players.add(playerName)
        places[getPlayersCount()] = 0
        purses[getPlayersCount()] = 0
        inPenaltyBox[getPlayersCount()] = false

        println(playerName + " was added")
        println("They are player number " + players.size)
        return true
    }

    fun getPlayersCount(): Int = players.size

    fun playTurn() {
        val currentPlayerName = getCurrentPlayerName()
        println("$currentPlayerName is the current player")

        val roll = rollDice()
        println("They have rolled a $roll")

        shouldGetOutFromPenaltyBox(currentPlayerName, roll)

        if (!isPlayerInPenaltyBox()) {
            moveCurrentPlayer(roll)
            askQuestion()
        }

        gameOver = if (isWrongAnswer()) {
            onWrongAnswer()
            false
        } else {
            onCorrectAnswer()
        }
    }

    private fun shouldGetOutFromPenaltyBox(currentPlayerName: String, roll: Int) {
        if (isPlayerInPenaltyBox()) {
            if (roll % 2 != 0) {
                inPenaltyBox[currentPlayerIndex] = false

                println("$currentPlayerName is getting out of the penalty box")
            } else {
                println("$currentPlayerName is not getting out of the penalty box")
                inPenaltyBox[currentPlayerIndex] = true
            }
        }
    }

    private fun isWrongAnswer() = rand.nextInt(9) == 7

    private fun isPlayerInPenaltyBox(): Boolean =
        inPenaltyBox[currentPlayerIndex]

    private fun rollDice(): Int =
        rand.nextInt(5) + 1

    private fun moveCurrentPlayer(roll: Int) {
        places[currentPlayerIndex] += roll

        if (places[currentPlayerIndex] > 11) {
            places[currentPlayerIndex] -= 12
        }

        println(
            getCurrentPlayerName()
                    + "'s new location is "
                    + places[currentPlayerIndex]
        )
    }

    private fun askQuestion() {
        val currentCategory = currentCategory()
        println("The category is $currentCategory")
        val question = questions.first { it.category == currentCategory }
        println(question.title)
        questions.remove(question)
    }

    private fun currentCategory(): QuestionCategory =
        when (places[currentPlayerIndex]) {
            0, 4, 8 -> QuestionCategory.POP
            1, 5, 9 -> QuestionCategory.SCIENCE
            2, 6, 10 -> QuestionCategory.SPORTS
            else -> QuestionCategory.ROCK
        }

    private fun onCorrectAnswer(): Boolean {
        if (isPlayerInPenaltyBox()) {
            return false
        } else {
            println("Answer was correct!!!!")
            increaseCurrentPlayerPoints()
            return hasCurrentPlayerWon()
        }
    }

    private fun increaseCurrentPlayerPoints() {
        purses[currentPlayerIndex]++
        println(
            getCurrentPlayerName()
                    + " now has "
                    + purses[currentPlayerIndex]
                    + " Gold Coins."
        )
    }

    private fun onWrongAnswer() {
        println("Question was incorrectly answered")
        println(getCurrentPlayerName() + " was sent to the penalty box")
        inPenaltyBox[currentPlayerIndex] = true
    }

    fun nextPlayer() {
        currentPlayerIndex++
        if (currentPlayerIndex == players.size) currentPlayerIndex = 0
    }

    private fun getCurrentPlayerName() = players[currentPlayerIndex]

    private fun hasCurrentPlayerWon(): Boolean {
        return purses[currentPlayerIndex] == 6
    }
}
