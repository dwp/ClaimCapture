package controllers


object PreviewRouteUtils {

  def yourDetailsRoute = {
    val g1YourDetailsRoute = controllers.s2_about_you.routes.G1YourDetails.present.toString
    val g2MaritalStatusRoute = controllers.s2_about_you.routes.G2MaritalStatus.present.toString
    val g3ContactDetailsRoute = controllers.s2_about_you.routes.G3ContactDetails.present.toString
    val g4NationalityRoute = controllers.s2_about_you.routes.G4NationalityAndResidency.present.toString
    val g7OtherEEARoute = controllers.s2_about_you.routes.G7OtherEEAStateOrSwitzerland.present.toString

    val routesMap = Map("about_you_full_name" -> g1YourDetailsRoute,
                        "about_you_nino" -> g1YourDetailsRoute,
                        "about_you_dob" -> g1YourDetailsRoute,
                        "about_you_address" -> g3ContactDetailsRoute,
                        "about_you_contact" -> g3ContactDetailsRoute,
                        "about_you_marital_status" -> g2MaritalStatusRoute,
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
    val g2ThierContactDetailsRoute = controllers.s4_care_you_provide.routes.G2TheirContactDetails.present.toString
    val g7MoreAboutTheCareRoute = controllers.s4_care_you_provide.routes.G7MoreAboutTheCare.present.toString
    val g10BreaksInCareRoute = controllers.s4_care_you_provide.routes.G10BreaksInCare.present.toString

    val personalDetailsList = Seq("care_you_provide_name", "care_you_provide_nino", "care_you_provide_dob", "care_you_provide_relationship")
    val contactDetailsList = Seq("care_you_provide_address")
    val moreAboutTheCareList = Seq("care_you_provide_spent35HoursCaring")
    val breaksInCareList = Seq("care_you_provide_anyBreaks")

    val routesMap = Map(personalDetailsList map {id => (id, g1ThierPersonalDetailsRoute)} : _*) ++
                    Map(contactDetailsList map{id => (id, g2ThierContactDetailsRoute)} : _*) ++
                    Map(moreAboutTheCareList map{id => (id, g7MoreAboutTheCareRoute)} : _*) ++
                    Map(breaksInCareList map{id => (id, g10BreaksInCareRoute)} : _*)
    routesMap
  }

  def yourPartner = {
    val g1YourPartnerPersonalDetailsRoute = controllers.s3_your_partner.routes.G1YourPartnerPersonalDetails.present.toString

    val partnerDetailsList = Seq("partner_hadPartner", "partner_name", "partner_nino",
      "partner_dateOfBirth", "partner_nationality", "partner_seperated", "partner_isPersonCareFor")

    val routesMap = Map(partnerDetailsList map {id => (id, g1YourPartnerPersonalDetailsRoute)} : _*)

    routesMap
  }

}
