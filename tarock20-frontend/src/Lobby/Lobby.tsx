import { Component } from "react";
import { Table } from "react-bootstrap";
import { Navigate } from "react-router-dom";
import OpenGameDto from "../store/OpenGameDto";
import ServerPathConfig from "../config/ServerPathConfig"
import playerIdService from "../service/PlayerIdService";

type LobbyState = {
    openGames: OpenGameDto[],
    isLoading: boolean,
    isCreating: boolean,
    joiningGame: string | null,
    joinGameForward: string | null,
}

export default class Lobby extends Component<unknown, LobbyState> {
    private updateHandler: number | null = null

    constructor(props: unknown) {
        super(props)
        
        this.loadOpenGames = this.loadOpenGames.bind(this)
        this.updateOpenGames = this.updateOpenGames.bind(this)
        this.createNewGame = this.createNewGame.bind(this)
        this.joinGame = this.joinGame.bind(this)
        this.openGame = this.openGame.bind(this)

        this.state = {
            openGames: [],
            isLoading: false,
            isCreating: false,
            joiningGame: null,
            joinGameForward: null,
        }
    }

    setOpenGames(openGames: OpenGameDto[]) {
        this.setState({
            ...this.state,
            openGames: openGames,
            isLoading: false,
        })
    }

    setIsLoading(isLoading: boolean) {
        this.setState({
            ...this.state,
            isLoading: isLoading,
        })
    }

    setIsCreating(isCreating: boolean) {
        this.setState({
            ...this.state,
            isCreating: isCreating,
        })
    }

    setJoiningGame(gameId: string | null) {
        this.setState({
            ...this.state,
            joiningGame: gameId,
        })
    }

    setJoinGameForward(gameId: string | null) {
        this.setState({
            ...this.state,
            joinGameForward: gameId,
        })
    }
    
    componentDidMount() {
        this.loadOpenGames()
    }
    
    componentWillUnmount() {
        this.cleanupUpdateHandler()
    }

    cleanupUpdateHandler() {
        if (this.updateHandler) {
            window.clearTimeout(this.updateHandler)
            this.updateHandler = null
        }
    }

    loadOpenGames() {
        this.cleanupUpdateHandler()
        this.setIsLoading(true)
        fetch(ServerPathConfig.rest.games)
        .then(response =>{
            this.cleanupUpdateHandler()
            this.updateHandler = window.setTimeout(this.loadOpenGames, 5000)
            if (response.status !== 200) {
                this.setIsLoading(false)
                return Promise.reject()
            }
            return response.json()
        })
        .then(this.updateOpenGames)
    }

    updateOpenGames(openGames: any[]) {
        this.setOpenGames(openGames.map(game => new OpenGameDto(game.id, game.players)))
    }

    render() {
        return (
            <div id="Lobby">
                { this.renderCreateButton() }
                { this.renderLoadingButton() }
                { this.renderNavigateForward() }
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Players</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        { this.renderOpenGames() }
                    </tbody>
                </Table>
            </div>
        )
    }

    renderCreateButton() {
        if (this.state.isCreating) {
            return (
                <button disabled={true}>Creating...</button>
            )
        } else {
            return (
                <button onClick={this.createNewGame}>Create</button>
            )
        }
    }

    createNewGame() {
        this.setIsCreating(true)
        fetch(ServerPathConfig.rest.games, { method: 'POST' })
        .then(response =>{
            this.setIsCreating(false)
            if (response.status !== 200) {
                return Promise.reject()
            } else {
                return response.text()
            }
        })
        .then((id: string) => this.openGame(id))
    }

    renderLoadingButton() {
        if (this.state.isLoading) {
            return (
                <button disabled={true}>Loading...</button>
            )
        } else {
            return (
                <button onClick={this.loadOpenGames}>Load</button>
            )
        }
    }

    renderOpenGames() {
        return this.state.openGames.map(game => (
            <tr key={ game.id }>
                <td>{ game.id }</td>
                <td>{ game.players.length } players</td>
                <td>{ this.renderJoinButton(game) }</td>
            </tr>
        ))
    }

    renderJoinButton(game: OpenGameDto) {
        if (this.state.joiningGame === game.id) {
            return (
                <button disabled={true}>Joining...</button>
            )
        } else if (game.isInGame(playerIdService.getPlayerId())) {
            return (
                <button disabled={this.state.joiningGame !== null} onClick={e => this.openGame(game.id)}>Rejoin</button>
            )
        } else {
            return (
                <button disabled={this.state.joiningGame !== null} onClick={e => this.joinGame(game.id)}>Join</button>
            )
        }
    }

    joinGame(id: string) {
        this.setJoiningGame(id)
        fetch(ServerPathConfig.rest.join(id), { method: 'POST' })
        .then(response =>{
            this.setJoiningGame(null)
            if (response.status !== 200) {
                return Promise.reject()
            } else {
                return response.text()
            }
        })
        .then(() => this.openGame(id))
    }

    openGame(id: string) {
        this.setJoinGameForward(id)
    }

    renderNavigateForward() {
        let { joinGameForward } = this.state
        if (joinGameForward) {
            return (<Navigate to={`/game/${joinGameForward}`} replace={true} />)
        } else {
            return null
        }
    }
}