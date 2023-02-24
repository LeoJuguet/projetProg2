package gui

import sfml.system.*
import sfml.graphics.*

enum ClickStates:
  case CLICK_IDLE, CLICK_HOVER, CLICK_PRESSED

trait Clickable:
  var onClickedBind: () => Unit = () => {}
  var onPressedBind: () => Unit = () => {}
  var onReleasedBind: () => Unit = () => {}
  var onHoveredBind: () => Unit = () => {}
  var onUnhoveredBind: () => Unit = () => {}

  var globalBounds: Rect[Float]
  var clickState = ClickStates.CLICK_IDLE

  def onClicked() =
    onClickedBind()

  def onPressed() =
    onPressedBind()

  def onReleased() =
    onReleasedBind()

  def onHovered() =
    onHoveredBind()

  def onUnhovered() =
    onUnhoveredBind()

  def unFocused() = {}

  def updateClick(mousePos: Vector2[Float], leftMouse: Boolean) =
    if (this.globalBounds.contains(mousePos.x, mousePos.y)) {
      if (leftMouse) {
        if (this.clickState != ClickStates.CLICK_PRESSED) then this.onClicked()
        this.clickState = ClickStates.CLICK_PRESSED
        this.onPressed()
      } else {
        if (this.clickState == ClickStates.CLICK_PRESSED)
          this.onReleased()
        this.clickState = ClickStates.CLICK_HOVER
        this.onHovered()
      }
    } else {
      if (leftMouse) this.unFocused()
      if (this.clickState != ClickStates.CLICK_IDLE) {
        this.clickState = ClickStates.CLICK_IDLE
        this.onUnhovered()
      }
    }
