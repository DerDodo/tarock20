package eu.thelastdodo.tarock20.service

import eu.thelastdodo.tarock20.controller.GameException
import eu.thelastdodo.tarock20.entity.Game
import eu.thelastdodo.tarock20.entity.Player
import eu.thelastdodo.tarock20.entity.enums.*
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.lang.Integer.min

class CardsWereHandedOut(source: Any, val game: Game): ApplicationEvent(source)

class WrongGamePhaseException : GameException()
class PlayersMissingException : GameException()
class GameNotDoneException : GameException()

data class GameScore(
    var foreHandTeamScore: Int = 0,
    var otherTeamScore: Int = 0,
) {
    fun add(team: TeamType, points: Int) {
        if (team == TeamType.ForeHand) {
            foreHandTeamScore += points
            otherTeamScore -= points
        } else {
            foreHandTeamScore -= points
            otherTeamScore += points
        }
    }
}

@Service
class GamePhaseService(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun startGame(game: Game) {
        assertCanBeStarted(game)
        game.phase = GamePhase.Playing
        startNewGame(game)
    }

    private fun assertCanBeStarted(game: Game) {
        if (game.phase != GamePhase.NotYetStarted) {
            throw WrongGamePhaseException()
        }

        if (game.players.size != Game.MAX_PLAYER_SIZE) {
            throw PlayersMissingException()
        }
    }

    private fun startNewGame(game: Game) {
        handOut(game)
        determineTeams(game)
    }

    fun handOut(game: Game) {
        val cards = CardType.values().asList().shuffled()
        for (i in 0..3) {
            game.players[i].hand.addAll(cards.subList(i * 10, i * 10 + 9))
        }
        applicationEventPublisher.publishEvent(CardsWereHandedOut(this, game))
    }

    private fun determineTeams(game: Game) {
        determineForeHandPartner(game)
        game.otherTeam = game.players.filter { it != game.foreHand && it != game.foreHandPartner }
    }

    private fun determineForeHandPartner(game: Game) {
        for (checkCard in FOREHAND_CARDS) {
            game.foreHandPartner = game.players.find { it.hand.contains(checkCard) }
            if (game.foreHandPartner != game.foreHand) {
                return
            }
        }
    }

    fun scoreGame(game: Game) {
        assertCanBeScored(game)

        val points = calcPoints(game)

        game.foreHandTeam.forEach { it.points += points.foreHandTeamScore }
        game.otherTeam.forEach { it.points -= points.otherTeamScore }
    }

    private fun assertCanBeScored(game: Game) {
        if (game.players.any { it.hand.isNotEmpty() }) {
            throw GameNotDoneException()
        }
    }

    private fun scoreTeam(team: List<Player>): Int {
        return team.sumOf { scoreHand(it) }
    }

    private fun scoreHand(player: Player): Int {
        return player.hand.sumOf { getCardValue(it.scoreType) }
    }

    private fun getCardValue(cardScoreType: CardScoreType): Int {
        return when(cardScoreType) {
            CardScoreType.Koenig -> 5
            CardScoreType.Dame -> 4
            CardScoreType.Reiter -> 3
            CardScoreType.Bube -> 2
            CardScoreType.Glatze -> 0
            CardScoreType.Tarock1 -> 1
            CardScoreType.Tarock5 -> 5
        }
    }

    fun calcPoints(game: Game): GameScore {
        val foreHandTeamScore = scoreTeam(game.foreHandTeam)
        val otherTeamScore = scoreTeam(game.otherTeam)
        val lowScore = min(foreHandTeamScore, otherTeamScore)
        val winningTeam = if (foreHandTeamScore > otherTeamScore) TeamType.ForeHand else TeamType.Other

        if (isValatAgainst(game.foreHandTeam))
            return GameScore(-6, 6)
        if (isValatAgainst(game.otherTeam))
            return GameScore(6, -6)

        val points = if (isAbsolut(lowScore)) 2 else 1

        val gameScore = GameScore()
        gameScore.add(winningTeam, points)

        val pagatUltimoTeam = wasPagatUltimo(game)
        if (pagatUltimoTeam != null) {
            gameScore.add(pagatUltimoTeam, 1)
        }

        return gameScore
    }

    private fun isValatAgainst(team: List<Player>): Boolean {
        return team.all { it.wonTricks.isEmpty() }
    }

    private fun isAbsolut(lowScore: Int): Boolean {
        return lowScore <= 32
    }

    private fun wasPagatUltimo(game: Game): TeamType? {
        val lastForeHandTrick = game.foreHandTeam.map { it.wonTricks }.flatten().maxBy { it.trickNum }
        val lastOtherTrick = game.otherTeam.map { it.wonTricks }.flatten().maxBy { it.trickNum }
        val lastTrick = if (lastForeHandTrick.trickNum > lastOtherTrick.trickNum) lastForeHandTrick else lastOtherTrick

        val pagat = lastTrick[CardType.Tarock1]
        return if (pagat != null) {
            val didPagatWin = lastTrick.cards.count { it.card.cardColor == CardColor.Tarock } == 1
            val pagatTeam = game.getTeamOf(pagat.player)
            if (didPagatWin) {
                pagatTeam
            } else {
                TeamType.getOpponent(pagatTeam)
            }
        } else {
            null
        }
    }

    companion object {
        val FOREHAND_CARDS = listOf(CardType.Tarock20, CardType.Tarock19,
            CardType.Tarock18, CardType.Tarock17, CardType.Tarock16)
    }
}