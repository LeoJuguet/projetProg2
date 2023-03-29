package gui

import sfml.graphics.*
import sfml.window.*
import sfml.system.*

import gui.UIComponent
import gui.Clickable
import gui.{TextStyle, Style}
import manager.FontManager


class TextBox(
    var textLimit: Int = 100,
    var font: Font = FontManager.get("game_over.ttf"),
    var string: String = "default text",
    var style: TextStyle = TextStyle(style = Style(outlineColor = Color.White()))
) extends UIComponent with Clickable:
    var text = Text()

/** Called whenever the text is changed interactively by the user
 *
 * @param newText the newText commited
 */
    var onTextChangedBind = (newText: String) => {}
    def onTextChanged()=
        this.globalBounds = this.text.globalBounds
        this.onTextChangedBind(this.string)

/** Called whenever the text is committed.
 * This happens when the user presses enter or the text box loses focus.
 *
 * @param newText the newText commited
 */
    var onTextCommitedBind = (newText: String) => {}
    def onTextCommited()=
        this.onTextCommitedBind(this.string)

    def this(string: String)=
        this()
        this.text.font_= (this.font)
        this.text.characterSize = 100
        this.string = string
        this.text.string = string
        this.style.apply(this.text)
        this.position= Vector2(100,100)

    override def position: Vector2[Float]= this.text.position

    override def position_=(position: Vector2[Float]) =
        this.text.position = position
        this.globalBounds = this.text.globalBounds

    override def onClicked() =
        this.isFocused = true
        this.text.string = this.string + "_"
        this.onClickedBind()

    override def unFocused()=
        this.isFocused = false
        this.text.string = this.string
        this.onTextCommited()


    override def draw(target: RenderTarget, states: RenderStates)=
        val transformStates = RenderStates(states.transform.combine(this.transform))
        this.text.draw(target,transformStates)

    def typedOn(input: Event.TextEntered) : Unit=
        if(this.isFocused){
            val charTyped = input.unicode
            if(charTyped < 128){
                if(charTyped == 13 || charTyped == 27){
                    this.isFocused = false
                    this.text.string = this.string
                    onTextCommited()
                    return
                }
                if((this.textLimit < 0 || this.string.length < textLimit) && charTyped != 8){
                    this.string = this.string + charTyped.toChar
                    this.text.string = this.string + "_"
                    this.onTextChanged()
                }else if(charTyped == 8){
                    this.string = this.string.dropRight(1)
                    this.text.string = this.string + "_"
                    this.onTextChanged()
                }
            }
            this.globalBounds = this.text.globalBounds
        }
