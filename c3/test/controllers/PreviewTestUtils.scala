package controllers

import utils.pageobjects.Page


class PreviewTestUtils {

}

object PreviewTestUtils {

  def answerText(questionId:String,previewPage:Page):String = {
  	val answerId = questionId + "_value"
    previewPage.xpath(s"//td[@id='$answerId']").getText
  }

  def apply() = new PreviewTestUtils()

}


