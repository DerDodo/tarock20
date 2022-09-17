import OtherPlayerDto from "./OtherPlayerDto"
import PlayerDto from "./PlayerDto"

export default class GameDto {
    public id: string = ""
    public player: PlayerDto | null = null
    public otherPlayers: OtherPlayerDto[] = []

    public isInGame(playerId: string): boolean {
        return !!this.otherPlayers.find((player) => player.id === playerId) || this.player?.id === playerId
    }

    public static fromJson(json: string): GameDto {
        return Object.assign(new GameDto(), JSON.parse(json))
    }
}