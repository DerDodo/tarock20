import { useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom"
import GameDto from "../store/GameDto";
import socketClient from "../service/GameSocketClient";
import { map } from "rxjs";
import playerActionService from "../service/PlayerActionService";
import { ErrorDetails } from "../store/ErrorDetails";
import { ErrorType } from "../store/ErrorType";
import playerIdService from "../service/PlayerIdService";
import ServerPathConfig from "../config/ServerPathConfig";

export default function Game() {
    let { gameId } = useParams()
    let navigate = useNavigate()
    const [game, setGame] = useState<GameDto | null>(null)
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState<ErrorType | null>(null)

    if (!gameId) {
        navigate("/")
        return null
    }

    if (!game && !isLoading && !error) {
        setIsLoading(true)
        socketClient.connect(playerIdService.getPlayerId(), () => {
            if (!gameId) {
                navigate("/")
                return null
            }

            socketClient.watch(ServerPathConfig.ws.gameUpdate(gameId))
                .pipe(map(msg => GameDto.fromJson(msg.body)))
                .subscribe({
                    next: (game) => {
                        setIsLoading(false)
                        setGame(game)
                    }
                })
                

            socketClient.watch(ServerPathConfig.ws.error(gameId))
                .pipe(map(msg => JSON.parse(msg.body)))
                .subscribe({
                    next: (error: ErrorDetails) => {
                        setIsLoading(false)
                        setError(error.type)
                    }
                })

            playerActionService.sendSync(gameId)
        },
        () => {
            console.log("Couldn't connect to the stomp server.")
            setIsLoading(false)
            setError(ErrorType.ConnectionError)
        })
    }

    return (
        <div id="Game">
            <p><Link to="/">Back to Lobby</Link></p>
            <p>Game: { gameId }</p>
            {isLoading && <p>Loading...</p>}
            {error &&
                <div>
                    <p>Couldn't load the game! ({ error })</p>
                </div>
            }
            {!isLoading && game && 
                <p>{game.otherPlayers.length + 1} players</p>
            }
        </div>
    )
}