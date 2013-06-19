package validation

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import models.NationalInsuranceNumber
import play.api.data.Form
import play.api.data.Mapping
import play.api.data.validation.Constraints._
import play.api.data.Forms._
import controllers.Validation

class NationalInsuranceNumberValidationSpec extends Specification {
  
"NI validation" should{
  "not complain about a valid NI" in {


    Form( "nationalInsuranceNumber" -> Validation.nationalInsuranceNumber.verifying( Validation.validNationalInsuranceNumber) ).bind( Map(
        "nationalInsuranceNumber.ni1" -> "AB",
        "nationalInsuranceNumber.ni2" -> "12",
        "nationalInsuranceNumber.ni3" -> "34",
        "nationalInsuranceNumber.ni4" -> "56",
        "nationalInsuranceNumber.ni5" -> "C") ).fold(
        formWithErrors => { "The mapping should not fail." must equalTo("Error") },
        { number => 
          number.ni1 must equalTo(Some("AB"))
          number.ni2 must equalTo(Some(12))
          number.ni3 must equalTo(Some(34))
          number.ni4 must equalTo(Some(56))
          number.ni5 must equalTo(Some("C"))
        }
    )

  }
}
  
}