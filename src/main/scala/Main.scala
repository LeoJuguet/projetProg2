import sfml.graphics.*
import sfml.window.*
import gamestate.*
import actor.*



@main def main =

    scala.util.Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(1024, 768), "Hello world"))
        var currentGame = GameState(window);
        val test = new Actor(currentGame)
        test.textures = "src/main/resources/sfml-logo.png"
        test.loadTexture()

        currentGame.actors_list += test

        val test2 = new Actor(currentGame)
        test2.textures = "src/main/resources/sfml-logo.png"
        test2.loadTexture()

        currentGame.actors_list += test2

        test.position=(200,100)
        test2.position=(200,50)
        test2.sprite.position=(100,50)

        test2.destroy()

        while window.isOpen() do
            for event <- window.pollEvent() do
                event match {
                    case _: Event.Closed => window.close()
                    case _ => ()
                }

            currentGame.drawGame()
    }
