package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.uglytrivia.Game
import java.util.*


object GameRunner {

    @JvmStatic
    fun main(args: Array<String>) {
        val rand = Random()
        playGame(rand)

    }

    fun playGame(rand: Random) {
        val aGame = Game(rand)

        aGame.addPlayer("Chet")
        aGame.addPlayer("Pat")
        aGame.addPlayer("Sue")


        do {
            aGame.playTurn()
        } while (!aGame.gameOver)
    }
}
