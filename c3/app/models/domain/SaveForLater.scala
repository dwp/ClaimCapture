package models.domain

case class SaveForLater(nationality: Option[String] = None,
                     actualnationality: Option[String] = None,
                     resideInUK: ResideInUK,
                     anyTrips: Option[String],
                     tripDetails: Option[String]
                      )

case class ResideInUK(answer: Option[String] = None,
                      text: Option[String] = None
                       )

