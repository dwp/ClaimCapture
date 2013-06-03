import org.scalatest._
import org.scalatest.matchers.MustMatchers
import org.scalatest.selenium._
import models.view.trash.{Claim, ContactDetailsForm, ContactDetails, AboutYou}

class HtmlSpec extends FunSpec with MustMatchers with HtmlUnit {
  describe("Html tests") {
    it("must have the correct title"){
      go to ("http://localhost:9000/")
      pageTitle must be ("GOV.UK - The best place to find government services and information")
    }

  }
}
