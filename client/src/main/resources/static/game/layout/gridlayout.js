import Layout from "/game/layout/layout.js";

class Cell {
    constructor() {
        this.position = { x: 0, y: 0 };
        this.size = { width: 0, height: 0 };
        this.component = null;
    }
}

export default class GridLayout extends Layout {

    constructor(gridDimension = { rows: 0, cols: 0 }) {
        super();

        this.dimension = gridDimension;

        this.components = new Array(this.dimension.rows)
            .fill(null)
            .map(() => new Array(this.dimension.cols).fill(null));

        for (let i = 0; i < this.dimension.rows; ++i) {
            for (let j = 0; j < this.dimension.cols; ++j) {
                this.components[i][j] = new Cell();
            }
        }
    }

    resize(size = {width: 0, height: 0 }) {
        this.size = size;

        const cellWidth = size.width / this.dimension.cols;
        const cellHeight = size.height / this.dimension.rows;

        const cellSize = {
            width: cellWidth,
            height: cellHeight
        }

        for (let i = 0; i < this.dimension.rows; ++i) {
            for (let j = 0; j < this.dimension.cols; ++j) {
                this.components[i][j].position = {
                    x: j * cellWidth + this.position.x,
                    y: i * cellHeight + this.position.y
                };

                this.components[i][j].size = cellSize;

                let currentComponent = this.components[i][j].component;
                this.setComponent({row: i, col: j}, currentComponent);
            }
        }
    }

    forEach(consumer) {
        for (let i = 0; i < this.dimension.rows; ++i) {
            for (let j = 0; j < this.dimension.cols; ++j) {
                if (this.components[i][j].component === undefined || this.components[i][j].component === null)
                    continue;

                consumer(this.components[i][j].component);
            }
        }
    }

    setComponent(index = { row: 0, col: 0 }, component) {
        if (index.row < 0 || index.row >= this.rows || index.col < 0 || index.col >= this.cols)
            return;

        this.components[index.row][index.col].component = component;

        if (component === undefined || component === null)
            return;

        this.components[index.row][index.col].component = component;
        this.components[index.row][index.col].component.setBoundingBox(
            this.components[index.row][index.col].position,
            this.components[index.row][index.col].size
        );
        if (this.components[index.row][index.col].component instanceof Layout) {
            this.components[index.row][index.col].component.resize(this.components[index.row][index.col].size);
        }
    }

    getComponent(index = { row: 0, col: 0 }) {
        return this.grid[index.row][index.col].component;
    }
}