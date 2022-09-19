package eu.thelastdodo.tarock20.entity

import eu.thelastdodo.tarock20.entity.enums.CardType

data class TrickCard(
    val card: CardType,
    val player: Player,
)

data class Trick(
    val trickNum: Int,
    val cards: List<TrickCard>,
) {
    operator fun get(card: CardType): TrickCard? {
        return cards.find { it.card == card }
    }
}