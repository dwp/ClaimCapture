package views

import org.specs2.mutable.Specification
import scala.xml.NodeSeq
import models.domain._

class ProgressBarSpec extends Specification {

  val name = ".name"

  def xml(sectionIdentifier: Section.Identifier, claim: Claim) = {
    val resultHtml: play.api.templates.Html = views.html.progressBar(sectionIdentifier)(claim)
    scala.xml.XML.loadString(resultHtml.body)
  }

  def findNodeWithClass(nodes: NodeSeq, value: String) = {
    nodes.filter(node => node.attribute("class").exists(c => c.text == value))
  }

  def findNodeWithText(nodes: NodeSeq, value: String) = nodes.filter(node => node.text == value)

  "ProgressBar" should {
    "show sections" in {
      val listItems = xml(CarersAllowance, Claim()) \ "ol" \ "li"

      listItems.size shouldEqual(10)
    }

    "mark current section active" in {
      val listItems = xml(AboutYou, Claim()) \\ "ol" \\ "li"
      val activeNode = findNodeWithClass(listItems, "active")

      activeNode.text must contain(AboutYou.id + name)
    }

    "mark preceding sections completed" in {
      val listItems = xml(YourPartner, Claim()) \\ "ol" \\ "li"

      val completedNodes = findNodeWithClass(listItems, "complete")

      completedNodes.head.text must contain(CarersAllowance.id + name)
      completedNodes.last.text must contain(AboutYou.id + name)
    }

    "remove hidden section" in {
      val listItems = xml(TimeSpentAbroad, Claim()) \\ "ol" \\ "li"
      val yourPartnerNode = findNodeWithText(listItems, YourPartner.id + name)
      yourPartnerNode.length must beEqualTo(1)

      val updatedListItems = xml(TimeSpentAbroad, Claim().hideSection(YourPartner)) \\ "ol" \\ "li"

      val hiddenNode = findNodeWithText(updatedListItems, YourPartner.id + name)

      hiddenNode.length must beEqualTo(0)
    }

    "handle employment sections as one" in {
      "when both are visible and not completed" in {
        val claim = Claim().showHideSection(true, Employed).showHideSection(true, SelfEmployment)
        val listItems = xml(Education, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(10)
        val employmentNode = listItems(6)
        employmentNode.text must contain(Employed.id + name)
      }
      "when both are visible and Employed is current section" in {
        val claim = Claim().showHideSection(true, Employed).showHideSection(true, SelfEmployment)
        val listItems = xml(Employed, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(10)
        val activeNode = findNodeWithClass(listItems, "active")
        activeNode.text must contain(Employed.id + name)
      }
      "when both are visible and SelfEmployed is current section" in {
        val claim = Claim().showHideSection(true, Employed).showHideSection(true, SelfEmployment)
        val listItems = xml(SelfEmployment, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(10)
        val activeNode = findNodeWithClass(listItems, "active")
        activeNode.text must contain(Employed.id + name)
      }
      "when both are visible and both are completed" in {
        val claim = Claim().showHideSection(true, Employed).showHideSection(true, SelfEmployment)
        val listItems = xml(OtherMoney, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(10)
        val completedNodes = findNodeWithClass(listItems, "complete")
        completedNodes.last.text must contain(Employed.id + name)
      }
      "when only Employed is visible" in {
        val claim = Claim().showHideSection(true, Employed).showHideSection(false, SelfEmployment)
        val listItems = xml(Education, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(10)
      }
      "when only Employed is visible and current section" in {
        val claim = Claim().showHideSection(true, Employed).showHideSection(false, SelfEmployment)
        val listItems = xml(Employed, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(10)
        val activeNode = findNodeWithClass(listItems, "active")
        activeNode.text must contain(Employed.id + name)
      }
      "when only Employed is visible and completed" in {
        val claim = Claim().showHideSection(true, Employed).showHideSection(false, SelfEmployment)
        val listItems = xml(OtherMoney, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(10)
        val completedNodes = findNodeWithClass(listItems, "complete")
        completedNodes.last.text must contain(Employed.id + name)
        val activeNode = findNodeWithClass(listItems, "active")
        activeNode.text must contain(OtherMoney.id + name)
      }
      "when only Self Employment is visible" in {
        val claim = Claim().showHideSection(false, Employed).showHideSection(true, SelfEmployment)
        val listItems = xml(Education, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(10)
      }
      "when only Self Employment is visible and current section" in {
        val claim = Claim().showHideSection(false, Employed).showHideSection(true, SelfEmployment)
        val listItems = xml(SelfEmployment, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(10)
        val activeNode = findNodeWithClass(listItems, "active")
        activeNode.text must contain(Employed.id + name)
      }
      "when only Self Employment is visible and completed" in {
        val claim = Claim().showHideSection(false, Employed).showHideSection(true, SelfEmployment)
        val listItems = xml(OtherMoney, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(10)
        val completedNodes = findNodeWithClass(listItems, "complete")
        completedNodes.last.text must contain(Employed.id + name)
      }
      "when both are hidden" in {
        val claim = Claim().showHideSection(false, Employed).showHideSection(false, SelfEmployment)
        val listItems = xml(OtherMoney, claim) \\ "ol" \\ "li"
        listItems.size shouldEqual(9)
      }
    }
  }
}