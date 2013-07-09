package views

import org.specs2.mutable.Specification
import scala.xml.NodeSeq
import models.domain._

class ProgressBarSpec extends Specification {

  val progressBar = ".progressBar"

  def xml(sectionID:String, claim:Claim) = {
    val resultHtml: play.api.templates.Html = views.html.progressBar(sectionID)(claim)
    scala.xml.XML.loadString(resultHtml.body)
  }

  def findNodeWithClass(nodes: NodeSeq, value: String) = {
    nodes.filter(node => node.attribute("class").exists(c => c.text == value))
  }

  def findNodeWithText(nodes: NodeSeq, value: String) = {
    nodes.filter(node => node.text == value)
  }

  "ProgressBar" should {
    "show sections" in {
      val listItems = xml(CarersAllowance.id, Claim()) \ "ol" \ "li"

      listItems.size must beGreaterThan(0)
    }

    "mark current section active" in {
      val listItems = xml(AboutYou.id, Claim()) \\ "ol" \\ "li"
      val activeNode = findNodeWithClass(listItems, "active")

      activeNode.text mustEqual AboutYou.id + progressBar
    }

    "mark preceding sections completed" in {
      val listItems = xml(YourPartner.id, Claim()) \\ "ol" \\ "li"

      val completedNodes = findNodeWithClass(listItems, "complete")

      completedNodes.head.text mustEqual CarersAllowance.id + progressBar
      completedNodes.last.text mustEqual AboutYou.id + progressBar
    }

    "remove hidden section" in {
      val listItems = xml(TimeSpentAbroad.id, Claim()) \\ "ol" \\ "li"
      val yourPartnerNode = findNodeWithText(listItems, YourPartner.id + progressBar)
      yourPartnerNode.length must beEqualTo(1)

      val updatedListItems =  xml(TimeSpentAbroad.id, Claim().hideSection(YourPartner.id)) \\ "ol" \\ "li"

      val hiddenNode = findNodeWithText(updatedListItems, YourPartner.id + progressBar)

      hiddenNode.length must beEqualTo(0)

    }
  }

}
