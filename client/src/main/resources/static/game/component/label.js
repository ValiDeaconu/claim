import Component from "/game/component/component.js";

export const LabelAlignment = {
    LEFT: 0,
    CENTER: 1,
    RIGHT: 2
};

export default class Label extends Component {
    constructor(text = "",
                labelAlignment = LabelAlignment.CENTER) {
        super();

        this.text = text;
        this.labelAlignment = labelAlignment;

        this.fillStyle = "#000";

        this.fontSize = 16;
        this.__defaultFontSize__ = this.fontSize;

        this.font = "Roboto";
    }

    setBoundingBox(position, size) {
        super.setBoundingBox(position, size);

        this.__processResponsiveFontSize__();

        switch (this.labelAlignment) {
            case LabelAlignment.LEFT:
                if (this.hasPreferredSize) {
                    this._relativePosition = {
                        x: this.__preferredPosition__.x,
                        y: this.__preferredPosition__.y + (this.preferredSize.height / 2) + (this.fontSize / 2)
                    };
                } else {
                    this._relativePosition = {
                        x: this.position.x,
                        y: this.position.y + (this.size.height / 2) + (this.fontSize / 2)
                    };
                }
                this._textAlign = "left";
                break;

            case LabelAlignment.CENTER:
                if (this.hasPreferredSize) {
                    this._relativePosition = {
                        x: this.__preferredPosition__.x + (this.preferredSize.width / 2),
                        y: this.__preferredPosition__.y + (this.preferredSize.height / 2) + (this.fontSize / 2)
                    };
                } else {
                    this._relativePosition = {
                        x: this.position.x + (this.size.width / 2),
                        y: this.position.y + (this.size.height / 2) + (this.fontSize / 2)
                    };
                }
                this._textAlign = "center";
                break;

            case LabelAlignment.RIGHT:
                if (this.hasPreferredSize) {
                    this._relativePosition = {
                        x: this.__preferredPosition__.x + this.preferredSize.width,
                        y: this.__preferredPosition__.y + (this.preferredSize.height / 2) + (this.fontSize / 2)
                    };
                } else {
                    this._relativePosition = {
                        x: this.position.x + this.size.width,
                        y: this.position.y + (this.size.height / 2) + (this.fontSize / 2)
                    };
                }
                this._textAlign = "right";
                break;

        }
    }

    __processResponsiveFontSize__() {
        const fontOffset = 2;

        let divFactor = this.text.length < fontOffset ? this.text.length : this.text.length - fontOffset;

        let availableFontSize = Math.min(
            Math.floor(this.size.width / divFactor),
            Math.floor(this.size.height)
        );

        this.fontSize = Math.min(availableFontSize, this.__defaultFontSize__);
    }

    update(deltaTime, inputHandler) {
        //
    }

    draw(context) {
        context.font = this.fontSize + "px " + this.font;
        context.textAlign = this._textAlign;
        context.fillStyle = this.fillStyle;

        if (this.hasPreferredSize)
            context.fillText(this.text, this.__preferredPosition__.x, this.__preferredPosition__.y);
        else
            context.fillText(this.text, this._relativePosition.x, this._relativePosition.y);
    }
}