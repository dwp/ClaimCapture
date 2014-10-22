package controllers


object PreviewRouteUtils {

  def yourDetailsRoute = {
    val g1YourDetailsRoute = controllers.s2_about_you.routes.G1YourDetails.present.toString
    val g2ContactDetailsRoute = controllers.s2_about_you.routes.G2ContactDetails.present.toString
    val g4NationalityRoute = controllers.s2_about_you.routes.G4NationalityAndResidency.present.toString
    val g7OtherEEARoute = controllers.s2_about_you.routes.G7OtherEEAStateOrSwitzerland.present.toString

    val routesMap = Map("about_you_full_name" -> g1YourDetailsRoute,
                        "about_you_nino" -> g1YourDetailsRoute,
                        "about_you_dob" -> g1YourDetailsRoute,
                        "about_you_address" -> g2ContactDetailsRoute,
                        "about_you_contact" -> g2ContactDetailsRoute,
                        "about_you_marital_status" -> g4NationalityRoute,
                        "about_you_claimDate" -> controllers.s1_2_claim_date.routes.G1ClaimDate.present.toString,
                        "about_you_nationality" -> g4NationalityRoute,
                        "about_you_abroad" -> controllers.s2_about_you.routes.G5AbroadForMoreThan52Weeks.present.toString,
                        "about_you_benefitsFromEEA" -> g7OtherEEARoute,
                        "about_you_workingForEEA" -> g7OtherEEARoute)

    routesMap

  }

  def otherMoneyRoute = {
    val g1AboutOtherMoneyRoute = controllers.s9_other_money.routes.G1AboutOtherMoney.present.toString
    val idList = Seq("other_money_anyPaymentsSinceClaimDate", "other_money_statutoryPay", "other_money_otherStatutoryPay")

    val routesMap = Map(idList map {id => (id, g1AboutOtherMoneyRoute)} : _*)

    routesMap
  }

  def educationRoute = {
    val g1YourCourseDetailsRoute = controllers.s6_education.routes.G1YourCourseDetails.present.toString
    val idList = Seq("education_beenInEducationSinceClaimDate", "education_courseTitle", "education_nameOfSchool",
      "education_nameOfTutor", "education_contactNumber", "education_startEndDates")

    val routesMap = Map(idList map {id => (id, g1YourCourseDetailsRoute)} : _*)

    routesMap
  }

  def careYouProvide = {
    val g1ThierPersonalDetailsRoute = controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present.toString
    val personalDetailsList = Seq("care_you_provide_name", "care_you_provide_nino", "care_you_provide_dob")
    val routesMap = Map(personalDetailsList map {id => (id, g1ThierPersonalDetailsRoute)} : _*)
    routesMap
  }

}
