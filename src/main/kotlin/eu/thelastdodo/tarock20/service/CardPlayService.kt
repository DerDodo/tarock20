package eu.thelastdodo.tarock20.service

import eu.thelastdodo.tarock20.controller.GameException
import eu.thelastdodo.tarock20.entity.Game
import eu.thelastdodo.tarock20.entity.Player
import eu.thelastdodo.tarock20.entity.enums.CardColor
import eu.thelastdodo.tarock20.entity.enums.CardType
import org.springframework.stereotype.Service

class CardNotPlayableException: GameException()
class PlayerNotActive: GameException()

@Service
class CardPlayService {
    fun playCard(game: Game, player: Player, card: CardType) {
        assertPlayerIsActive(game, player)
        assertPlayerOwnsCard(player, card)
        assertCardCanBePlayed(game, player, card)

        player.hand.remove(card)
        player.playedCard = card
    }

    fun assertPlayerIsActive(game: Game, player: Player) {
        if (game.activePlayer != player)
            throw PlayerNotActive()
    }

    fun assertPlayerOwnsCard(player: Player, card: CardType) {
        if (!player.hand.contains(card))
            throw CardNotPlayableException()
    }

    fun assertCardCanBePlayed(game: Game, player: Player, card: CardType) {
        if (player == game.firstPlayerInTurn)
            return

        val firstCard = game.firstPlayerInTurn.playedCard!!
        val playedSameColor = card.cardColor == firstCard.cardColor
        val playedTarock = card.cardColor == CardColor.Tarock
        val hasSameColor = player.hand.any { it.cardColor == card.cardColor }
        val hasTarock = player.hand.any { it.cardColor == CardColor.Tarock }

        if (hasSameColor && !playedSameColor)
            throw CardNotPlayableException()

        if (!hasSameColor && hasTarock && !playedTarock)
            throw CardNotPlayableException()
    }
}