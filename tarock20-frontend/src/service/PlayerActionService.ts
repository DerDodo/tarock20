import socketClient from "./GameSocketClient"

class PlayerActionService {

  public sendSync(gameId: string): void {
    this.publicNoArgs('/app/game/' + gameId + '/sync')
  }

  public startGame(gameId: string): void {
    this.publicNoArgs('/app/game/' + gameId + '/start')
  }

  private publicNoArgs(destination: string) {
    const params = {
        destination: destination
    }
    socketClient.publish(params)
  }
}

let playerActionService = new PlayerActionService()
export default playerActionService