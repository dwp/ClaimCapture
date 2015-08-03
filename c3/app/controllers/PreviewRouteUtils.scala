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
    val gYourCourseDetailsRoute = controllers.s_education.routes.GYourCourseDetails.present.toString
    val idList = Seq("education_beenInEducationSinceClaimDate", "education_courseTitle", "education_nameOfSchool",
      "education_nameOfTutor", "education_contactNumber", "education_startEndDates")

    val routesMap = Map(idList map {id => (id, gYourCourseDetailsRoute)} : _*)

    routesMap
  }

  def careYouProvide = {
    val gTheirPersonalDetailsRoute = controllers.s_care_you_provide.routes.GTheirPersonalDetails.present.toString
    val gTheirContactDetailsRoute = controllers.s_care_you_provide.routes.GTheirContactDetails.present.toString
    val gMoreAboutTheCareRoute = controllers.s_care_you_provide.routes.GMoreAboutTheCare.present.toString
    val gBreaksInCareRoute = controllers.s_care_you_provide.routes.GBreaksInCare.present.toString

    val personalDetailsList = Seq("care_you_provide_name", "care_you_provide_nino", "care_you_provide_dob", "care_you_provide_relationship")
    val contactDetailsList = Seq("care_you_provide_address")
    val moreAboutTheCareList = Seq("care_you_provide_spent35HoursCaring")
    val breaksInCareList = Seq("care_you_provide_anyBreaks")

    val routesMap = Map(personalDetailsList map {id => (id, gTheirPersonalDetailsRoute)} : _*) ++
                    Map(contactDetailsList map{id => (id, gTheirContactDetailsRoute)} : _*) ++
                    Map(moreAboutTheCareList map{id => (id, gMoreAboutTheCareRoute)} : _*) ++
                    Map(breaksInCareList map{id => (id, gBreaksInCareRoute)} : _*)
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
