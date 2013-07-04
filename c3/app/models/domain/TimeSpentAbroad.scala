package models.domain

case object TimeSpentAbroad {
  val id = "s5"
}

case class NormalResidenceAndCurrentLocation(normallyLiveInUK: String, whereDoYouNormallyLive: Option[String] = None) extends QuestionGroup(NormalResidenceAndCurrentLocation.id)

case object NormalResidenceAndCurrentLocation extends QuestionGroup(s"${TimeSpentAbroad.id}.g1")