package controllers.submission

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithApplication

class WebServiceSubmitterSpec extends Specification with Tags {
    "Web service submitter" should {
        "return valid xml formatting can screw up the values" in new WithApplication {
          pending
//          val submitter = new WebServiceSubmitter(null,null)
//          val xml = submitter.pollXml("id","url")
//          val result = (xml \\ "pollEndpoint").text
//          result must equalTo("url")
        }
    }
}
