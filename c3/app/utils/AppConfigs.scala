package utils

import models.claim._
import play.api.mvc.Call

object AppConfigs {

  lazy val urlMap = Map(Benefits.id -> controllers.routes.CarersAllowance.benefits()
    , Hours.id ->  controllers.routes.CarersAllowance.hours()
    , LivesInGB.id ->  controllers.routes.CarersAllowance.livesInGB()
    , Over16.id ->  controllers.routes.CarersAllowance.over16()
    , YourDetails.id -> controllers.routes.AboutYou.yourDetails()
    , ContactDetails.id -> controllers.routes.AboutYou.contactDetails()
  )

  def formUrl(formId:String): Call = {
    urlMap.get(formId) match {
        case Some(call) => call
        case None => controllers.routes.CarersAllowance.benefits()
    }
  }


}
