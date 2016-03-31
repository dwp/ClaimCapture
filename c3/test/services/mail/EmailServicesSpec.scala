package services.mail

import app.XMLValues._
import models.domain.{SelfEmploymentPensionsAndExpenses, QuestionGroup, YourIncomes, Claim}
import models.view.CachedClaim
import models.yesNo.YesNoWithText
import org.specs2.mutable._
import services.EmailServices
import utils.WithApplication

/**
 * Created by tudormalene on 17/08/15.
 */
class EmailServicesSpec extends Specification {
  section ("unit")
  "The email subject" should {
    """When i answer Yes to "Have you been employed at any time since <<dd/MON/yyyy>> (this is six months before your claim date: <<dd/MON/yyyy)?"
      |or "Have you been self-employed at any time since <<dd/MON/yyyy>>"  or <<Do>>, >> or << Did>> you pay into a pension?"
      |Then the text displayed in the subject line must read "Carer's Allowance application: next steps"""" in new WithApplication {

      verifyEmployedSubject(YourIncomes(no, yes))
      verifyEmployedSubject(YourIncomes(yes, no))
      verifyEmployedSubject(SelfEmploymentPensionsAndExpenses(YesNoWithText(yes, None)))

    }

    """When i answer No to "Have you been employed at any time since <<dd/MON/yyyy>> (this is six months before your claim date: <<dd/MON/yyyy)?"
      |And I answer No to "Have you been self-employed at any time since <<dd/MON/yyyy>>"
      |Then the text in the subject line must read "Carer's Allowance application received"""" in new WithApplication {

      verifyNotEmployedSubject(YourIncomes(no, no))
    }
  }
  section ("unit")

  def verifyEmployedSubject(qg: QuestionGroup) = "Carer's Allowance application: next steps" must_== EmailServices.claimEmailSubject(Claim(CachedClaim.key).update(qg))

  def verifyNotEmployedSubject(qg: QuestionGroup) = "Carer's Allowance application received" must_== EmailServices.claimEmailSubject(Claim(CachedClaim.key).update(qg))
}
