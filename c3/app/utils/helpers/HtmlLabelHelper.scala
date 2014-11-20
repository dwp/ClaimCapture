package utils.helpers

import models.DayMonthYear


object HtmlLabelHelper {

  def displayPlaybackDatesFormat(implicit lang: play.api.i18n.Lang, date:DayMonthYear):String = {
    date.`d month yyyy`
  }
}
