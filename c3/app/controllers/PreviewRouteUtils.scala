package controllers


object PreviewRouteUtils {

  def yourDetailsRoute = {
    val gYourDetailsRoute = controllers.s_about_you.routes.GYourDetails.present.toString
    val gMaritalStatusRoute = controllers.s_about_you.routes.GMaritalStatus.present.toString
    val gContactDetailsRoute = controllers.s_about_you.routes.GContactDetails.present.toString
    val gNationalityRoute = controllers.s_about_you.routes.GNationalityAndResidency.present.toString
    val gOtherEEARoute = controllers.s_about_you.routes.GOtherEEAStateOrSwitzerland.present.toString

    val routesMap = Map("about_you_full_name" -> gYourDetailsRoute,
                        "about_you_nino" -> gYourDetailsRoute,
                        "about_you_dob" -> gYourDetailsRoute,
                        "about_you_address" -> gContactDetailsRoute,
                        "about_you_contact" -> gContactDetailsRoute,
                        "about_you_marital_status" -> gMaritalStatusRoute,
                        "about_you_claimDate" -> controllers.s_claim_date.routes.GClaimDate.present.toString,
                        "about_you_nationality" -> gNationalityRoute,
                        "about_you_abroad" -> controllers.s_about_you.routes.GAbroadForMoreThan52Weeks.present.toString,
                        "about_you_benefitsFromEEA" -> gOtherEEARoute,
                        "about_you_workingForEEA" -> gOtherEEARoute)

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
    val gYourPartnerPersonalDetailsRoute = controllers.s_your_partner.routes.GYourPartnerPersonalDetails.present.toString

    val partnerDetailsList = Seq("partner_hadPartner", "partner_name", "partner_nino",
      "partner_dateOfBirth", "partner_nationality", "partner_seperated", "partner_isPersonCareFor")

    val routesMap = Map(partnerDetailsList map {id => (id, gYourPartnerPersonalDetailsRoute)} : _*)

    routesMap
  }

}
