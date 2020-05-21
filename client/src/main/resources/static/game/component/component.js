

export default class Component {

    constructor() {
        this.position = {x:0, y:0};
        this.size = {width:0, height:0};

        this.hasPreferredSize = false;
        this.preferredSize = this.size;
    }

    setBoundingBox(position, size) {
        this.position = position;
        this.size = size;

        this.__computePreferredPosition__();
    }

    setPreferredSize(preferredSize) {
        if (preferredSize === undefined || preferredSize === null) {
            this.hasPreferredSize = false;
            this.preferredSize = this.size;

            this.__preferredPosition__ = null;

            return;
        }

        this.preferredSize = preferredSize;
        this.hasPreferredSize = true;

        this.__computePreferredPosition__();
    }

    __computePreferredPosition__() {
        if (!this.hasPreferredSize)
            return;

        // compute preferred position
        let center = {
            x: this.position.x + (this.size.width / 2),
            y: this.position.y + (this.size.height / 2)
        };

        this.__preferredPosition__ = {
            x: center.x - (this.preferredSize.width / 2),
            y: center.y - (this.preferredSize.height / 2)
        };
    }

    update(deltaTime, inputHandler) {
        //
    }

    draw(context) {
        //
    }
}