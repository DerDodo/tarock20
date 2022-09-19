package eu.thelastdodo.tarock20.domain

import eu.thelastdodo.tarock20.entity.enums.CardType

data class PlayerDto(
    val id: String,
    val hand: List<CardType>,
    val wonTricks: List<List<CardType>>,
    val playedCard: CardType?,
    val points: Int,
)