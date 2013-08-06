package controllers.s7_employment

object JobID {
  def apply(form: play.api.data.Form[_])(implicit claim: models.domain.Claim, request: play.api.mvc.Request[_]): String = {
    val regex = """^(?:.*?)/employment/(?:.*?)(?:/(.*?))?$""".r

    form("jobID").value.getOrElse(regex.findFirstMatchIn(request.path).map {
      _ group 1 match {
        case s if s != null && s.length > 0 => s
        case _ => java.util.UUID.randomUUID.toString
      }
    }.getOrElse(java.util.UUID.randomUUID.toString))
  }
}