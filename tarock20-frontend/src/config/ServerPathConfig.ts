import playerIdService from "../service/PlayerIdService";

class RestServerPathConfig {
    public get games() { return "/api/rest/games?userId=" + playerIdService.getPlayerId() }
    public join(gameId: string) { return "api/rest/game/join?gameId=" + gameId + "&userId=" + playerIdService.getPlayerId() }
}

class WsServerPathConfig {
    public get register() { return "/api/ws/register" }
    public gameSync(gameId: string) { return "/user/topic/game/" + gameId + "/sync" }
    public gameStarted(gameId: string) { return "/topic/game/" + gameId + "/started" }
    public playerJoined(gameId: string) { return "/topic/game/" + gameId + "/player-joined" }
    public error(gameId: string) { return "/user/topic/game/" + gameId + "/error" }
}

export default class ServerPathConfig {
    public static get base() { return window.location.protocol + '//' + window.location.host }

    private static _rest = new RestServerPathConfig()
    public static get rest() { return this._rest }
    
    private static _ws = new WsServerPathConfig()
    public static get ws() { return this._ws }
}