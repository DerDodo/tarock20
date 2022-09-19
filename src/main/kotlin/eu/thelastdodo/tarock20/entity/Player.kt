package eu.thelastdodo.tarock20.entity

import eu.thelastdodo.tarock20.entity.enums.CardType

class Player(val id: String) {
    var hand: MutableList<CardType> = mutableListOf()
    var wonTricks: MutableList<Trick> = mutableListOf()
    var playedCard: CardType? = null
    var points: Int = 0
}