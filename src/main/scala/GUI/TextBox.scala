package gui

import sfml.graphics.*
import sfml.window.*
import sfml.system.*

import gui.UIComponent
import gui.Clickable
import gui.{TextStyle, Style}


class TextBox(
    var textLimit: Int = 100,
    var font: Font = Font(),
    var defaultText: String = "default text",
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
        this.onTextChangedBind(this.defaultText)

/** Called whenever the text is committed.
 * This happens when the user presses enter or the text box loses focus.
 *
 * @param newText the newText commited
 */
    var onTextCommitedBind = (newText: String) => {}
    def onTextCommited()=
        this.onTextCommitedBind(this.defaultText)

    def this(defaultText: String)=
        this()
        this.font.loadFromFile("src/main/resources/fonts/game_over.ttf")
        this.text.font_= (this.font)
        this.text.characterSize = 100
        this.defaultText = defaultText
        this.text.string = defaultText
        this.style.apply(this.text)
        this.position= Vector2(100,100)

    override def position: Vector2[Float]= this.text.position

    override def position_=(position: Vector2[Float]) =
        this.text.position = position
        this.globalBounds = this.text.globalBounds

    override def onClicked() =
        this.isFocused = true
        this.text.string = this.defaultText + "_"
        this.onClickedBind()

    override def unFocused()=
        this.isFocused = false
        this.text.string = this.defaultText
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
                    this.text.string = this.defaultText
                    onTextCommited()
                    return
                }
                if((this.textLimit < 0 || this.defaultText.length < textLimit) && charTyped != 8){
                    this.defaultText = this.defaultText + charTyped.toChar
                    this.text.string = this.defaultText + "_"
                    this.onTextChanged()
                }else if(charTyped == 8){
                    this.defaultText = this.defaultText.dropRight(1)
                    this.text.string = this.defaultText + "_"
                    this.onTextChanged()
                }
            }
            this.globalBounds = this.text.globalBounds
        }
