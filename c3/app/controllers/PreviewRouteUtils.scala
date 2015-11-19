package controllers

import controllers.s_breaks.GBreaksInCare


object PreviewRouteUtils {

  def yourDetailsRoute = {
    val gYourDetailsRoute = controllers.s_about_you.routes.GYourDetails.present.toString
    val gMaritalStatusRoute = controllers.s_about_you.routes.GMaritalStatus.present.toString
    val gContactDetailsRoute = controllers.s_about_you.routes.GContactDetails.present.toString
    val gNationalityRoute = controllers.s_about_you.routes.GNationalityAndResidency.present.toString
    val gOtherEEARoute = controllers.s_about_you.routes.GOtherEEAStateOrSwitzerland.present.toString

    val routesMap = Map("about_you_full_name" -> gYourDetailsRoute,
                        "about_you_dob" -> gYourDetailsRoute,
                        "about_you_address" -> gContactDetailsRoute,
                        "about_you_contact" -> gContactDetailsRoute,
                        "about_you_email" -> gContactDetailsRoute,
                        "about_you_marital_status" -> gMaritalStatusRoute,
                        "about_you_claimDate" -> controllers.s_claim_date.routes.GClaimDate.present.toString,
                        "about_you_nationality" -> gNationalityRoute,
                        "about_you_residence" -> gNationalityRoute,
                        "about_you_abroad" -> controllers.s_about_you.routes.GAbroadForMoreThan52Weeks.present.toString,
                        "about_you_eeaGuardQuestion" -> gOtherEEARoute,
                        "about_you_benefitsFromEEA" -> gOtherEEARoute,
                        "about_you_workingForEEA" -> gOtherEEARoute)

    routesMap

  }


  def employmentRoute = {
    val employmentRoute = controllers.s_self_employment.routes.GEmployment.present.toString
    val jobsRoute           = controllers.s_employment.routes.GBeenEmployed.present.toString
    val aboutSelfEmployment = controllers.s_self_employment.routes.GAboutSelfEmployment.present.toString
    val additionalInfoRoute = controllers.s_employment.routes.GEmploymentAdditionalInfo.present.toString
    val routesMap = Map(
      "employment_been_employed_since" -> employmentRoute,
      "employment_jobs" -> jobsRoute,
      "self_employment_been_self_employed" -> employmentRoute,
      "self_employment_nature_of_business" -> aboutSelfEmployment,
      "employment_additional_info" -> additionalInfoRoute
    )
    routesMap
  }

  def otherMoneyRoute = {
    val gAboutOtherMoneyRoute = controllers.s_other_money.routes.GAboutOtherMoney.present.toString
    val idList = Seq("other_money_anyPaymentsSinceClaimDate", "other_money_statutoryPay", "other_money_otherStatutoryPay")

    val routesMap = Map(idList map {id => (id, gAboutOtherMoneyRoute)} : _*)

    routesMap
  }

  def educationRoute = {
    val gYourCourseDetailsRoute = controllers.s_education.routes.GYourCourseDetails.present.toString
    val idList = Seq("education_beenInEducationSinceClaimDate", "education_courseTitle", "education_nameOfSchool",
      "education_nameOfTutor", "education_contactNumber", "education_startEndDates")

    val routesMap = Map(idList map {id => (id, gYourCourseDetailsRoute)} : _*)

    routesMap
  }

  def breaks = {

    val gBreaksInCareRoute = controllers.s_breaks.routes.GBreaksInCare.present.toString

    val breaksInCareList = Seq("care_you_provide_anyBreaks")

    val routesMap = Map(breaksInCareList map{id => (id,gBreaksInCareRoute)}: _*)

    routesMap
  }

  def careYouProvide = {
    val gTheirPersonalDetailsRoute = controllers.s_care_you_provide.routes.GTheirPersonalDetails.present.toString
    val gMoreAboutTheCareRoute = controllers.s_care_you_provide.routes.GMoreAboutTheCare.present.toString

    val personalDetailsList = Seq("care_you_provide_name", "care_you_provide_dob", "care_you_provide_relationship")
    val contactDetailsList = Seq("care_you_provide_address")
    val moreAboutTheCareList = Seq("care_you_provide_spent35HoursCaring")

    val routesMap = Map(personalDetailsList map {id => (id, gTheirPersonalDetailsRoute)} : _*) ++
                    Map(contactDetailsList map{id => (id, gTheirPersonalDetailsRoute)} : _*) ++
                    Map(moreAboutTheCareList map{id => (id, gMoreAboutTheCareRoute)} : _*)
    routesMap
  }

  def yourPartner = {
    val gYourPartnerPersonalDetailsRoute = controllers.s_your_partner.routes.GYourPartnerPersonalDetails.present.toString

    val partnerDetailsList = Seq("partner_hadPartner", "partner_name",
      "partner_dateOfBirth", "partner_nationality", "partner_seperated", "partner_isPersonCareFor")

    val routesMap = Map(partnerDetailsList map {id => (id, gYourPartnerPersonalDetailsRoute)} : _*)

    routesMap
  }

  def bankDetailsRoute = {
    val gBankDetailsRoute = controllers.s_pay_details.routes.GHowWePayYou.present.toString

    val bankDetailsList = Seq("bank_details")
    val routesMap = Map(bankDetailsList map {id => (id, gBankDetailsRoute)} : _*)
    routesMap
  }

  def additionalInfoRoute = {
    val gAdditionalInfoRoute = controllers.s_information.routes.GAdditionalInfo.present.toString

    val additionalInfoList = Seq("additional_info", "additional_info_welsh")

    val routesMap = Map(additionalInfoList map {id => (id, gAdditionalInfoRoute)} : _*)
    routesMap
  }

}
