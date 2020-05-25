export default class ConnectionHandler {

    constructor(address) {
        this.socket = new SockJS(address);

        this.stompClient = Stomp.over(this.socket);
        this.stompClient.debug = null;

        this.connected = false;

        this.onReceiveCallbacks = [];
    }

    connect(topicName, onStompConnect) {
        let me = this;
        this.stompClient.connect({}, function (frame) {
            me.stompClient.subscribe(topicName, function (payload) {
                let jsonObject = JSON.parse(payload.body);
                me.onReceiveCallbacks.forEach(callback => callback(jsonObject));
            });

            if (onStompConnect) onStompConnect();
        });

        this.connected = true;
    }

    disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
        }

        this.connected = false;
    }

    isConnected() {
        return this.connected;
    }

    send(endPoint, jsonObject) {
        this.stompClient.send(endPoint, {}, jsonObject);
    }

    addOnReceiveCallback(callback) {
        this.onReceiveCallbacks.push(callback);
    }
}