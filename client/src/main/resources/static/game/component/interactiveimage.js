import {isPointInsideBoundingBox} from "/game/misc/math.js";
import Image from "/game/component/image.js";

export default class InteractiveImage extends Image {
    constructor(imageSrc) {
        super(imageSrc);

        this.onImageClickedConsumer = (image) => { };
        this.onImageHoverConsumer = (image) => { };
        this.onImageDrawConsumer = (image) => { };
        this.onImageUpdateConsumer = (image) => { };

        this.visible = true;
    }

    setBoundingBox(position, size) {
        super.setBoundingBox(position, size);
    }

    setPreferredSize(preferredSize) {
        super.setPreferredSize(preferredSize);
    }

    update(deltaTime, inputHandler) {
        this.onImageUpdateConsumer(this);

        let mouseCoords = inputHandler.mouseCoords;

        let isMouseInsideBbox = false;

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
            this.onImageHoverConsumer(this);

            if (inputHandler.isMouseClicked) {
                this.onImageClickedConsumer(this);
            }
        }
    }

    draw(context) {
        if (!this.visible)
            return;

        this.onImageDrawConsumer(this);

        super.draw(context);
    }

    onImageClicked(consumer) {
        this.onImageClickedConsumer = consumer;
    }

    onImageHovered(consumer) {
        this.onImageHoverConsumer = consumer;
    }

    onImageUpdate(consumer) {
        this.onImageUpdateConsumer = consumer;
    }

    onImageDraw(consumer) {
        this.onImageDrawConsumer = consumer;
    }
}