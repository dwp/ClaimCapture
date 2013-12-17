package models

import models.yesNo.YesNo

case class NationalityAndResidence(nationality: String = "",
                                   resideInUK: YesNo,
                                   residence: Option[String] = None)