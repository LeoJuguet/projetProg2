package actor

import sfml.graphics.*
import sfml.Resource
import sfml.window.*
import sfml.system.*

import gui.*

import gamestate.*



enum States:
    case IDLE, HOVER, PRESSED


trait Clickable
{
    var sprite: Sprite = new Sprite(Texture())
    var state = States.IDLE

    // Called whenever the button is click by the user
    var onClickedBind: () => Unit = () => {}

    // Called whenever the button is pressed by the user
    var onPressedBind: () => Unit = () => {}

    // Called whenever the button is released by the user
    var onReleasedBind: () => Unit = () => {}

    // Called whenever the button is hovered by the user
    var onHoveredBind: () => Unit  = () => {}

    // Called whenever the button was hovered by the user the frame before and it's not in this frame
    var onUnhoveredBind : () => Unit = () => {}


    var idleColor: Color = Color.White()
    var hoverColor: Color = Color.Green()
    var pressedColor: Color = Color.Red()

    def isPressed: Boolean =
        return this.state == States.PRESSED

    def update(mousePos : Vector2[Float], leftMouse: Boolean, rightMouse : Boolean) =
        if(this.sprite.globalBounds.contains(mousePos.x, mousePos.y)){
            if(leftMouse){
                if(this.state != States.PRESSED) then
                    this.onClicked()
                this.state = States.PRESSED
            }else {
                if (rightMouse && this.state == States.PRESSED)
                    this.onReleased()
                    this.state = States.HOVER
                if this.state == States.IDLE then
                    this.state = States.HOVER
            }
        }else{
            if(this.state == States.HOVER)
                this.onUnhovered()
                this.state = States.IDLE

            if (leftMouse && this.state == States.PRESSED)
                this.onReleased()
                this.state = States.IDLE
        }

        state match
            case States.IDLE => {
                this.sprite.color= this.idleColor
            }
            case States.HOVER => {
                this.onHovered()
            }
            case States.PRESSED => {
                this.onPressed()
            }

    def onPressed()=
        this.sprite.color= this.pressedColor
        this.onPressedBind()

    def onClicked()=
        this.onClickedBind()

    def onHovered()=
        this.sprite.color= this.hoverColor
        this.onHoveredBind()

    def onReleased()=
        this.sprite.color= this.hoverColor
        this.onReleasedBind()

    def onUnhovered()=
        this.sprite.color= this.idleColor
        this.onUnhoveredBind()

}


/** Actor
 *
 * @constructor crate a new Actor
 * @param gameState the game state who draw this actor
 */
class Actor(var gameState :GameState) extends Transformable with Drawable with Resource with Clickable :
    var textures: String = "src/main/resources/sfml-logo.png"

    def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(states.blendMode, this.transform)
        target.draw(sprite, render_states)

    override def close() =
      sprite.close()

/** Load the textures save in textures
 */
    def loadTexture() =
        val texture = Texture()
        texture.loadFromFile(textures)
        sprite = Sprite(texture)

    def destroy() =
      // code pour supprimer l'actor
      gameState.actors_list -= this
      this.close

end Actor