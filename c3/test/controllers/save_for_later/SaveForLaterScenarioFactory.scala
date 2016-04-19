package controllers.save_for_later

import utils.pageobjects.TestData

object SaveForLaterScenarioFactory {
  def AboutYouData() = {
    val claim=new TestData()
    claim.AboutYouTitle = "Mr"
    claim.AboutYouFirstName = "John"
    claim.AboutYouSurname = "Appleseed"
    claim.AboutYouDateOfBirth = "03/04/1950"
    claim.AboutYouNationalityAndResidencyNationality = "British"
    claim.AboutYouNationalityAndResidencyResideInUK = "Yes"
    claim.AboutYouNINO = "AB123456C"
    claim.AboutYouAddress = "101 Clifton Street&Blackpool"
    claim.AboutYouPostcode = "FY1 2RW"
    claim.HowWeContactYou = "01772 888901"

    claim
  }

  def WithNoEmailSet() = {
    val claim=AboutYouData()
    claim.AboutYouWantsEmailContact = "No"

    claim
  }

  def WithEmailSet() = {
    val claim=AboutYouData()
    claim.AboutYouWantsEmailContact = "Yes"
    claim.AboutYouMail="cg@bt.com"
    claim.AboutYouMailConfirmation="cg@bt.com"

    claim
  }

  def PartialNationalityData() = {
    val claim=WithEmailSet()
    claim.AboutYouNationalityAndResidencyNationality = "Another nationality"

    claim
  }

  def French() = {
    val claim=WithEmailSet()
    claim.AboutYouNationalityAndResidencyNationality = "Another nationality"
    claim.AboutYouNationalityAndResidencyActualNationality = "French"
    claim.AboutYouNationalityAndResidencyAlwaysLivedInUK = "Yes"
    claim.AboutYouNationalityAndResidencyTrip52Weeks = "No"
    claim
  }

  def Spanish() = {
    val claim=WithEmailSet()
    claim.AboutYouNationalityAndResidencyNationality = "Another nationality"
    claim.AboutYouNationalityAndResidencyActualNationality = "Spanish"
    claim.AboutYouNationalityAndResidencyAlwaysLivedInUK = "Yes"
    claim.AboutYouNationalityAndResidencyTrip52Weeks = "No"
    claim
  }

  def ResumePageData() = {
    val claim=new TestData()
    claim.AboutYouFirstName = "John"
    claim.AboutYouSurname = "Green"
    claim.AboutYouDateOfBirth = "01/01/1970"
    claim.AboutYouNINO = "AB123456D"

    claim
  }

  def BadResumePageData()={
    val claim=new TestData()
    claim.AboutYouFirstName = "Wrong"
    claim.AboutYouSurname = "Green"
    claim.AboutYouDateOfBirth = "01/01/1970"
    claim.AboutYouNINO = "AB123456D"

    claim
  }
}
