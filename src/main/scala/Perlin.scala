package perlin

import scala.math.*

def smooth(x : Float) : Float = x * x * (3 - 2 * x)

def array_min_max(array : Array[Float], min : Boolean) : Float =
    var min_max = array(0)
    for i <- 1 to array.length - 1 do
        if min then
            if array(i) < min_max then
                min_max = array(i)
        else
            if array(i) > min_max then
                min_max = array(i)
    min_max

def seed_array(n : Int) : Array[Float] =
    var seed = Array.fill(n)(0f)
    for i <- 0 to n - 1 do
        seed(i) = scala.util.Random.nextFloat()
    seed

//this is only a one-dimensional perlin noise
def perlinNoise(size : Int, decay : Float) : Array[Float] =
    var noise = Array.fill(size)(0f)

    for f <- 0 to round((log(size)/log(2)).toFloat) do
        var n = pow(2, f).toInt
        var scale = pow(decay, f).toFloat
        var seed = seed_array(n)

        for i <- 0 to n - 1 do
            var N = (size / n).toInt

            for j <- 0 to N - 1 do
                var h = smooth(j / N)
                var y = (1 - h) * seed(i) + h * seed((i + 1) % n)
                noise(i * N + j) += y / scale
    
    var m = array_min_max(noise, true)
    noise = noise.map(x => x - m)
    var M = array_min_max(noise, false)
    noise = noise.map(x => x / M)

    noise