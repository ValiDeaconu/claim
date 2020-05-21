import Component from "/game/component/component.js";
import {isPointInsideBoundingBox} from "/game/misc/math.js";
import Label, {LabelAlignment} from "/game/component/label.js";
import {KeyToValue} from "/game/inputhandler/keyenum.js";

export default class TextField extends Component {
    constructor() {
        super();
        this.label = new Label("", LabelAlignment.CENTER);
        this.label.fillStyle = "black";

        this.style = "rgba(128, 128, 128, 1)";
        this.hoverStyle = "rgba(200, 200, 200, 1)";
        this.focusStyle = "rgba(255, 255, 255, 1)";

        this.hasFocus = false;

        this._fillStyle = this.style;
    }

    setBoundingBox(position, size) {
        super.setBoundingBox(position, size);
        this.label.setBoundingBox(position, size);
    }

    setPreferredSize(preferredSize) {
        super.setPreferredSize(preferredSize);
        this.label.setPreferredSize(preferredSize);
    }

    update(deltaTime, inputHandler) {
        let mouseCoords = inputHandler.mouseCoords;

        if (this.hasFocus) {
            this._fillStyle = this.focusStyle;

            if (inputHandler.isMouseClicked) {
                this.hasFocus = false;
            }
        } else {
            let isMouseInsideBbox;

            if (this.hasPreferredSize) {
                isMouseInsideBbox = isPointInsideBoundingBox(mouseCoords, this.__preferredPosition__, {
                    x: this.__preferredPosition__.x + this.preferredSize.width,
                    y: this.__preferredPosition__.y + this.preferredSize.height
                });
            } else {
                isMouseInsideBbox = isPointInsideBoundingBox(mouseCoords, this.position, {
                    x: this.position.x + this.size.width,
                    y: this.position.y + this.size.height
                });
            }

            if (isMouseInsideBbox) {
                this._fillStyle = this.hoverStyle;

                if (inputHandler.isMouseClicked) {
                    this.hasFocus = true;
                }
            } else {
                this._fillStyle = this.style;
            }
        }

        if (this.hasFocus) {
            if (this.label.text.length < 4) {
                if (inputHandler.keyPressed[KeyToValue.KEY_A])
                    this.label.text += 'A';
                if (inputHandler.keyPressed[KeyToValue.KEY_B])
                    this.label.text += 'B';
                if (inputHandler.keyPressed[KeyToValue.KEY_C])
                    this.label.text += 'C';
                if (inputHandler.keyPressed[KeyToValue.KEY_D])
                    this.label.text += 'D';
                if (inputHandler.keyPressed[KeyToValue.KEY_E])
                    this.label.text += 'E';
                if (inputHandler.keyPressed[KeyToValue.KEY_F])
                    this.label.text += 'F';
                if (inputHandler.keyPressed[KeyToValue.KEY_G])
                    this.label.text += 'G';
                if (inputHandler.keyPressed[KeyToValue.KEY_H])
                    this.label.text += 'H';
                if (inputHandler.keyPressed[KeyToValue.KEY_I])
                    this.label.text += 'I';
                if (inputHandler.keyPressed[KeyToValue.KEY_J])
                    this.label.text += 'J';
                if (inputHandler.keyPressed[KeyToValue.KEY_K])
                    this.label.text += 'K';
                if (inputHandler.keyPressed[KeyToValue.KEY_L])
                    this.label.text += 'L';
                if (inputHandler.keyPressed[KeyToValue.KEY_M])
                    this.label.text += 'M';
                if (inputHandler.keyPressed[KeyToValue.KEY_N])
                    this.label.text += 'N';
                if (inputHandler.keyPressed[KeyToValue.KEY_O])
                    this.label.text += 'O';
                if (inputHandler.keyPressed[KeyToValue.KEY_P])
                    this.label.text += 'P';
                if (inputHandler.keyPressed[KeyToValue.KEY_Q])
                    this.label.text += 'Q';
                if (inputHandler.keyPressed[KeyToValue.KEY_R])
                    this.label.text += 'R';
                if (inputHandler.keyPressed[KeyToValue.KEY_S])
                    this.label.text += 'S';
                if (inputHandler.keyPressed[KeyToValue.KEY_T])
                    this.label.text += 'T';
                if (inputHandler.keyPressed[KeyToValue.KEY_U])
                    this.label.text += 'U';
                if (inputHandler.keyPressed[KeyToValue.KEY_V])
                    this.label.text += 'V';
                if (inputHandler.keyPressed[KeyToValue.KEY_W])
                    this.label.text += 'W';
                if (inputHandler.keyPressed[KeyToValue.KEY_X])
                    this.label.text += 'X';
                if (inputHandler.keyPressed[KeyToValue.KEY_Y])
                    this.label.text += 'Y';
                if (inputHandler.keyPressed[KeyToValue.KEY_Z])
                    this.label.text += 'Z';
            }

            if (this.label.text.length > 0) {
                if (inputHandler.keyPressed[KeyToValue.BACKSPACE])
                    this.label.text = this.label.text.substr(0, this.label.text.length - 1);
            }

            if (inputHandler.keyPressed[KeyToValue.ENTER] || inputHandler.keyPressed[KeyToValue.ESCAPE]) {
                this.hasFocus = false;
            }
        }

        this.label.update(deltaTime, inputHandler);
    }

    draw(context) {
        context.fillStyle = this._fillStyle;

        if (this.hasPreferredSize)
            context.fillRect(this.__preferredPosition__.x, this.__preferredPosition__.y, this.preferredSize.width, this.preferredSize.height);
        else
            context.fillRect(this.position.x, this.position.y, this.size.width, this.size.height);

        this.label.draw(context);
    }
}