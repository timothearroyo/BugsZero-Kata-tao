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

class Game(private val rand: Random) {
    var players = mutableListOf<String>()
    var places = IntArray(6)
    var purses = IntArray(6)
    var inPenaltyBox = BooleanArray(6)

    var popQuestions = mutableListOf<String>()
    var scienceQuestions = mutableListOf<String>()
    var sportsQuestions = mutableListOf<String>()
    var rockQuestions = mutableListOf<String>()

    var currentPlayerIndex = 0
    var isGettingOutOfPenaltyBox: Boolean = false

    var gameOver: Boolean = false

    val isPlayable: Boolean
        get() = getPlayersCount() >= 2

    init {
        for (i in 0..49) {
            popQuestions.addLast("Pop Question $i")
            scienceQuestions.addLast("Science Question $i")
            sportsQuestions.addLast("Sports Question $i")
            rockQuestions.addLast("Rock Question $i")
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
        val roll = rollDice()
        val currentPlayerName = getCurrentPlayerName()
        println("$currentPlayerName is the current player")
        println("They have rolled a $roll")

        if (canCurrentPlayerMove(roll, currentPlayerName)) {
            moveCurrentPlayer(roll)
            askQuestion()
        }

        gameOver = if (isWrongAnswer()) {
            wrongAnswer()
            false
        } else {
            correctAnswer()
        }
    }

    private fun isWrongAnswer() = rand.nextInt(9) == 7

    private fun canCurrentPlayerMove(roll: Int, currentPlayerName: String): Boolean {
        val isCurrentPlayerInPenaltyBox = inPenaltyBox[currentPlayerIndex]
        if (isCurrentPlayerInPenaltyBox) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true

                println("$currentPlayerName is getting out of the penalty box")
                return true
            } else {
                println("$currentPlayerName is not getting out of the penalty box")
                isGettingOutOfPenaltyBox = false
                return false
            }
        } else {
            return true
        }
    }

    private fun rollDice(): Int {
        val roll = rand.nextInt(5) + 1
        return roll
    }

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
        when (currentCategory) {
            QuestionCategory.POP -> println(popQuestions.removeFirst())
            QuestionCategory.SCIENCE -> println(scienceQuestions.removeFirst())
            QuestionCategory.SPORTS -> println(sportsQuestions.removeFirst())
            QuestionCategory.ROCK -> println(rockQuestions.removeFirst())
        }
    }

    private fun currentCategory(): QuestionCategory =
        when (places[currentPlayerIndex]) {
            0, 4, 8 -> QuestionCategory.POP
            1, 5, 9 -> QuestionCategory.SCIENCE
            2, 6, 10 -> QuestionCategory.SPORTS
            else -> QuestionCategory.ROCK
        }

    private fun correctAnswer(): Boolean {
        if (inPenaltyBox[currentPlayerIndex]) {
            if (isGettingOutOfPenaltyBox) {
                println("Answer was correct!!!!")
                nextPlayer()
                increaseCurrentPlayerPoints()

                return hasCurrentPlayerWon()
            } else {
                nextPlayer()
                return false
            }
        } else {
            println("Answer was corrent!!!!")
            increaseCurrentPlayerPoints()

            val winner = hasCurrentPlayerWon()
            nextPlayer()

            return winner
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

    private fun wrongAnswer() {
        println("Question was incorrectly answered")
        println(getCurrentPlayerName() + " was sent to the penalty box")
        inPenaltyBox[currentPlayerIndex] = true

        nextPlayer()
    }

    private fun nextPlayer() {
        currentPlayerIndex++
        if (currentPlayerIndex == players.size) currentPlayerIndex = 0
    }

    private fun getCurrentPlayerName() = players[currentPlayerIndex]


    private fun hasCurrentPlayerWon(): Boolean {
        return purses[currentPlayerIndex] == 6
    }
}

fun MutableList<String>.removeFirst(): String {
    return this.removeAt(0)
}

fun MutableList<String>.addLast(element: String) {
    this.add(element)
}
