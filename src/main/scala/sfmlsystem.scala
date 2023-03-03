package sfml.system

import sfml.system.Vector2

def norm(vector: Vector2[Float]) : Float =
    Math.sqrt(vector.x * vector.x + vector.y * vector.y).toFloat