package com.adaptionsoft.games.uglytrivia

import java.util.*

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
            movePlayerAndAskQuestion(roll)
        }
    }

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

    private fun movePlayerAndAskQuestion(roll: Int) {
        moveCurrentPlayer(roll)

        askQuestion()
    }

    private fun moveCurrentPlayer(roll: Int) {
        places[currentPlayerIndex] += roll

        if (places[currentPlayerIndex] > 11) {
            places[currentPlayerIndex] -= 12
        }

        println(getCurrentPlayerName()
                + "'s new location is "
                + places[currentPlayerIndex])
    }

    private fun askQuestion() {
        val currentCategory = currentCategory()
        println("The category is $currentCategory")
        if (currentCategory === "Pop")
            println(popQuestions.removeFirst())
        if (currentCategory === "Science")
            println(scienceQuestions.removeFirst())
        if (currentCategory === "Sports")
            println(sportsQuestions.removeFirst())
        if (currentCategory === "Rock")
            println(rockQuestions.removeFirst())
    }


    private fun currentCategory(): String {
        if (places[currentPlayerIndex] == 0) return "Pop"
        if (places[currentPlayerIndex] == 4) return "Pop"
        if (places[currentPlayerIndex] == 8) return "Pop"
        if (places[currentPlayerIndex] == 1) return "Science"
        if (places[currentPlayerIndex] == 5) return "Science"
        if (places[currentPlayerIndex] == 9) return "Science"
        if (places[currentPlayerIndex] == 2) return "Sports"
        if (places[currentPlayerIndex] == 6) return "Sports"
        return if (places[currentPlayerIndex] == 10) "Sports" else "Rock"
    }

    fun wasCorrectlyAnswered(): Boolean {
        if (inPenaltyBox[currentPlayerIndex]) {
            if (isGettingOutOfPenaltyBox) {
                println("Answer was correct!!!!")
                currentPlayerIndex++
                if (currentPlayerIndex == players.size) currentPlayerIndex = 0
                purses[currentPlayerIndex]++
                println(
                    getCurrentPlayerName()
                        + " now has "
                        + purses[currentPlayerIndex]
                        + " Gold Coins.")

                return didPlayerWin()
            } else {
                currentPlayerIndex++
                if (currentPlayerIndex == players.size) currentPlayerIndex = 0
                return true
            }


        } else {

            println("Answer was corrent!!!!")
            purses[currentPlayerIndex]++
            println(
                getCurrentPlayerName()
                    + " now has "
                    + purses[currentPlayerIndex]
                    + " Gold Coins.")

            val winner = didPlayerWin()
            currentPlayerIndex++
            if (currentPlayerIndex == players.size) currentPlayerIndex = 0

            return winner
        }
    }

    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(getCurrentPlayerName() + " was sent to the penalty box")
        inPenaltyBox[currentPlayerIndex] = true

        currentPlayerIndex++
        if (currentPlayerIndex == players.size) currentPlayerIndex = 0
        return true
    }

    private fun getCurrentPlayerName() = players.get(currentPlayerIndex)


    private fun didPlayerWin(): Boolean {
        return purses[currentPlayerIndex] != 6
    }
}

fun MutableList<String>.removeFirst(): String {
    return this.removeAt(0)
}
fun MutableList<String>.addLast(element: String) {
    this.add(element)
}
