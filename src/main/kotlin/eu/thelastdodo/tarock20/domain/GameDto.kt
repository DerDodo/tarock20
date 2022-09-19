package eu.thelastdodo.tarock20.domain

import eu.thelastdodo.tarock20.entity.enums.GamePhase

data class GameDto(
    val id: String,
    var phase: GamePhase,
    val player: PlayerDto,
    val otherPlayers: List<OtherPlayerDto>,
    var activePlayer: String,
    var foreHand: String,
)