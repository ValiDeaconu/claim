import Layout from "/game/layout/layout.js";

const HeaderPanelHeightPercentage = 0.10;
const CenterPanelHeightPercentage = 0.85;
const FooterPanelHeightPercentage = 0.05;

export default class BorderLayout extends Layout {
    constructor() {
        super();
    }

    resize(size = { width: 0, height: 0 }) {
        this.size = size;

        for (let i = 0; i < 3; ++i) {
            let currentComponent = this.components[i];
            this.setComponent(i, currentComponent);
        }
    }

    setComponent(index, component) {
        if (index < 0 || index >= 3)
            return;

        this.components[index] = component;

        if (component === undefined || component === null)
            return;

        let sz;
        switch (index) {
            case 0:
                sz = {
                    width: this.size.width,
                    height: this.size.height * HeaderPanelHeightPercentage
                };
                this.components[index].setBoundingBox(this.position, sz);
                if (this.components[index] instanceof Layout) {
                    this.components[index].resize(sz);
                }
                break;

            case 1:
                sz = {
                    width: this.size.width,
                    height: this.size.height * CenterPanelHeightPercentage
                };
                this.components[index].setBoundingBox({
                    x: this.position.x,
                    y: this.position.y + this.size.height * HeaderPanelHeightPercentage
                }, sz);
                if (this.components[index] instanceof Layout) {
                    this.components[index].resize(sz);
                }
                break;

            case 2:
                sz = {
                    width: this.size.width,
                    height: this.size.height * FooterPanelHeightPercentage
                };
                this.components[index].setBoundingBox({
                    x: this.position.x,
                    y: this.position.y + this.size.height * (HeaderPanelHeightPercentage + CenterPanelHeightPercentage)
                }, sz);
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