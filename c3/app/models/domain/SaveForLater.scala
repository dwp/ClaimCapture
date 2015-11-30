package models.domain

import models.{DayMonthYear, NationalInsuranceNumber}

case class SaveForLater(
                         )

case class ResumeClaim(
                        firstName: String = "",
                  surname: String = "",
                  nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(None),
                  dateOfBirth: DayMonthYear = DayMonthYear(None, None, None)) extends Serializable

