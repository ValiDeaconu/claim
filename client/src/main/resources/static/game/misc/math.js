
export function isPointInsideBoundingBox(point, topLeft, bottomRight) {
    return (point.x >= topLeft.x && point.y >= topLeft.y && point.x <= bottomRight.x && point.y <= bottomRight.y);
}