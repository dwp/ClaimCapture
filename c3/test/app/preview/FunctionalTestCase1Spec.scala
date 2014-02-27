package app.preview

import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage
import utils.pageobjects._
import utils.pageobjects.xml_validation.{XMLClaimBusinessValidation, XMLBusinessValidation}
import app.FunctionalTestCommon
import utils.pageobjects.preview.PreviewPage
import org.fluentlenium.core.filter.FilterConstructor._
import org.openqa.selenium.By
import models.DayMonthYear
import org.joda.time.format.DateTimeFormat
import play.api.Logger

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class FunctionalTestCase1Spec extends FunctionalTestCommon {
  isolated

  "The application Claim" should {
    "Successfully run absolute Claim Test Case 1" in new WithBrowser with PageObjects {


      val page = G1BenefitsPage(context)
      implicit val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, PreviewPage.title)

      println(lastPage.source())

      val toFindData = Seq(
          "Name" -> Seq("AboutYouTitle","AboutYouFirstName","AboutYouMiddleName","AboutYouSurname"),
          "National Insurance number" -> Seq("AboutYouNINO"),
          "Date of birth" -> Seq(DateTransformer("AboutYouDateOfBirth")),
          "Address" -> Seq(AddressTransformer("AboutYouAddress"),"AboutYouPostcode"),
          "Your claim date" -> Seq(DateTransformer("AboutYouWhenDoYouWantYourCarersAllowanceClaimtoStart")),
          "Your nationality" -> Seq("AboutYouNationalityAndResidencyNationality"),
          "Do you normally live in England, Scotland or Wales?" -> Seq("AboutYouNationalityAndResidencyResideInUK"),
          "Time outside of England, Scotland or Wales" -> Seq(AnyYesTransformer("AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime")),
          "Do you, or any member of your family, receive any benefits or pensions from a European Economic Area (EEA) state or Switzerland?" -> Seq("OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA"),
          "Have you, or a member of your family, made a claim for any benefits or pensions from a European Economic Area (EEA) state or Switzerland?" -> Seq("OtherMoneyOtherAreYouClaimingForBenefitsFromAnotherEEA"),
          "Are you, or a member of your family, working in or paying insurance to, another European Economic Area (EEA) state or Switzerland?" -> Seq("OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA"),
          "Marital status" -> Seq(MaritalTransformer("AboutYouWhatIsYourMaritalOrCivilPartnershipStatus")),
          "Name" -> Seq("AboutTheCareYouProvideTitlePersonCareFor","AboutTheCareYouProvideFirstNamePersonCareFor","AboutTheCareYouProvideMiddleNamePersonCareFor","AboutTheCareYouProvideSurnamePersonCareFor"),
          "Date of birth" -> Seq(DateTransformer("AboutTheCareYouProvideDateofBirthPersonYouCareFor")),
          "Address" -> Seq(AddressTransformer("AboutTheCareYouProvideAddressPersonCareFor"),"AboutTheCareYouProvidePostcodePersonCareFor"),
          "What's their relationship to you?" -> Seq("AboutTheCareYouProvideWhatTheirRelationshipToYou"),
          "Do you spend 35 hours or more each week caring for this person?" -> Seq("AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek"),
          "Have you had any breaks in caring for this person" -> Seq(AnyYesTransformer("AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare")),
          "Have you been on a course of education since your claim date?" -> Seq("AboutYouHaveYouBeenOnACourseOfEducation"),
          "Have you been employed at any time since" -> Seq("EmploymentHaveYouBeenEmployedAtAnyTime_0"),
          "Have you been self employed at any time since" -> Seq("EmploymentHaveYouBeenSelfEmployedAtAnyTime"),
          "Have you  claimed or received any other benefits since your claim date" -> Seq("OtherMoneyHaveYouClaimedOtherBenefits"),
          "Have you received any payments for the person you care for or any other person since your claim date" -> Seq("OtherMoneyAnyPaymentsSinceClaimDate"),
          "Have you had any Statutory Sick Pay" -> Seq("OtherMoneyHaveYouSSPSinceClaim"),
          "Have you had any SMP, SPP or SAP since your claim date" -> Seq("OtherMoneyHaveYouSMPSinceClaim")
      )


      toFindData.foreach{t =>
        import scala.collection.JavaConverters._


        var matchesAny = false
        val elems = context.browser.webDriver.findElements(By.xpath(s"""//dt[contains(.,"${t._1}")]/following-sibling::dd[1]""")).asScala

        Logger.debug("")
        Logger.debug(s"Checking for ${t._1} and found ${elems.size} elems")
        elems.foreach{webElem =>
          val elemValue = webElem.getText.toLowerCase.replaceAll(" ","")
          t._2.foreach{ fieldId =>
            val claimValue = fieldId match {
              case id:String => claim.selectDynamic(id)
              case transformer:Transformer => transformer.transform()
            }

            if (claimValue != null && claimValue.length > 0){
              val modifiedClaimValue = claimValue.toLowerCase.replaceAll(" ","")
              Logger.debug(s"$modifiedClaimValue in $elemValue ? ${elemValue.contains(modifiedClaimValue)}")
              if(elemValue.contains(modifiedClaimValue)) matchesAny = true
            }

          }
        }

        if (!matchesAny) failure(s"${t._1} does not match its value")

      }

    }

  } section ("functional","preview")
}

trait Transformer{
  val id:String
  def transform():String
}

case class DateTransformer(id:String,patternIn:String="dd/MM/yyyy",patternOut:String = "dd MMMM, yyyy")(implicit claim:TestData) extends Transformer{
  override def transform(): String = DateTimeFormat.forPattern(patternIn).parseDateTime(claim.selectDynamic(id)).toString(patternOut)
}
case class AddressTransformer(id:String)(implicit claim:TestData) extends Transformer{
  override def transform(): String = claim.selectDynamic(id).replaceAll("&",",")
}

case class AnyYesTransformer(id:String)(implicit claim:TestData) extends Transformer{
  override def transform():String = {
    var valueSeq = Seq.empty[String]
    var i = 1

    while(claim.selectDynamic(id+"_"+i) != null){
      valueSeq = valueSeq.+:(claim.selectDynamic(id+"_"+i).toLowerCase)
      i = i+1
    }

    if(valueSeq.contains("yes")) "yes" else "no"
  }
}

case class MaritalTransformer(id:String)(implicit claim:TestData) extends Transformer{
  val rels = Map("m" -> "Married or civil partner","s" -> "Single","d" -> "Divorced or civil partnership dissolved","w" -> "Widowed or surviving civil partner","n" -> "Separated","p" -> "Living with partner")

  override def transform():String = {
    rels(claim.selectDynamic(id))
  }
}



