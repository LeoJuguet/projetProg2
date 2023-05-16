package perlin

import scala.math.{pow, log, round}
import scala.util.Random

def smooth(x : Float) : Float = x * x * (3 - 2 * x)

def seed_array(n : Int) : Array[Float] = {
    var seed = Array.fill(n)(0f)
    for i <- 0 to n - 1 do
        seed(i) = Random.nextFloat()
    seed
}

//this is only a one-dimensional perlin noise
def perlinNoise(size : Int, decay : Float, startingFreq : Int) : Array[Float] = {
    var noise = Array.fill(size)(0f)

    //for every frequency
    for f <- startingFreq to round((log(size)/log(2)).toFloat) do
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
}

//2D perlin noise in scala
//This is generalizable to any dimension, but I don't need that right now so I stick to 2D
def perlin2D(size : Int, decay : Float, startingFreq : Int) : Array[Array[Float]] = {
    var noise = Array.fill(size, size)(0f)

    //for every frequency
    for f <- startingFreq to round((log(size)/log(2)).toFloat) do
        var n = pow(2, f).toInt
        var scale = pow(decay, f).toFloat
        var seed = Array.fill(n, n)(0f)
        for i <- 0 to n - 1 do
            for j <- 0 to n - 1 do
                seed(i)(j) = Random.nextFloat()

        //for every point in the noise array
        var N = (size / n).toInt
        for xi <- 0 to n - 1 do
            for xj <- 0 to N - 1 do
                for yi <- 0 to n - 1 do
                    for yj <- 0 to N - 1 do
                        //interpolate between the two seed values
                        var xh = smooth(xj / N)
                        var yh = smooth(yj / N)

                        var a = (1 - xh) * seed(yi)(xi) + xh * seed(yi)((xi+1) % n)
                        var b = (1 - xh) * seed((yi+1) % n)(xi) + xh * seed((yi+1) % n)((xi+1) % n)

                        noise(N*yi + yj)(N*xi + xj) += ((1 - yh) * a + yh * b) / scale
        
    var m = noise.flatten.min
    noise = noise.map(x => x.map(y => y - m))
    var M = noise.flatten.max
    noise = noise.map(x => x.map(y => y / M))

    noise
}

def generateField(size : Int, decay : Float, startingFreq : Int, epsilon : Float) : (Array[Array[Float]], Array[Array[Boolean]]) = {
    var noise = perlin2D(size, decay, startingFreq)
    var field = Array.fill(size, size)(false)

    field = noise.map(x => x.map(y => (y < 0.5 + epsilon) && (y > 0.5 - epsilon)))

    (noise, field)
}