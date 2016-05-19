package xml.claim

import models.domain.{Claim, _}
import models.view.CachedClaim
import models.yesNo.YesNoWithText
import org.specs2.mutable._
import utils.WithApplication
import play.api.i18n._
import play.api.Play.current

class OtherInformationSpec extends Specification {
  val welsh="No"
  val claim = new Claim(CachedClaim.key, uuid = "1234")

  section("unit")
  "Incomes section xml generation" should {
    "Generate correct xml items for Welsh and Additional Info" in new WithApplication {
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

      val addinfo = new AdditionalInfo(YesNoWithText(answer = "Yes", text = Some("Some additional info")), welsh)
      val xml = OtherInformation.xml(claim + addinfo)
      (xml \\ "OtherInformation" \\ "WelshCommunication" \ "QuestionLabel").text mustEqual messagesApi("welshCommunication")
      (xml \\ "OtherInformation" \\ "WelshCommunication" \ "Answer").text shouldEqual "No"
      (xml \\ "OtherInformation" \\ "AdditionalInformation" \ "QuestionLabel").text mustEqual messagesApi("anythingElse.answer")
      (xml \\ "OtherInformation" \\ "AdditionalInformation" \ "Answer").text shouldEqual "Yes"
      (xml \\ "OtherInformation" \\ "AdditionalInformation" \\ "Why" \ "QuestionLabel").text mustEqual messagesApi("anythingElse.text")
      (xml \\ "OtherInformation" \\ "AdditionalInformation" \\ "Why" \ "Answer").text mustEqual messagesApi("Some additional info")
    }

    // Live issue 28/04/2016 and 04/05/2016 when we get the following xml failing inspection ( because no additional Yes/No answer in the xml, instead we got self closing </Answer>
    // Schema is
    // <xs:element minOccurs="0" name="WelshCommunication" type="QuestionTextType"/>
    // <xs:element minOccurs="0" maxOccurs="1" name="AdditionalInformation" type="QuestionYesNoWhyThreeThousandType"/>
    //
    // ( Missing <WelshCommunication> and empty <AdditionalInformation><Answer> )
    // <OtherInformation>
    // <AdditionalInformation><QuestionLabel>Do you want to tell us any additional information about your claim?</QuestionLabel><Answer/></AdditionalInformation>
    // </OtherInformation>
    //
    // The self closing <Answer/> is written into suspicious messages in ingressservice, but is fully formatted when leaving c3 <Answer></Answer>

    // Answer Yes but addinfo-answer=None should not be possible lets see what xml we get out. Trying to force an error i.e broken xml
    // And see if we get a self closing answer ie. <answer/> rather than <answer></answer>
    "Flush out live issue try generate xml items for bad additional info" in new WithApplication {
      val addinfo = new AdditionalInfo(YesNoWithText(answer = "Yes", text = None), welsh)
      val xml = OtherInformation.xml(claim + addinfo)
      // Actually we dont get the <Why><QuestionLabel>...<Answer>...</Why> block
      (xml \\ "OtherInformation" \\ "AdditionalInformation" \ "Answer").text mustEqual "Yes"
      (xml \\ "OtherInformation" \\ "AdditionalInformation" \\ "Why" \ "Answer").text mustEqual ""
    }

    "Flush out live issue try generate xml items for bad additional info text entered" in new WithApplication {
      val addinfo = new AdditionalInfo(YesNoWithText(answer = "Yes", text = Some("<>!@£#$%`\"")), welsh)

      val xml = OtherInformation.xml(claim + addinfo)
      (xml \\ "OtherInformation" \\ "AdditionalInformation" \\ "Why" \ "Answer").text mustEqual "<>!@£#$%`\""
    }

    // Additional Info Page somehow skipped thus blank ... we get xml as per live problem ( which saves in suspicious messages as <Answer/>
    "Mimic live issue when user skips addinfo page" in new WithApplication {
      val addinfo = new AdditionalInfo()
      val xml = OtherInformation.xml(claim + addinfo)
      println(xml)
      // <OtherInformation>
      // <AdditionalInformation><QuestionLabel>Do you want to tell us any additional information about your claim?</QuestionLabel><Answer></Answer></AdditionalInformation>
      // </OtherInformation>
      (xml \\ "OtherInformation" \\ "AdditionalInformation" \ "Answer").length mustEqual 1
      (xml \\ "OtherInformation" \\ "AdditionalInformation" \ "Answer").text mustEqual ""
      (xml \\ "OtherInformation" \\ "AdditionalInformation" \ "Why").length mustEqual 0
    }
  }
  section("unit")

}
