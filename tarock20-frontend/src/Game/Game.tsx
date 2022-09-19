import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom"
import GameDto from "../domain/GameDto";
import socketClient from "../service/GameSocketClient";
import playerActionService from "../service/PlayerActionService";
import { ErrorDetails } from "../domain/ErrorDetails";
import { ErrorType } from "../domain/ErrorType";
import playerIdService from "../service/PlayerIdService";
import gameSocketWatcher from "../service/GameSocketWatcher";
import { GamePhase } from "../domain/GamePhase";
import GameLobby from "./GameLobby";

export default function Game() {
    let { gameId } = useParams()
    let navigate = useNavigate()
    let [game, setGame] = useState<GameDto | null>(null)
    let [isLoading, setIsLoading] = useState(true)
    let [error, setError] = useState<ErrorType | null>(null)

    useEffect(() => {
        socketClient.connect(playerIdService.getPlayerId(), () => {
            gameSocketWatcher.error("Game", gameId!, (error: ErrorDetails) => {
                setIsLoading(false)
                setError(error.type)
            })
    
            gameSocketWatcher.sync("Game", gameId!, (newGame: GameDto) => {
                setIsLoading(false)
                setGame(newGame)
                game = newGame
            })
    
            gameSocketWatcher.started("Game", gameId!, () => {
                const newGame = game!.deepCopy()
                newGame!.phase = GamePhase.Playing
                setGame(newGame)
                game = newGame
            })
    
            playerActionService.sendSync(gameId!)
        },
        () => {
            console.log("Couldn't connect to the stomp server.")
            setIsLoading(false)
            setError(ErrorType.ConnectionError)
        })
    }, [""])

    if (!gameId) {
        navigate("/")
        return null
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
            {!isLoading && game && game.phase === GamePhase.NotYetStarted &&
                <GameLobby game={game} />
            }
            {!isLoading && game && game.phase === GamePhase.Playing &&
                <p>Playing...</p>
            }
        </div>
    )
}