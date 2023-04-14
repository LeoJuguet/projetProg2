package sfml.system

import sfml.system.Vector2

def norm(vector: Vector2[Float]) : Float =
    Math.sqrt(vector.x * vector.x + vector.y * vector.y).toFloat

def distance(v1: Vector2[Float], v2: Vector2[Float]) : Float =
    norm(Vector2(v1.x - v2.x, v1.y - v2.y))