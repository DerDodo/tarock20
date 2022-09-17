package eu.thelastdodo.tarock20.controller.ws

import eu.thelastdodo.tarock20.domain.GameDto
import eu.thelastdodo.tarock20.entity.Game
import eu.thelastdodo.tarock20.entity.OtherPlayerDto
import eu.thelastdodo.tarock20.entity.Player
import eu.thelastdodo.tarock20.entity.PlayerDto

class GameMapper {
    companion object {
        fun gameToGameDto(entity: Game, playerId: String): GameDto {
            val player = entity.players.firstOrNull { it.id == playerId } ?: throw PlayerNotInGameException()
            val otherPlayers = entity.players.filter { it.id != playerId }
            return GameDto(
                entity.id,
                playerToPlayerDto(player),
                otherPlayers.map { playerToOtherPlayerDto(it) }
            )
        }

        fun playerToPlayerDto(entity: Player): PlayerDto {
            return PlayerDto(entity.id)
        }

        fun playerToOtherPlayerDto(entity: Player): OtherPlayerDto {
            return OtherPlayerDto(entity.id)
        }
    }
}