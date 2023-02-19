package gui

import sfml.graphics.*

import gui.UIComponent


class Label extends UIComponent:
    var text = Text()

    def this(text: String, font: Font, characterSize: Int)=
        this()
        this.text.string = text
        this.text.font = font
        this.text.characterSize = characterSize


    override def close() =
      this.text.close()

    override def draw(target: RenderTarget, states: RenderStates)=
      val transformStates = RenderStates(states.transform.combine(this.transform))
      text.draw(target,transformStates)
