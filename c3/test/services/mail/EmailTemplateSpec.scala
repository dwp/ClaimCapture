package services.mail

import models.DayMonthYear
import models.domain._
import models.view.CachedClaim
import models.yesNo.{YesNoWithEmployerAndMoney, YesNo, YesNoWithText}
import org.specs2.mutable._
import play.api.test.{FakeApplication}
import play.twirl.api.{HtmlFormat}
import controllers.mappings.Mappings
import utils.WithApplication
import utils.LightFakeApplication
import play.api.i18n._
import play.api.Play.current

/**
 * Created by valtechuk on 24/03/2015.
 */
class EmailTemplateSpec extends Specification {
  def escapeMessage(id:String,param:String="") = {
    val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
    messagesApi(id,param)
  }

  "Email template" should {

    val xmlSchemaVersionNumber = "some value"
    "Display XML schema version number" in new WithApplication(app = LightFakeApplication.faXmlVersion(xmlSchemaVersionNumber)){
      implicit val lang = Lang("en")
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      implicit val messages = Messages(lang, messagesApi)
      val claim = Claim(CachedClaim.key)
      val renderedEmail = views.html.mail(claim,isClaim = true,isEmployment = false).body
      renderedEmail must contain(xmlSchemaVersionNumber)
    }

    "Display claim email" in new WithApplication {
      implicit val lang = Lang("en")
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      implicit val messages = Messages(lang, messagesApi)
      val claim = Claim(CachedClaim.key)
      val renderedEmail = views.html.mail(claim,isClaim = true,isEmployment = false).body

      renderedEmail.size must beGreaterThan(0)


      renderedEmail must contain(escapeMessage("mail.claim.title"))
      renderedEmail must contain(escapeMessage("mail.claim.successful"))
      renderedEmail must contain(escapeMessage("mail.claim.next.line1"))
      renderedEmail must contain(escapeMessage("mail.next.line"))

      renderedEmail must not contain escapeMessage("mail.cofc.title")
      renderedEmail must not contain escapeMessage("mail.cofc.successful")
    }

    "Display claim employment and self employment email" in new WithApplication {
      implicit val lang = Lang("en")
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      implicit val messages = Messages(lang, messagesApi)
      val jobs= Employment(Mappings.yes,Mappings.yes)
      val claim = Claim(CachedClaim.key).+(ClaimDate(DayMonthYear())).update(jobs)

      val renderedEmail = views.html.mail(claim,isClaim = true,isEmployment = true).body

      renderedEmail.size must beGreaterThan(0)

      renderedEmail must contain(escapeMessage("evidence.selfEmployment.accounts"))

      renderedEmail must contain(escapeMessage("mail.claim.title"))
      renderedEmail must contain(escapeMessage("mail.claim.successful"))
      renderedEmail must contain(escapeMessage("mail.claim.next.line1.alt"))
      renderedEmail must contain(escapeMessage("mail.next.line2"))
      renderedEmail must contain(escapeMessage("mail.next.send1"))
      renderedEmail must contain(escapeMessage("evidence.email.employment.mostRecentPayslipBefore",DayMonthYear().`dd month yyyy`))

      renderedEmail must not contain escapeMessage("evidence.pensionStatements")
      renderedEmail must not contain escapeMessage("mail.next.line")

      renderedEmail must not contain escapeMessage("mail.cofc.title")
      renderedEmail must not contain escapeMessage("mail.cofc.successful")
    }

    "Ask for pension documents if paying into pension" in new WithApplication {
      implicit val lang = Lang("en")
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      implicit val messages = Messages(lang, messagesApi)
      val employment= Employment(Mappings.yes,Mappings.yes)
      val pensionAndExpenses = PensionAndExpenses("", YesNoWithText(Mappings.yes, Some("blah blah blah")),
        YesNoWithText("", None), YesNoWithText("", None))
      val jobs = Jobs(List(Iteration("1", List(pensionAndExpenses))))
      val claim = Claim(CachedClaim.key).+(ClaimDate(DayMonthYear())).update(employment).update(jobs)

      val renderedEmail = views.html.mail(claim,isClaim = true,isEmployment = true).body

      renderedEmail.size must beGreaterThan(0)

      renderedEmail must contain(escapeMessage("evidence.selfEmployment.accounts"))

      renderedEmail must contain(escapeMessage("mail.claim.title"))
      renderedEmail must contain(escapeMessage("mail.claim.successful"))
      renderedEmail must contain(escapeMessage("mail.claim.next.line1.alt"))
      renderedEmail must contain(escapeMessage("mail.next.line2"))
      renderedEmail must contain(escapeMessage("mail.next.send1"))
      renderedEmail must contain(escapeMessage("evidence.pensionStatements"))
      renderedEmail must contain(escapeMessage("evidence.email.employment.mostRecentPayslipBefore",DayMonthYear().`dd month yyyy`))

      renderedEmail must not contain escapeMessage("mail.next.line")

      renderedEmail must not contain escapeMessage("mail.cofc.title")
      renderedEmail must not contain escapeMessage("mail.cofc.successful")
    }

    "Ask for evidence if statutory payments are received" in new WithApplication {
      implicit val lang = Lang("en")
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      implicit val messages = Messages(lang, messagesApi)
      val otherPayments = AboutOtherMoney(YesNo(""), None, None, None,
        YesNoWithEmployerAndMoney(Mappings.yes, None, None, None, None, None),
        YesNoWithEmployerAndMoney(Mappings.yes, None, None, None, None, None))

      // Without statutory payments
      val claim = Claim(CachedClaim.key).+(ClaimDate(DayMonthYear()))
      val email = views.html.mail(claim,isClaim = true,isEmployment = true).body
      email must not contain escapeMessage("evidence.otherMoney.statutorySickPay")
      email must not contain escapeMessage("evidence.otherMoney.otherStatutoryPay")

      // With statutory payments
      val claimWithStatutoryPayments = claim.update(otherPayments)
      val emailWithStatutoryPayments = views.html.mail(claimWithStatutoryPayments,isClaim = true,isEmployment = false).body
      val emailWithEmploymentAsWell = views.html.mail(claimWithStatutoryPayments,isClaim = true,isEmployment = true).body
      emailWithStatutoryPayments must contain(escapeMessage("evidence.otherMoney.statutorySickPay"))
      emailWithStatutoryPayments must contain(escapeMessage("evidence.otherMoney.otherStatutoryPay"))
      emailWithEmploymentAsWell must contain(escapeMessage("evidence.otherMoney.statutorySickPay"))
      emailWithEmploymentAsWell must contain(escapeMessage("evidence.otherMoney.otherStatutoryPay"))
    }

    "Display cofc employment email" in new WithApplication {
      implicit val lang = Lang("en")
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      implicit val messages = Messages(lang, messagesApi)
      val claim = Claim(CachedClaim.key)
      val renderedEmail = views.html.mail(claim,isClaim = false,isEmployment = true).body

      renderedEmail.size must beGreaterThan(0)


      renderedEmail must contain(escapeMessage("mail.cofc.title"))
      renderedEmail must contain(escapeMessage("mail.cofc.successful"))
      renderedEmail must contain(escapeMessage("evidence.email.cofc.employment.anyPayslips"))

      renderedEmail must contain(escapeMessage("mail.next.send1"))
      renderedEmail must contain(escapeMessage("mail.next.line2"))
      renderedEmail must contain(escapeMessage("evidence.pensionStatements"))

      renderedEmail must not contain escapeMessage("evidence.email.employment.mostRecentPayslipBefore")
      renderedEmail must not contain escapeMessage("mail.next.line")

      renderedEmail must not contain escapeMessage("mail.claim.next.line1.alt")
      renderedEmail must not contain escapeMessage("mail.claim.title")
      renderedEmail must not contain escapeMessage("mail.claim.successful")
      renderedEmail must not contain escapeMessage("mail.claim.next.line1")
    }

    "Display cofc email" in new WithApplication {
      implicit val lang = Lang("en")
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      implicit val messages = Messages(lang, messagesApi)
      val claim = Claim(CachedClaim.key)
      val renderedEmail = views.html.mail(claim,false,false).body

      renderedEmail.size must beGreaterThan(0)

      renderedEmail must contain(escapeMessage("mail.cofc.title"))
      renderedEmail must contain(escapeMessage("mail.cofc.successful"))
      renderedEmail must contain(escapeMessage("mail.next.line"))

      renderedEmail must not contain escapeMessage("mail.claim.title")
      renderedEmail must not contain escapeMessage("mail.claim.successful")
      renderedEmail must not contain escapeMessage("mail.claim.next.line1")
    }

  }
  section("unit")
}
