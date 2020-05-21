import Layout from "/game/layout/layout.js";

export default class SplitLayout extends Layout {
    constructor(isVerticalSplit = true,
                firstPercentage = 0.50,
                secondPercentage = 0.50) {
        super ();

        this.isVerticalSplit = isVerticalSplit;
        this.firstPercentage = firstPercentage;
        this.secondPercentage = secondPercentage;
    }

    resize(size = { width: 0, height: 0 }) {
        this.size = size;

        for (let i = 0; i < 2; ++i) {
            let currentComponent = this.components[i];
            this.setComponent(i, currentComponent);
        }
    }

    setComponent(index, component) {
        if (index < 0 || index >= 2)
            return;

        this.components[index] = component;

        if (component === undefined || component === null)
            return;

        if (this.position === null || this.size === null)
            return;

        let sz;
        switch (index) {
            case 0:
                if (this.isVerticalSplit) {
                    sz = {
                        width: this.size.width * this.firstPercentage,
                        height: this.size.height
                    };
                } else {
                    sz = {
                        width: this.size.width,
                        height: this.size.height * this.firstPercentage
                    };
                }
                this.components[index].setBoundingBox(this.position, sz);
                if (this.components[index] instanceof Layout) {
                    this.components[index].resize(sz);
                }
                break;
            case 1:
                let off;
                if (this.isVerticalSplit) {
                    off = {
                        x: this.position.x + this.size.width * this.firstPercentage,
                        y: this.position.y
                    };
                    sz = {
                        width: this.size.width * this.secondPercentage,
                        height: this.size.height
                    };
                } else {
                    off = {
                        x: this.position.x,
                        y: this.position.y + this.size.height * this.firstPercentage
                    };
                    sz = {
                        width: this.size.width,
                        height: this.size.height * this.secondPercentage
                    };
                }
                this.components[index].setBoundingBox(off, sz);
                if (this.components[index] instanceof Layout) {
                    this.components[index].resize(sz);
                }
                break;

            default:
                break;
        }
    }

    getComponent(index, component) {
        return this.components[index];
    }
}