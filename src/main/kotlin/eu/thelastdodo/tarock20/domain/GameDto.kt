package eu.thelastdodo.tarock20.domain

import eu.thelastdodo.tarock20.entity.OtherPlayerDto
import eu.thelastdodo.tarock20.entity.PlayerDto

data class GameDto(
    val id: String,
    val player: PlayerDto,
    val otherPlayers: List<OtherPlayerDto>,
)