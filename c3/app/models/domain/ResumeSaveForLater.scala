package models.domain

import models.{DayMonthYear, NationalInsuranceNumber}

/**
 * Created by peterwhitehead on 27/11/2015.
 */
case class ResumeSaveForLater(firstName: String = "",
                              surname: String = "",
                              nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(None),
                              dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                              uuid: String = "")
