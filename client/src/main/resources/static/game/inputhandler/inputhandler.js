import { ValueToKey } from '/game/inputhandler/keyenum.js'

export default class InputHandler {
    constructor() {
        this.keyPressed = [];
        this.mouseCoords = { x: 0, y: 0 };
        this.isMouseClicked = false;

        document.addEventListener('keydown', ev => {
            this.keyPressed[ValueToKey[ev.code]] = true;
        });

        document.onmousemove = ev => {
            this.mouseCoords.x = ev.clientX;
            this.mouseCoords.y = ev.clientY;
        }

        document.onclick = ev => {
            this.isMouseClicked = true;
        }
    }

    update() {
        this.keyPressed = [];
        this.isMouseClicked = false;
    }
}