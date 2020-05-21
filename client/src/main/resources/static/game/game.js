export default class Game {
    constructor(canvas, viewManager, inputHandler, currentUser) {
        this.canvas = canvas;
        this.context = canvas.getContext('2d');

        this.viewManager = viewManager;
        this.inputhandler = inputHandler;
        this.currentUser = currentUser;

        this._lastTime = 0;
    }

    resize(width, height) {
        this.canvas.width = width;
        this.canvas.height = height;

        this.viewManager.resize({ width: width, height: height });
    }

    start() {
        requestAnimationFrame((timestamp) => this._gameLoop(timestamp));
    }

    stop() {
        this.stopSignal = true;
    }

    _gameLoop(timestamp) {
        let deltaTime = timestamp - this._lastTime;
        this._lastTime = timestamp;

        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);

        // update call
        this.viewManager.getView().update(deltaTime, this.inputhandler);

        // draw call
        this.viewManager.getView().draw(this.context);

        // update inputHandler
        this.inputhandler.update();

        if (!this.stopSignal) {
            requestAnimationFrame((timestamp) => this._gameLoop(timestamp));
        }
    }
}