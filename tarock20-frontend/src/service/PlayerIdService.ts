import {v4 as uuidv4} from 'uuid';

class PlayerIdService {
    private static get LOCAL_STORAGE_PLAYER_ID_KEY():string { return "player-id" }

    private playerId: string | null = null

    public getPlayerId(): string {
        if (!this.playerId) {
            let storedPlayerId = localStorage.getItem(PlayerIdService.LOCAL_STORAGE_PLAYER_ID_KEY)
            if (storedPlayerId) {
                this.playerId = storedPlayerId
            } else {
                this.playerId = uuidv4()
                localStorage.setItem(PlayerIdService.LOCAL_STORAGE_PLAYER_ID_KEY, this.playerId)
            }
        }
        return this.playerId
    }
}

let playerIdService = new PlayerIdService()
export default playerIdService