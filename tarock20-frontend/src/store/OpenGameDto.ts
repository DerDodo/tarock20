export default class OpenGameDto {
    public id: string
    public players: string[]

    constructor(id: string, players: string[]) {
        this.id = id
        this.players = players
    }

    public isInGame(playerId: string): boolean {
        return this.players.includes(playerId)
    }
}