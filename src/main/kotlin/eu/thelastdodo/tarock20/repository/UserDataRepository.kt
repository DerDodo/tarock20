package eu.thelastdodo.tarock20.repository

import eu.thelastdodo.tarock20.entity.UserData
import org.springframework.stereotype.Service

@Service
class UserDataRepository(
    private val sessionIds: MutableMap<String, UserData>
) {
    fun put(sessionId: String, userData: UserData) {
        sessionIds[sessionId] = userData
    }

    fun get(sessionId: String): UserData? {
        return sessionIds[sessionId]
    }
}