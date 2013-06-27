package validation

import org.specs2.mutable.Specification
import play.api.data.Form
import controllers.Mappings._
import scala.Some
import models.MultiLineAddress

class MultiLineAddressValidationSpec extends Specification {

  "Multi Line Address" should {

    "reject empty input" in {
      Form( "address" -> ( address.verifying(requiredAddress) ) ).bind( Map( "address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "") ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        address => "This mapping should not happen." must equalTo ("Valid")
      )
    }

    "accept single lineOne" in {
      Form( "address" -> ( address.verifying(requiredAddress) ) ).bind( Map( "address.lineOne" -> "line1", "address.lineTwo" -> "", "address.lineThree" -> "") ).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        address => address must equalTo( MultiLineAddress( Some("line1"), None, None) )
        )
    }

    "accept single lineTwo" in {
      Form( "address" -> ( address.verifying(requiredAddress) ) ).bind( Map( "address.lineOne" -> "", "address.lineTwo" -> "line2", "address.lineThree" -> "") ).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        address => address must equalTo( MultiLineAddress( None, Some("line2"), None) )
      )
    }

    "accept single lineThree" in {
      Form( "address" -> ( address.verifying(requiredAddress) ) ).bind( Map( "address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "line3") ).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        address => address must equalTo( MultiLineAddress( None, None, Some("line3")) )
      )
    }

    "have a maxLength constraint for lineOne" in {
      Form( "address" -> ( address.verifying(requiredAddress) ) ).bind(
        Map( "address.lineOne" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        address => "This mapping should not happen." must equalTo ("Valid")
      )
    }

    "have a maxLength constraint for lineTwo" in {
      Form( "address" -> ( address.verifying(requiredAddress) ) ).bind(
        Map( "address.lineTwo" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        address => "This mapping should not happen." must equalTo ("Valid")
      )
    }

    "have a maxLength constraint for lineThree" in {
      Form( "address" -> ( address.verifying(requiredAddress) ) ).bind(
        Map( "address.lineThree" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        address => "This mapping should not happen." must equalTo ("Valid")
      )
    }
  }

}
