package eu.thelastdodo.tarock20.config

class RequestMappingPath private constructor() {
    companion object {
        private const val API_BASE_PATH = "/api"

        // REST Path Configs
        private const val REST_BASE_PATH = "$API_BASE_PATH/rest"

        const val GAMES_BASE_PATH = "$REST_BASE_PATH/games"
        const val JOIN_GAME = "$REST_BASE_PATH/game/join"

        // WS Path Configs
        private const val WS_BASE_PATH = "$API_BASE_PATH/ws"

        const val WS_REGISTRY_PATH = "$WS_BASE_PATH/register"

        const val WS_PRIVATE_BASE_PATH = "/user"
        const val WS_PUBLIC_BASE_PATH = "/topic"
        const val WS_INCOMING_BASE_PATH = "/app"

        private const val WS_GAME_BASE_PATH = "/game/{gameId}"

        fun wsOutGameSync(gameId: String): String {
            return createGameChannel(gameId, "sync")
        }

        fun wsOutGameStarted(gameId: String): String {
            return createGameChannel(gameId, "started")
        }

        fun wsOutGameHandoutCards(gameId: String): String {
            return createGameChannel(gameId, "handout-cards")
        }

        fun wsOutGamePlayerJoined(gameId: String): String {
            return createGameChannel(gameId, "player-joined")
        }

        fun wsOutError(gameId: String): String {
            return createGameChannel(gameId, "error")
        }

        private fun createGameChannel(gameId: String, channel: String): String {
            return "$WS_PUBLIC_BASE_PATH/game/$gameId/$channel"
        }

        const val WS_IN_SYNC = "$WS_GAME_BASE_PATH/sync"
        const val WS_IN_START = "$WS_GAME_BASE_PATH/start"
    }
}