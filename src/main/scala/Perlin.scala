package perlin

import scala.math.*

def smooth(x : Float) : Float = x * x * (3 - 2 * x)

def seed_array(n : Int) : Array[Float] =
    var seed = Array.fill(n)(0f)
    for i <- 0 to n - 1 do
        seed(i) = scala.util.Random.nextFloat()
    seed

//this is only a one-dimensional perlin noise
def perlinNoise(size : Int, decay : Float) : Array[Float] =
    var noise = Array.fill(size)(0f)

    //for every frequency
    for f <- 0 to round((log(size)/log(2)).toFloat) do
        var n = pow(2, f).toInt
        var scale = pow(decay, f).toFloat
        var seed = seed_array(n)

        //for every point in the noise array
        for i <- 0 to n - 1 do
            var N = (size / n).toInt

            for j <- 0 to N - 1 do
                //interpolate between the two seed values
                var h = smooth(j / N)
                var y = (1 - h) * seed(i) + h * seed((i + 1) % n)
                noise(i * N + j) += y / scale
    
    var m = noise.min
    noise = noise.map(x => x - m)
    var M = noise.max
    noise = noise.map(x => x / M)

    noise