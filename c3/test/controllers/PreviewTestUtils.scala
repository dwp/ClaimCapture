package controllers

import org.openqa.selenium.By
import utils.pageobjects.Page
import utils.helpers.PreviewField._


class PreviewTestUtils {

}

object PreviewTestUtils {

  def answerText(questionId: String, previewPage: Page): String = {
    previewPage.ctx.browser.webDriver.findElement(By.id(getValueId(questionId, false))).getText
  }

  def apply() = new PreviewTestUtils()

}


