import Layout from "/game/layout/layout.js";

export default class MarginLayout extends Layout {
    constructor(margin = { top: 0.25, left: 0.25, bottom: 0.25, right: 0.25 }) {
        super ();
        this.margin = margin;
    }

    resize(size = { width: 0, height: 0 }) {
        this.size = size;

        let currentComponent = this.components[0];
        this.setComponent(0, currentComponent);
    }

    setComponent(index, component) {
        if (index !== 0)
            return;

        this.components[index] = component;

        if (component === undefined || component === null)
            return;

        let sz = {
            width: this.size.width - this.margin.left * this.size.width - this.margin.right * this.size.width,
            height: this.size.height - this.margin.top * this.size.height - this.margin.bottom * this.size.height
        };

        let offset = {
            x: this.position.x + this.margin.left * this.size.width,
            y: this.position.y + this.margin.top * this.size.height
        };

        this.components[index].setBoundingBox(offset, sz);
        if (this.components[index] instanceof Layout) {
            this.components[index].resize(sz);
        }
    }

    getComponent(index, component) {
        return this.components[index];
    }
}