package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{AboutSelfEmployment, Employment, Claim}
import controllers.Mappings._
import models.DayMonthYear

class SelfEmploymentSpec extends Specification with Tags {

  "SelfEmployment" should {

    val startDate = DayMonthYear(Some(1), Some(1), Some(2000))
    val endDate = DayMonthYear(Some(1), Some(1), Some(2005))
    val software = "software"

    "generate xml when data is present" in {
      val aboutSelfEmployment =  AboutSelfEmployment(areYouSelfEmployedNow = yes,
        whenDidYouStartThisJob=Some(startDate),
        whenDidTheJobFinish=Some(endDate),
        haveYouCeasedTrading = Some(no),
        natureOfYourBusiness = Some(software)
      )

      val claim = Claim().update(Employment(beenSelfEmployedSince1WeekBeforeClaim = yes))
      .update(aboutSelfEmployment)

      val selfEmploymentXml = SelfEmployment.xml(claim)

      (selfEmploymentXml \\ "SelfEmployedNow").text mustEqual yes
      val recentJobDetailsXml = selfEmploymentXml \\ "RecentJobDetails"
      (recentJobDetailsXml \\ "DateStarted").text mustEqual startDate.`yyyy-MM-dd`
      (recentJobDetailsXml \\ "DateEnded").text mustEqual endDate.`yyyy-MM-dd`
      (recentJobDetailsXml \\ "NatureOfBusiness").text mustEqual software
      (recentJobDetailsXml \\ "TradingCeased").text mustEqual no
    }

    "generate xml when data is missing" in {
      val claim = Claim().update(Employment(beenSelfEmployedSince1WeekBeforeClaim = no))
      val selfEmploymentXml = SelfEmployment.xml(claim)
      selfEmploymentXml.text mustEqual ""
    }

  } section "unit"

}