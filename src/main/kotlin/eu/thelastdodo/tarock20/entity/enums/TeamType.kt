package eu.thelastdodo.tarock20.entity.enums

enum class TeamType {
    ForeHand,
    Other;

    fun getOpponent(): TeamType {
        return getOpponent(this)
    }

    companion object {
        fun getOpponent(team: TeamType): TeamType {
            return if (team == ForeHand) Other else ForeHand
        }
    }
}