import Component from "/game/component/component.js";
import {downScaleImage} from "/game/misc/imagescale.js";

export default class Image extends Component {
    constructor(imageSrc) {
        super();

        this.setImage(imageSrc);
    }

    setImage(imageSrc) {
        this.imageSrc = imageSrc;
        this.scaledImage = imageSrc;
        this.originalSize = { width: this.imageSrc.naturalWidth, height: this.imageSrc.naturalHeight }
    }

    setBoundingBox(position, size) {
        super.setBoundingBox(position, size);

        const imageRatio = this.imageSrc.naturalWidth / this.imageSrc.naturalHeight;
        // (w/h) = imageRatio => h = (w/imageRatio); w = h*imageRatio

        if (this.hasPreferredSize) {
            if (this.imageSrc.naturalWidth > this.imageSrc.naturalHeight) {
                this.__relativeSize__ = {
                    width: this.preferredSize.width,
                    height: this.preferredSize.width / imageRatio
                }
            } else {
                this.__relativeSize__ = {
                    width: this.preferredSize.height * imageRatio,
                    height: this.preferredSize.height
                }
            }
        } else {
            if (this.imageSrc.naturalWidth > this.imageSrc.naturalHeight) {
                this.__relativeSize__ = {
                    width: this.size.width,
                    height: this.size.width / imageRatio
                }
            } else {
                this.__relativeSize__ = {
                    width: this.size.height * imageRatio,
                    height: this.size.height
                }
            }
        }

        //const scale = this.__relativeSize__.width / this.originalSize.width;
        //if (scale <= 0 || scale >= 1) {
            this.scaledImage = this.imageSrc;
        //} else {
        //    this.scaledImage = downScaleImage(this.imageSrc, scale);
        //}

        this.__computeCenteredPosition__();
    }

    __computeCenteredPosition__() {
        let center = {
            x: this.position.x + this.size.width / 2,
            y: this.position.y + this.size.height / 2
        };

        this.__centeredPosition__ = {
            x: center.x - (this.__relativeSize__.width / 2),
            y: center.y - (this.__relativeSize__.height / 2)
        };
    }

    update(deltaTime, inputHandler) {
        //
    }

    draw(context) {
        if (this.scaledImage === null || this.scaledImage === undefined)
            return;

        if (this.hasPreferredSize) {
            context.drawImage(this.scaledImage,
                this.__centeredPosition__.x, this.__centeredPosition__.y,
                this.__relativeSize__.width, this.__relativeSize__.height);
        } else {
            context.drawImage(this.scaledImage,
                this.__centeredPosition__.x, this.__centeredPosition__.y,
                this.__relativeSize__.width, this.__relativeSize__.height);
        }
    }
}