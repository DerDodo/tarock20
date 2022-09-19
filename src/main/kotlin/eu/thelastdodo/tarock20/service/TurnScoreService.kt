package eu.thelastdodo.tarock20.service

import eu.thelastdodo.tarock20.controller.GameException
import eu.thelastdodo.tarock20.entity.Game
import eu.thelastdodo.tarock20.entity.Player
import eu.thelastdodo.tarock20.entity.Trick
import eu.thelastdodo.tarock20.entity.TrickCard
import eu.thelastdodo.tarock20.entity.enums.CardColor
import org.springframework.stereotype.Service

class CardsMissingException: GameException()

@Service
class TurnScoreService {
    fun scoreTurn(game: Game) {
        assertEveryOnePlayed(game)
        val wasAnyTarockPlayed = game.players.any { it.playedCard!!.cardColor == CardColor.Tarock }
        val trickColor = game.firstPlayerInTurn.playedCard!!.cardColor
        val scoreColor = if (wasAnyTarockPlayed) CardColor.Tarock else trickColor
        val winningPlayer = getPlayerWithHighestValue(game, scoreColor)
        scoreTurnFor(game, winningPlayer)
    }

    fun getPlayerWithHighestValue(game: Game, cardColor: CardColor): Player {
        return game.players
            .filter { it.playedCard!!.cardColor == cardColor }
            .maxBy { it.playedCard!!.trickValue }
    }

    fun scoreTurnFor(game: Game, winningPlayer: Player) {
        val trickCards = game.players.map { TrickCard(it.playedCard!!, it) }

        val trickNum = game.players.map { it.wonTricks.map { t -> t.trickNum } }.flatten().size
        winningPlayer.wonTricks.add(Trick(trickNum, trickCards))

        game.players.forEach { it.playedCard = null }
        game.firstPlayerInTurn = winningPlayer
        game.activePlayer = winningPlayer
    }

    fun assertEveryOnePlayed(game: Game) {
        if (game.players.any { it.playedCard == null }) {
            throw CardsMissingException()
        }
    }
}