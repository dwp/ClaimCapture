package models.domain

case class CheckYAnswers(previouslySavedClaim:Option[Claim] = None,cyaPointOfEntry:Option[String] = None)
