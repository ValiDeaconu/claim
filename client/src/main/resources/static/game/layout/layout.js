export default class Layout {
    constructor() {
        this.position = {x:0, y:0};
        this.size = {width:0, height:0};
        this.components = [];

        this.backgroundColor = "rgba(255, 255, 255, 0)";
        this.strokeColor = "rgba(255, 255, 255, 0)";
    }

    setBoundingBox(position, size) {
        this.position = position;
        this.size = size;
        this.resize(size);
    }

    forEach(consumer) {
        this.components.forEach(c => {
            if (c === undefined || c === null)
                return;

            consumer(c);
        });
    }

    resize(size = { width: 0, height: 0 }) {
        this.size = size;
    }

    setComponent(index, component) {
        this.components[index] = component;
    }

    getComponent(index, component) {
        return this.components[index];
    }

    update(deltaTime, inputHandler) {
        this.forEach(c => c.update(deltaTime, inputHandler));
    }

    draw(context) {
        context.strokeStyle = this.strokeColor;
        context.fillStyle = this.backgroundColor;
        context.fillRect(this.position.x, this.position.y, this.size.width, this.size.height);

        this.forEach(c => c.draw(context));
    }
}