package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.{MultiLineAddress, DayMonthYear, NationalInsuranceNumber}
import controllers.Mappings.{yes, no}
import models.yesNo.{YesNoWithDropDownAndText, YesNoWithDropDown, YesNoWithDate}

class CareeSpec extends Specification with Tags {

  "Caree" should {

    "generate <Caree> xml" in {

      val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))
      val dateOfBirth = DayMonthYear(Some(3), Some(4), Some(1950))
      val claimDate = DayMonthYear(Some(10), Some(10), Some(2013))
      val theirPersonalDetails = TheirPersonalDetails(title = "title", firstName = "firstName", middleName = Some("middleName"), surname = "surname", nationalInsuranceNumber = Some(nationalInsuranceNr), dateOfBirth = dateOfBirth)
      val theirContactDetails = TheirContactDetails(address = MultiLineAddress(Some("line1")), postcode = Some("postcode"), phoneNumber = Some("020-12302312"))
      val moreAboutThePerson = MoreAboutThePerson(relationship = "son", claimedAllowanceBefore = yes)
      val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaring = yes, spent35HoursCaringBeforeClaim = YesNoWithDate(no, Some(claimDate)), hasSomeonePaidYou = yes)
      val representatives = RepresentativesForPerson(youAct = YesNoWithDropDown("youSign", Some("youActing")), someoneElseAct = YesNoWithDropDownAndText(Some("someoneElseSign"), Some("dropDown"), Some("text")))

      val claim = Claim().update(theirPersonalDetails).update(theirContactDetails).update(moreAboutThePerson).update(moreAboutTheCare).update(representatives)

      val xml = Caree.xml(claim)

      (xml \\ "Surname").text shouldEqual theirPersonalDetails.surname
      (xml \\ "OtherNames").text must contain(theirPersonalDetails.firstName)
      (xml \\ "OtherNames").text must contain(theirPersonalDetails.middleName.get)
      (xml \\ "Title").text shouldEqual theirPersonalDetails.title
      (xml \\ "DateOfBirth").text shouldEqual dateOfBirth.`yyyy-MM-dd`
      (xml \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify
      (xml \\ "Address" \\ "Line").theSeq(0).text shouldEqual theirContactDetails.address.lineOne.get
      (xml \\ "Address" \\ "PostCode").text shouldEqual theirContactDetails.postcode.get
      (xml \\ "DaytimePhoneNumber" \\ "Number").text shouldEqual theirContactDetails.phoneNumber.get
      (xml \\ "RelationToClaimant").text shouldEqual moreAboutThePerson.relationship
      (xml \\ "Cared35hours").text shouldEqual yes
      (xml \\ "CanSomeoneElseSign").text shouldEqual representatives.someoneElseAct.answer.get
      (xml \\ "CanClaimantSign").text shouldEqual representatives.youAct.answer
      (xml \\ "BreaksSinceClaim").text shouldEqual no
      (xml \\ "Cared35hoursBefore").text shouldEqual moreAboutTheCare.spent35HoursCaringBeforeClaim.answer
      (xml \\ "PaidForCaring").text shouldEqual moreAboutTheCare.hasSomeonePaidYou
      (xml \\ "PayReceived").text must not(beEmpty)
      (xml \\ "ClaimedPreviously").text shouldEqual moreAboutThePerson.claimedAllowanceBefore
      (xml \\ "PreviousClaimant").text must not(beEmpty)
    }

    "<ClaimantActingType> should contain yes for" in {
      "<ParentOrGuardian> when claimer act as Parent or guardian" in {
        val representatives = RepresentativesForPerson(youAct = YesNoWithDropDown(yes, Some("guardian")))
        val xml = Caree.claimantActingType(Claim().update(representatives))
        (xml \\ "ParentOrGuardian").text shouldEqual yes
      }
      "<PowerOfAttorney> when claimer act as Attorney" in {
        val representatives = RepresentativesForPerson(youAct = YesNoWithDropDown(yes, Some("attorney")))
        val xml = Caree.claimantActingType(Claim().update(representatives))
        (xml \\ "PowerOfAttorney").text shouldEqual yes
      }
      "<Appointee> when claimer act as Appointee" in {
        val representatives = RepresentativesForPerson(youAct = YesNoWithDropDown(yes, Some("appointee")))
        val xml = Caree.claimantActingType(Claim().update(representatives))
        (xml \\ "Appointee").text shouldEqual yes
      }
      "<JudicialFactor> when claimer act as Judicial factor" in {
        val representatives = RepresentativesForPerson(youAct = YesNoWithDropDown(yes, Some("judicial")))
        val xml = Caree.claimantActingType(Claim().update(representatives))
        (xml \\ "JudicialFactor").text shouldEqual yes
      }
      "<Receiver> when claimer act as Deputy" in {
        val representatives = RepresentativesForPerson(youAct = YesNoWithDropDown(yes, Some("deputy")))
        val xml = Caree.claimantActingType(Claim().update(representatives))
        (xml \\ "Receiver").text shouldEqual yes
      }
      "<Receiver> when claimer act as Curator bonis" in {
        val representatives = RepresentativesForPerson(youAct = YesNoWithDropDown(yes, Some("curator")))
        val xml = Caree.claimantActingType(Claim().update(representatives))
        (xml \\ "Receiver").text shouldEqual yes
      }
    }

    "skip <ClaimantActingType> if claimer not acting for person they care for" in {
      val representatives = RepresentativesForPerson(youAct = YesNoWithDropDown(no, None))
      val xml = Caree.claimantActingType(Claim().update(representatives))
      xml.text must beEmpty
    }

    "<BreaksSinceClaim> xml should contain" in {
      "yes when claimer has breaks and NOT spent 35 hours caring BEFORE claim date" in {
        val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(no, None))
        val breaks = BreaksInCare().update(Break())

        val xml = Caree.breaksSinceClaim(Claim().update(moreAboutTheCare).update(breaks))
        xml.text shouldEqual yes
      }
      "no when claimer has breaks and spent 35 hours caring BEFORE claim date" in {
        val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(yes, None))
        val xml = Caree.breaksSinceClaim(Claim().update(moreAboutTheCare))
        xml.text shouldEqual no
      }
      "no when claimer has NO breaks and NOT spent 35 hours caring BEFORE claim date" in {
        val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(no, None))
        val xml = Caree.breaksSinceClaim(Claim().update(moreAboutTheCare))
        xml.text shouldEqual no
      }
    }

    "<BreaksBeforeClaim> xml should contain" in {
      "yes when claimer has breaks and spent 35 hours caring BEFORE claim date" in {
        val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(yes, None))
        val breaks = BreaksInCare().update(Break())

        val xml = Caree.breaksBeforeClaim(Claim().update(moreAboutTheCare).update(breaks))
        xml.text shouldEqual yes
      }
      "no when claimer has no breaks and spent 35 hours caring BEFORE claim date" in {
        val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(yes, None))
        val xml = Caree.breaksBeforeClaim(Claim().update(moreAboutTheCare))
        xml.text shouldEqual no
      }
    }

    "skip <BreaksBeforeClaim> xml if claimer did NOT spent 35 hours caring BEFORE claim date" in {
      val xml = Caree.breaksBeforeClaim(Claim())
      xml.text must beEmpty
    }

    "skip <DateStartedCaring> xml if claimer did NOT spent 35 hours caring BEFORE claim date" in {
      val xml = Caree.dateStartedCaring(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(no, None)))
      xml.text must beEmpty
    }

    "<DateStartedCaring> should contain date if claimer spent 35 hours caring BEFORE claim date" in {
      val startedCaringDate = DayMonthYear(Some(5), Some(5), Some(2000))
      val xml = Caree.dateStartedCaring(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(yes, Some(startedCaringDate))))
      xml.text shouldEqual startedCaringDate.`yyyy-MM-dd`
    }

    "skip <PayReceived> xml if claimer has NOT received payments" in {
      val moreAboutTheCare = MoreAboutTheCare(hasSomeonePaidYou = no)

      val xml = Caree.payReceived(Claim().update(moreAboutTheCare))
      xml.text must beEmpty
    }

    "generate <PayReceived> xml if claimer has received payments" in {
      val moreAboutTheCare = MoreAboutTheCare(hasSomeonePaidYou = yes)
      val oneWhoPays = OneWhoPaysPersonalDetails(organisation = Some("organisation"), title = Some("title"), firstName = Some("firstName"), middleName = Some("middleName"), surname = Some("surname"), amount = Some("505.5"), startDatePayment = Some(DayMonthYear(Some(3), Some(4), Some(2012))))
      val contactDetailsPayingPerson = ContactDetailsOfPayingPerson(address = Some(MultiLineAddress(Some("line1"))), postcode = Some("postcode"))
      val claim = Claim().update(moreAboutTheCare).update(oneWhoPays).update(contactDetailsPayingPerson)
      val xml = Caree.payReceived(claim)

      (xml \\ "PayerName").text must contain(oneWhoPays.organisation.get)
      (xml \\ "PayerName").text must contain(oneWhoPays.title.get)
      (xml \\ "PayerName").text must contain(oneWhoPays.firstName.get)
      (xml \\ "PayerName").text must contain(oneWhoPays.middleName.get)
      (xml \\ "PayerName").text must contain(oneWhoPays.surname.get)
      (xml \\ "PayerAddress" \\ "Line").theSeq(0).text shouldEqual contactDetailsPayingPerson.address.get.lineOne.get
      (xml \\ "PayerAddress" \\ "PostCode").text shouldEqual contactDetailsPayingPerson.postcode.get
      (xml \\ "Payment" \\ "Amount").text shouldEqual oneWhoPays.amount.get
      (xml \\ "DatePaymentStarted").text shouldEqual oneWhoPays.startDatePayment.get.`yyyy-MM-dd`
    }

    "skip <PreviousClaimant> xml if NOT claimed allowance before" in {
      val moreAboutThePerson = MoreAboutThePerson(claimedAllowanceBefore = no)

      val xml = Caree.previousClaimant(Claim().update(moreAboutThePerson))
      xml.text must beEmpty
    }

    "generate <PreviousClaimant> xml if claimed allowance before" in {
      val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))
      val dateOfBirth = DayMonthYear(Some(3), Some(4), Some(1950))
      val moreAboutThePerson = MoreAboutThePerson(claimedAllowanceBefore = yes)
      val previousCarerPersonalDetails = PreviousCarerPersonalDetails(firstName = Some("firstName"), middleName = Some("middleName"), surname = Some("surname"), nationalInsuranceNumber=Some(nationalInsuranceNr), dateOfBirth=Some(dateOfBirth))
      val previousCarerContactDetails = PreviousCarerContactDetails(address = Some(MultiLineAddress(Some("line1"))), postcode = Some("postcode"))
      val claim = Claim().update(moreAboutThePerson).update(previousCarerPersonalDetails).update(previousCarerContactDetails)

      val xml = Caree.previousClaimant(claim)
      (xml \\ "Surname").text shouldEqual previousCarerPersonalDetails.surname.get
      (xml \\ "OtherNames").text must contain(previousCarerPersonalDetails.firstName.get)
      (xml \\ "OtherNames").text must contain(previousCarerPersonalDetails.middleName.get)
      (xml \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify
      (xml \\ "Address" \\ "Line").theSeq(0).text shouldEqual previousCarerContactDetails.address.get.lineOne.get
      (xml \\ "Address" \\ "PostCode").text shouldEqual previousCarerContactDetails.postcode.get
    }
  } section "unit"


  //  <Caree>
  //    <Surname>surname</Surname>
  //    <OtherNames>firstName middleName</OtherNames>
  //    <Title>title</Title>
  //    <DateOfBirth>1950-04-03</DateOfBirth>
  //    <NationalInsuranceNumber>VO123456D</NationalInsuranceNumber>
  //    <Address>
  //      <gds:Line>line1</gds:Line>
  //      <gds:Line></gds:Line>
  //      <gds:Line></gds:Line>
  //      <gds:PostCode>postcode</gds:PostCode>
  //    </Address>
  //    <ConfirmAddress>yes</ConfirmAddress>
  //    <HomePhoneNumber/>
  //    <DaytimePhoneNumber>
  //      <Number>020-12302312</Number>
  //      <Qualifier/>
  //    </DaytimePhoneNumber>
  //    <RelationToClaimant>son</RelationToClaimant>
  //    <Cared35hours>yes</Cared35hours>
  //    <CanCareeSign>yes</CanCareeSign>
  //    <CanSomeoneElseSign>someoneElseSign</CanSomeoneElseSign>
  //    <CanClaimantSign>youSign</CanClaimantSign>
  //    <ClaimantActingType>
  //      <ParentOrGuardian></ParentOrGuardian>
  //      <PowerOfAttorney></PowerOfAttorney>
  //      <Appointee></Appointee>
  //      <JudicialFactor></JudicialFactor>
  //      <Receiver></Receiver>
  //    </ClaimantActingType>
  //    <BreaksSinceClaim>no</BreaksSinceClaim>
  //    <Cared35hoursBefore>no</Cared35hoursBefore>
  //    <PaidForCaring>yes</PaidForCaring>
  //    <PayReceived>
  //      <PayerName></PayerName>
  //      <PayerAddress>
  //        <gds:Line></gds:Line>
  //        <gds:Line></gds:Line>
  //        <gds:Line></gds:Line>
  //        <gds:PostCode></gds:PostCode>
  //      </PayerAddress>
  //      <ConfirmAddress>yes</ConfirmAddress>
  //      <Payment>
  //        <Currency>GBP</Currency>
  //        <Amount></Amount>
  //      </Payment>
  //      <DatePaymentStarted></DatePaymentStarted>
  //    </PayReceived>
  //    <ClaimedPreviously>yes</ClaimedPreviously>
  //    <PreviousClaimant>
  //      <Surname></Surname>
  //      <OtherNames></OtherNames>
  //      <DateOfBirth></DateOfBirth>
  //      <NationalInsuranceNumber></NationalInsuranceNumber>
  //      <Address>
  //        <gds:Line></gds:Line>
  //        <gds:Line></gds:Line>
  //        <gds:Line></gds:Line>
  //        <gds:PostCode></gds:PostCode>
  //      </Address>
  //    </PreviousClaimant>
  //  </Caree>


}
