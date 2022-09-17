import socketClient from "./GameSocketClient"

class PlayerActionService {

  public sendSync(gameId: string): void {
    const myDestination = '/app/game/' + gameId + '/sync'
    const params = {
        destination: myDestination
    }
    socketClient.publish(params)
  }
}

let playerActionService = new PlayerActionService()
export default playerActionService