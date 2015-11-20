package controllers.circs.s2_report_changes

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._
import scala.Predef._
import models.MultiLineAddress

/**
 * Created by neddakaltcheva on 2/14/14.
 */
class G6AddressChangeFormSpec extends Specification {
  "Report a change in your address - Address Change Form" should {
    val yes = "yes"
    val no = "no"
    val stillCaringDateDay = 10
    val stillCaringDateMonth = 11
    val stillCaringDateYear = 2012
    val invalidYear = 99999
    val moreAboutChanges = "This is more about the change"
    val addressLineOne = "lineOne"
    val addressLineTwo = "lineTwo"
    val addressLineThree = "lineThree"
    val postCode = "PR1A4JQ"

    "map data into case class" in new WithApplication {
      G6AddressChange.form.bind(
        Map(
          "previousAddress.lineOne" -> addressLineOne,
          "previousAddress.lineTwo" -> addressLineTwo,
          "previousAddress.lineThree" -> addressLineThree,
          "previousPostcode" -> postCode,
          "stillCaring.answer" -> yes,
          "newAddress.lineOne" -> addressLineOne,
          "newAddress.lineTwo" -> addressLineTwo,
          "newAddress.lineThree" -> addressLineThree,
          "newPostcode" -> postCode,
          "caredForChangedAddress.answer" -> yes,
          "sameAddress.answer" -> yes,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          form => {
            form.previousAddress must equalTo(MultiLineAddress(Some(addressLineOne), Some(addressLineTwo), Some(addressLineThree)))
            form.previousPostcode must equalTo(Some(postCode))
            form.stillCaring.answer must equalTo(yes)
            form.newAddress must equalTo(MultiLineAddress(Some(addressLineOne), Some(addressLineTwo), Some(addressLineThree)))
            form.newPostcode must equalTo(Some(postCode))
            form.caredForChangedAddress.answer must equalTo(Some(yes))
            form.sameAddress.answer must equalTo(Some(yes))
            form.moreAboutChanges must equalTo(Some(moreAboutChanges))
          }
        )
    }

    "mandatory fields must be populated when still caring is not set" in new WithApplication {
      G6AddressChange.form.bind(
        Map("moreAboutChanges" -> moreAboutChanges, "newPostcode" -> postCode, "previousPostcode" -> postCode)
      ).fold(
          formWithErrors => {
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(2).message must equalTo(Mappings.errorRequired)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "mandatory fields must be populated when still caring is set to 'no'" in new WithApplication {
      G6AddressChange.form.bind(
        Map(
          "stillCaring_answer" -> no,
          "newPostcode" -> postCode,
          "previousPostcode" -> postCode,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(2).message must equalTo(Mappings.errorRequired)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "mandatory fields must be populated when still caring is set to 'yes'" in new WithApplication {
      G6AddressChange.form.bind(
        Map(
          "stillCaring_answer" -> yes,
          "newPostcode" -> postCode,
          "previousPostcode" -> postCode,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(2).message must equalTo(Mappings.errorRequired)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject special characters in text field" in new WithApplication {
      G6AddressChange.form.bind(
        Map(
          "previousAddress.lineOne" -> addressLineOne,
          "previousAddress.lineTwo" -> addressLineTwo,
          "previousAddress.lineThree" -> addressLineThree,
          "previousPostcode" -> postCode,
          "stillCaring.answer" -> yes,
          "stillCaring.date.day" -> stillCaringDateDay.toString,
          "stillCaring.date.month" -> stillCaringDateMonth.toString,
          "stillCaring.date.year" -> stillCaringDateYear.toString,
          "newAddress.lineOne" -> addressLineOne,
          "newAddress.lineTwo" -> addressLineTwo,
          "newAddress.lineThree" -> addressLineThree,
          "newPostcode" -> postCode,
          "caredForChangedAddress.answer" -> yes,
          "sameAddress.answer" -> yes,
          "moreAboutChanges" -> "<>"
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject invalid still caring date" in new WithApplication {
      G6AddressChange.form.bind(
        Map(
          "previousAddress.lineOne" -> addressLineOne,
          "previousAddress.lineTwo" -> addressLineTwo,
          "previousAddress.lineThree" -> addressLineThree,
          "previousPostcode" -> postCode,
          "stillCaring.answer" -> yes,
          "stillCaring.date.day" -> stillCaringDateDay.toString,
          "stillCaring.date.month" -> stillCaringDateMonth.toString,
          "stillCaring_date_year" -> invalidYear.toString,
          "newAddress.lineOne" -> addressLineOne,
          "newAddress.lineTwo" -> addressLineTwo,
          "newAddress.lineThree" -> addressLineThree,
          "newPostcode" -> postCode,
          "caredForChangedAddress.answer" -> yes,
          "sameAddress.answer" -> yes,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if second line of address is empty for previous address" in new WithApplication {
      G6AddressChange.form.bind(
        Map(
          "previousAddress.lineOne" -> addressLineOne,
          "previousAddress.lineTwo" -> "",
          "previousAddress.lineThree" -> addressLineThree,
          "previousPostcode" -> postCode,
          "stillCaring.answer" -> yes,
          "stillCaring.date.day" -> stillCaringDateDay.toString,
          "stillCaring.date.month" -> stillCaringDateMonth.toString,
          "stillCaring.date.year" -> stillCaringDateYear.toString,
          "newAddress.lineOne" -> addressLineOne,
          "newAddress.lineTwo" -> addressLineTwo,
          "newAddress.lineThree" -> addressLineThree,
          "newPostcode" -> postCode,
          "caredForChangedAddress.answer" -> yes,
          "sameAddress.answer" -> yes,
          "moreAboutChanges" -> "No more changes"
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.addressLines.required")
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if second line of address is empty for new address" in new WithApplication {
      G6AddressChange.form.bind(
        Map(
          "previousAddress.lineOne" -> addressLineOne,
          "previousAddress.lineTwo" -> addressLineTwo,
          "previousAddress.lineThree" -> addressLineThree,
          "previousPostcode" -> postCode,
          "stillCaring.answer" -> yes,
          "stillCaring.date.day" -> stillCaringDateDay.toString,
          "stillCaring.date.month" -> stillCaringDateMonth.toString,
          "stillCaring.date.year" -> stillCaringDateYear.toString,
          "newAddress.lineOne" -> addressLineOne,
          "newAddress.lineTwo" -> "",
          "newAddress.lineThree" -> addressLineThree,
          "newPostcode" -> postCode,
          "caredForChangedAddress.answer" -> yes,
          "sameAddress.answer" -> yes,
          "moreAboutChanges" -> "No more changes"
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.addressLines.required")
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

  }
  section("unit", models.domain.CircumstancesAddressChange.id)
}
