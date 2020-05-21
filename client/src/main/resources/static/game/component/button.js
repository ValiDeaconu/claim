import Component from "/game/component/component.js";
import {isPointInsideBoundingBox} from "/game/misc/math.js";
import Label, {LabelAlignment} from "/game/component/label.js";
import {roundRect} from "/game/misc/roundrect.js";

export default class Button extends Component {
    constructor(text = "") {
        super();
        this.label = new Label(text, LabelAlignment.CENTER);
        this.label.fillStyle = "white";

        this.onButtonClickedConsumer = () => { };

        this.visible = true;

        this.style = "rgba(210, 160, 40, 1)";
        this.hoverStyle = "rgba(212, 117, 44, 1)";
        this.clickedStyle = "rgba(212, 80, 44, 1)";

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

        let isClickInsideBbox;

        if (this.hasPreferredSize) {
            isClickInsideBbox = isPointInsideBoundingBox(mouseCoords, this.__preferredPosition__, {
                x: this.__preferredPosition__.x + this.preferredSize.width,
                y: this.__preferredPosition__.y + this.preferredSize.height
            });
        } else {
            isClickInsideBbox = isPointInsideBoundingBox(mouseCoords, this.position, {
                x: this.position.x + this.size.width,
                y: this.position.y + this.size.height
            });
        }

        if (isClickInsideBbox) {
            if (inputHandler.isMouseClicked) {
                this._fillStyle = this.clickedStyle;
                this.onButtonClickedConsumer();
            } else {
                this._fillStyle = this.hoverStyle;
            }
        } else {
            this._fillStyle = this.style;
        }


        this.label.update(deltaTime, inputHandler);
    }

    draw(context) {
        if (!this.visible)
            return;

        //context.fillStyle = this._fillStyle;
        context.strokeStyle = this._fillStyle;

        if (this.hasPreferredSize)
            roundRect(context, this.__preferredPosition__.x, this.__preferredPosition__.y, this.preferredSize.width, this.preferredSize.height);
        else
            roundRect(context, this.position.x, this.position.y, this.size.width, this.size.height);

        this.label.draw(context);
    }

    onButtonClicked(consumer) {
        this.onButtonClickedConsumer = consumer;
    }
}