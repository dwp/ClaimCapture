package models.claim

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import services.submission.ClaimSubmission
import helpers.ClaimBuilder._
import play.api.test.WithApplication
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory
import scala.xml.Elem
import org.specs2.execute.PendingUntilFixed

class ClaimSubmissionSpec extends Specification with Tags with PendingUntilFixed {

  private def updateClaim(claim: Claim) = {
    claim.update(aboutYou.yourDetails)
      .update(aboutYou.claimDate)
      .update(aboutYou.contactDetails)
      
      .update(yourPartner.yourPartnerPersonalDetails)
      .update(yourPartner.yourPartnerContactDetails)
      .update(yourPartner.moreAboutYourPartner)
      .update(yourPartner.personYouCareFor.get)
      
      .update(careYouProvide.theirPersonalDetails)
      .update(careYouProvide.theirContactDetails)
      .update(careYouProvide.moreAboutThePerson)
      .update(careYouProvide.representatives)
      .update(careYouProvide.previousCarerContactDetails.get)
      .update(careYouProvide.previousCarerPersonalDetails.get)
      .update(careYouProvide.moreAboutTheCare)
      .update(careYouProvide.oneWhoPays.get)
      .update(careYouProvide.contactDetailsPayingPerson.get)
      .update(careYouProvide.breaksInCare)

      .update(payDetails.howWePayYou)
      .update(payDetails.bankBuildingSocietyDetails)

      .update(consentAndDeclaration.additionalInfo)
      .update(consentAndDeclaration.consent)
      .update(consentAndDeclaration.disclaimer)
      .update(consentAndDeclaration.declaration)
  }
  "Claim Submission" should {
    "build and confirm normal AboutYou input" in new WithApplication {
      val claim = updateClaim(Claim())
      val claimSub = ClaimSubmission(claim, "TY6TV9G")

      val claimXml = claimSub.buildDwpClaim

      (claimXml \\ "Claimant" \\ "Title").text mustEqual yourDetails.title
      (claimXml \\ "Claimant" \\ "OtherNames").text mustEqual s"${yourDetails.firstName} "
      (claimXml \\ "Claimant" \\ "OtherSurnames").text mustEqual yourDetails.otherSurnames.get
      (claimXml \\ "Claimant" \\ "DateOfClaim").text mustEqual claimDate.dateOfClaim.toXmlString
      (claimXml \\ "Claimant" \\ "Address" \\ "PostCode").text mustEqual contactDetails.postcode.get
      (claimXml \\ "Claimant" \\ "HomePhoneNumber").text mustEqual contactDetails.mobileNumber.getOrElse("") // holds mobile
    }

    "build and confirm contains YourPartner input" in new WithApplication {
      val claim = updateClaim(Claim())
      val claimSub = ClaimSubmission(claim, "TY6TV9G")
      
      val claimXml = claimSub.buildDwpClaim

      (claimXml \\ "Partner" \\ "NationalityPartner").text mustEqual yourPartnerPersonalDetails.nationality.get
      (claimXml \\ "Partner" \\ "Surname").text mustEqual yourPartnerPersonalDetails.surname
      (claimXml \\ "Partner" \\ "OtherNames").text mustEqual s"${yourPartnerPersonalDetails.firstName} ${yourPartnerPersonalDetails.middleName.getOrElse("")}"
      (claimXml \\ "Partner" \\ "Title").text mustEqual yourPartnerPersonalDetails.title
      (claimXml \\ "Partner" \\ "DateOfBirth").text mustEqual yourPartnerPersonalDetails.dateOfBirth.toXmlString
      (claimXml \\ "Partner" \\ "NationalInsuranceNumber").text mustEqual yourPartnerPersonalDetails.nationalInsuranceNumber.get.toXmlString
      (claimXml \\ "Partner" \\ "Address" \\ "PostCode").text mustEqual yourPartnerContactDetails.postcode.get
      (claimXml \\ "Partner" \\ "RelationshipStatus" \\ "JoinedHouseholdAfterDateOfClaim").text mustEqual moreAboutYourPartner.startedLivingTogether.get.answer
      (claimXml \\ "Partner" \\ "RelationshipStatus" \\ "JoinedHouseholdDate").text mustEqual moreAboutYourPartner.startedLivingTogether.get.date.get.toXmlString
      (claimXml \\ "Partner" \\ "RelationshipStatus" \\ "SeparatedFromPartner").text mustEqual moreAboutYourPartner.separated.answer
      (claimXml \\ "Partner" \\ "RelationshipStatus" \\ "SeparationDate").text mustEqual ""
    }

    "validate a good claim" in new WithApplication {
      val claim = updateClaim(Claim())


      val claimSub = ClaimSubmission(claim, "TY6TV9G")

      val claimXml = claimSub.buildDwpClaim

      val fullXml = buildFullClaim(claimXml)

      val validator = XmlValidatorFactory.buildCaValidator()

      validator.validate(fullXml.buildString(stripComments = true)) must beTrue
    }

    "validate a bad claim" in new WithApplication {
      val claim = updateClaim(Claim())

      val claimSub = ClaimSubmission(claim, "878786876786Y6TV9G")

      val claimXml = claimSub.buildDwpClaim

      val fullXml = buildFullClaim(claimXml)

      val validator = XmlValidatorFactory.buildCaValidator()

      validator.validate(fullXml.buildString(stripComments = true)) must beFalse
    }
  } section "externalDependency"

  def buildFullClaim(claimXml:Elem) = {

    <DWPBody xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666"
                   xmlns="http://www.govtalk.gov.uk/dwp/ca/claim"
                   xmlns:gds="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails"
                   xmlns:dc="http://purl.org/dc/elements/1.1/"
                   xmlns:dcq="http://purl.org/dc/terms/"
                   xmlns:gms="http://www.govtalk.gov.uk/CM/gms"
                   xmlns:dsig="http://www.w3.org/2000/09/xmldsig#"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.govtalk.gov.uk/dwp/ca/claim file:/Users/jmi/Temp/dwp-ca-claim-v1_10.xsd">
      <DWPEnvelope>
        <DWPCAHeader>
        <TestMessage>5</TestMessage>
        <Keys>
          <Key type="}~e"></Key>
          <Key type="Z}"></Key>
        </Keys>
        <Language>en</Language>
        <DefaultCurrency>GBP</DefaultCurrency>
        <Manifest>
          <Reference>
            <Namespace>http://PtqKCMVh/</Namespace>
            <SchemaVersion></SchemaVersion>
            <TopElementName>FZXic.rwPpxsw5wsX</TopElementName>
          </Reference>
          <Reference>
            <Namespace>http://jwJGvJlj/</Namespace>
            <SchemaVersion></SchemaVersion>
            <TopElementName>vaN1Eh5z61pekYlfOv-vP0sGy</TopElementName>
          </Reference>
        </Manifest>
        <TransactionId>{(claimXml \\ "DWPCAClaim" \ "@id").text}</TransactionId>
      </DWPCAHeader>
      {claimXml}
    </DWPEnvelope>
    </DWPBody>
  }
}
