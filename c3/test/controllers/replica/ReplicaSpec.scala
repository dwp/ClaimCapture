package controllers.replica

import org.specs2.mutable.Specification
import play.api.Play._
import play.api.routing.Router
import utils.pageobjects.replica.ReplicaPage
import utils.{WithJsBrowser}
import utils.pageobjects.PageObjects

import scala.collection.mutable.ListBuffer

class ReplicaSpec extends Specification {
  section("unit", "replica")

  "Replica page" should {
    "present a list of links with expected number of sections and links" in new WithJsBrowser with PageObjects {
      val page = ReplicaPage(context)
      page goToThePage()

      val sections = browser.$(".section")
      val links = browser.$("a")
      sections.size mustEqual (17)
      links.size mustEqual (32)
    }

    // We need to ensure that if any changes are made to c3 application with new urls, that the replica is considered.
    // If not needed in replica then they should be added to the exclusion list below.
    "contain links for all claim page urls except those listed as exclusions" in new WithJsBrowser with PageObjects {
      val urlStripPattern = "^.*:[^/]*/"

      // Get all sections and links from replica page
      val page = ReplicaPage(context)
      page goToThePage()
      val links = browser.$("a")

      val router = current.injector.instanceOf[Router]
      val pathList = replicaUrlsLessExclusions(router.documentation.map(_._2))
      var badUrlList = ListBuffer[String]()
      for (routepath <- pathList) {
        var urlUsedInReplica = false
        for (fullhref <- links.getAttributes("href").toArray) {
          val href = fullhref.toString.replaceAll(urlStripPattern, "/")
          if (routepath.equals(href)) {
            urlUsedInReplica = true
          }
        }
        if (!urlUsedInReplica) {
          println("ERROR - Application route not found in Replica or exclusion list:" + routepath)
          badUrlList += routepath
        }
      }
      badUrlList must beEmpty
    }

    /* The following urls are present in the c3 application but not used in replica so excluded */
    def replicaUrlsLessExclusions(pathList: Seq[String]): Seq[String] = {
      pathList
        .filterNot(value => value.equals("/"))
        .filterNot(value => value.contains("/circumstances"))
        .filterNot(value => value.contains("/circs"))
        .filterNot(value => value.contains("/feedback"))
        .filterNot(value => value.contains("$id"))
        .filterNot(value => value.contains("$file"))
        .filterNot(value => value.contains("/save"))
        .filterNot(value => value.contains("/resume"))
        .filterNot(value => value.contains("error"))
        .filterNot(value => value.contains("timeout"))
        .filterNot(value => value.contains("/replica"))
        .filterNot(value => value.contains("/report"))
        .filterNot(value => value.contains("2015"))
        .filterNot(value => value.contains("/cookies"))
        .filterNot(value => value.contains("/claim-help"))
        .filterNot(value => value.contains("/change-language"))
        .filterNot(value => value.contains("/preview"))
        .filterNot(value => value.contains("submitting"))
        .filterNot(value => value.contains("/thankyou"))
        .filterNot(value => value.contains("/back-button"))
        .filterNot(value => value.contains("delete"))
        .filterNot(value => value.contains("/breaks/break"))
        .filterNot(value => value.contains("/employment/job-details"))
        .filterNot(value => value.contains("/employment/last-wage"))
        .filterNot(value => value.contains("/employment/about-expenses"))
        .distinct
    }
    section("unit", "replica")
  }
}
