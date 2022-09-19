package eu.thelastdodo.tarock20.controller.ws

import eu.thelastdodo.tarock20.domain.GameDto
import eu.thelastdodo.tarock20.entity.Game
import eu.thelastdodo.tarock20.domain.OtherPlayerDto
import eu.thelastdodo.tarock20.entity.Player
import eu.thelastdodo.tarock20.domain.PlayerDto

class GameMapper {
    companion object {
        fun gameToGameDto(game: Game, playerId: String): GameDto {
            val player = game.players.firstOrNull { it.id == playerId } ?: throw PlayerNotInGameException()
            val otherPlayers = game.players.filter { it.id != playerId }
            return GameDto(
                phase = game.phase,
                id = game.id,
                player = playerToPlayerDto(player),
                otherPlayers = otherPlayers.map { playerToOtherPlayerDto(it) },
                activePlayer = game.activePlayer.id,
                foreHand = game.foreHand.id,
            )
        }

        private fun playerToPlayerDto(player: Player): PlayerDto {
            return PlayerDto(
                player.id,
                hand = player.hand,
                wonTricks = player.wonTricks.map { t -> t.cards.map { c -> c.card } },
                playedCard = player.playedCard,
                points = player.points,
            )
        }

        fun playerToOtherPlayerDto(player: Player): OtherPlayerDto {
            return OtherPlayerDto(
                id = player.id,
                numHandCards = player.hand.size,
                numWonTricks = player.wonTricks.size,
                points = player.points)
        }
    }
}