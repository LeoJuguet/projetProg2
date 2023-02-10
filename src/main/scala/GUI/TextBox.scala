package gui

import sfml.graphics.*
import sfml.window.*
import sfml.system.*

import gui.UIComponent



class TextBox extends UIComponent:
    var text = Text()
    var textLimit = 100
    var defaultText = "default text"

    var font = Font()

    var onClickedBind: () => Unit = () => {}
/** Called whenever the text is changed interactively by the user
 *
 * @param newText the newText commited
 */
    var onTextChangedBind = (newText: String) => {}
    def onTextChanged()=
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
        this.text.color = Color.Red()
        this.text.position= Vector2(100,100)

    def onClicked() =
        this.isFocused = true
        this.onClickedBind()

    override def render(target: RenderTarget)=
        target.draw(this.text)

    def typedOn(input: Event.TextEntered)=
        if(this.isFocused){
            val charTyped = input.unicode
            if(charTyped < 128){
                if(this.textLimit >= 0){
                    if(this.defaultText.length < textLimit && charTyped != 8){
                        this.defaultText = this.defaultText + charTyped.toChar
                        this.text.string = this.defaultText + "_"
                        this.onTextChanged()
                    }else if(charTyped == 8){
                        this.defaultText = this.defaultText.dropRight(1)
                        this.text.string = this.defaultText + "_"
                        this.onTextChanged()
                    }
                }
            }
        }
