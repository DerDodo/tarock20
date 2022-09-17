import SockJS from 'sockjs-client';
import { RxStomp, RxStompConfig, RxStompState } from '@stomp/rx-stomp';
import { IMessage } from '@stomp/stompjs';
import {v4 as uuidv4} from 'uuid';
import { Observable } from 'rxjs';

class GameSocketClient {

    private rxStomp: RxStomp | null = null
    private isEstablished = false
    private sessionId = uuidv4()

    public connect(userId: string, onConnectedCallback: () => void, onErrorCallback: () => void, debug = false) {
        if (!this.isEstablished) {
            this.isEstablished = true
            const stompConfig: RxStompConfig = new RxStompConfig()
            stompConfig.webSocketFactory = () => {
                return new SockJS(window.location.protocol + '//' + window.location.host + '/api/ws/register')
            }
            stompConfig.connectHeaders = {
                login: userId,
                passcode: 'password',
            }

            if (debug) {
                stompConfig.debug = msg => console.log(msg)
            }

            this.rxStomp = new RxStomp()
            this.rxStomp.configure(stompConfig)
            this.rxStomp.activate()

            this.rxStomp.connectionState$.subscribe({
                next: (response: RxStompState) => {
                if (response === RxStompState.OPEN) {
                    onConnectedCallback()
                }},
                error: error => {
                    onErrorCallback()
                }
            })
        } else {
            if (this.isConnected()) {
                onConnectedCallback()
            } else {
                this.isEstablished = false
                this.connect(userId, onConnectedCallback, onErrorCallback, debug)
            }
        }
    }

    isConnected(): boolean {
        return !!this.rxStomp && this.rxStomp.connected()
    }

    watch(topic: string): Observable<IMessage> {
        return this.useRxStomp((rxStomp) => {
            return rxStomp.watch(topic)
        })
    }

    publish(data: any) {
        this.useRxStomp((rxStomp) => {
            rxStomp.publish(data)
        })
    }

    useRxStomp(callback: (rxStomp: RxStomp) => any) {
        if (this.rxStomp && this.isConnected()) {
            return callback(this.rxStomp)
        } else {
            throw new Error("Socketclient not connected")
        }
    }
}

let socketClient = new GameSocketClient()
export default socketClient
