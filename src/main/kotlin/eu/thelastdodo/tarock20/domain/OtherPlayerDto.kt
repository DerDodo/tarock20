package eu.thelastdodo.tarock20.domain

data class OtherPlayerDto(
    val id: String,
    val numHandCards: Int,
    val numWonTricks: Int,
    var points: Int,
)