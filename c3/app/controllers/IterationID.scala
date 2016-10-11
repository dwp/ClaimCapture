package controllers

import java.util.UUID._

import models.domain.Claim
import play.api.mvc.Request

object IterationID {
  def apply(form: play.api.data.Form[_])(implicit claim: Claim, request: Request[_]): String = {
    val regex =
      if (request.path.contains("employment"))
        """^(?:.*?)/your-income/employment/(?:.*?)(?:/(.*?))?$""".r
      else
        """^(?:.*?)/breaks/(?:.*?)(?:/(.*?))?$""".r

    form("iterationID").value.flatMap(s => if (s.isEmpty) None else Some(s)).getOrElse(regex.findFirstMatchIn(request.path).map {
      _ group 1 match {
        case s if s != null && s.length > 0 => s
        case _ => randomUUID.toString
      }
    }.getOrElse(randomUUID.toString))
  }
}

object Iteration {
  trait Identifier {
    val iterationID: String
  }
}
