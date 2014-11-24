package controllers

import utils.pageobjects.Page


class PreviewTestUtils {

}

object PreviewTestUtils {

  def answerText(questionId:String,previewPage:Page):String = {
    previewPage.xpath(s"//dt[./a[@id='$questionId']]/following-sibling::dd").getText
  }

  def apply() = new PreviewTestUtils()

}


