package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.uglytrivia.Game
import com.adaptionsoft.games.uglytrivia.Player
import java.util.*


object GameRunner {

    @JvmStatic
    fun main(args: Array<String>) {
        val rand = Random()
        playGame(rand)

    }

    fun playGame(rand: Random) {
        val aGame = Game(
            rand = rand,
            players = mutableListOf<Player>()
                .addPlayer("Chet")
                .addPlayer("Pat")
                .addPlayer("Sue")
        )

        do {
            aGame.playTurn()
            aGame.nextPlayer()
        } while (!aGame.gameOver)
    }

    fun MutableList<Player>.addPlayer(playerName: String): MutableList<Player>  {
        val player = Player(name = playerName, place = 0, purse = 0, inPenaltyBox = false)
        add(player)
        println("$playerName was added")
        println("They are player number $size")
        return this
    }
}
