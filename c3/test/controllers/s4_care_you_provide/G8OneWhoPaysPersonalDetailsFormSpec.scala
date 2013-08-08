package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear
import models.domain.OneWhoPaysPersonalDetails
import play.api.data.{FormError, Form}

class G8OneWhoPaysPersonalDetailsFormSpec extends Specification with Tags {
  val `36Characters` = (1 to 36).foldLeft("x")((x, _) => x + "x").mkString

  val `101Characters` = (1 to 101).foldLeft("x")((x, _) => x + "x").mkString

  val data = Map(
    "organisation" -> "DWP",
    "title" -> "mr",
    "firstName" -> "Ronald",
    "middleName" -> "Mc",
    "surname" -> "Donald",
    "amount" -> "30",
    "startDatePayment.day" -> "3",
    "startDatePayment.month" -> "4",
    "startDatePayment.year" -> "1980")

  "One Who Pays Personal Details Form" should {
    "map data into case class" in {
      val o: OneWhoPaysPersonalDetails = G8OneWhoPaysPersonalDetails.form.bind(data).get

      o.organisation should beSome("DWP")
      o.title should beSome("mr")
      o.firstName shouldEqual "Ronald"
      o.middleName should beSome("Mc")
      o.surname shouldEqual "Donald"
      o.amount shouldEqual "30"
      o.startDatePayment shouldEqual DayMonthYear(Some(3), Some(4), Some(1980), None, None)
    }

    "reject organisation name of over 100 characters" in {
      val form: Form[OneWhoPaysPersonalDetails] = G8OneWhoPaysPersonalDetails.form.bind(data + ("organisation" -> `101Characters`))

      form.errors.size shouldEqual 1
      form.error("organisation") should beLike { case Some(fe: FormError) => fe.message shouldEqual "error.maxLength" }
    }

    "reject first name of over 35 characters" in {
      val form: Form[OneWhoPaysPersonalDetails] = G8OneWhoPaysPersonalDetails.form.bind(data + ("firstName" -> `36Characters`))

      form.errors.size shouldEqual 1
      form.error("firstName") should beLike { case Some(fe: FormError) => fe.message shouldEqual "error.maxLength" }
    }

    "reject middle name of over 35 characters" in {
      val form: Form[OneWhoPaysPersonalDetails] = G8OneWhoPaysPersonalDetails.form.bind(data + ("middleName" -> `36Characters`))

      form.errors.size shouldEqual 1
      form.error("middleName") should beLike { case Some(fe: FormError) => fe.message shouldEqual "error.maxLength" }
    }

    "reject surname of over 35 characters" in {
      val form: Form[OneWhoPaysPersonalDetails] = G8OneWhoPaysPersonalDetails.form.bind(data + ("surname" -> `36Characters`))

      form.errors.size shouldEqual 1
      form.error("surname") should beLike { case Some(fe: FormError) => fe.message shouldEqual "error.maxLength" }
    }

    "reject 3 decimal amount" in {
      val form: Form[OneWhoPaysPersonalDetails] = G8OneWhoPaysPersonalDetails.form.bind(data + ("amount" -> "500.501"))

      form.errors.size shouldEqual 1
      form.error("amount") should beLike { case Some(fe: FormError) => fe.message shouldEqual "decimal.invalid" }
    }

    "reject invalid date" in {
      val form: Form[OneWhoPaysPersonalDetails] = G8OneWhoPaysPersonalDetails.form.bind(data + ("startDatePayment.year" -> "12345"))

      form.errors.size shouldEqual 1
      form.error("startDatePayment") should beLike { case Some(fe: FormError) => fe.message shouldEqual "error.invalid" }
    }

    "reject all missing data" in {
      val form: Form[OneWhoPaysPersonalDetails] = G8OneWhoPaysPersonalDetails.form.bind(Map.empty[String, String])

      form.errors should containAllOf(FormError("firstName", "error.required") ::
                                      FormError("surname", "error.required") ::
                                      FormError("amount", "error.required") ::
                                      FormError("startDatePayment", "error.required") :: Nil).only
    }
  } section("unit", models.domain.CareYouProvide.id)
}