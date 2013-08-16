package models.view

import org.specs2.mutable.Specification
import play.api.mvc.{AnyContent, Action}
import play.api.mvc.Results._
import play.api.test.FakeRequest
import play.api.test.Helpers._

class NavigationSpec extends Specification {
  "Navigation" should {
    "track an action" in {
      val navigation = Navigation() track Action { Ok }

      val action: Action[AnyContent] = navigation.current
      val result = action(FakeRequest())
      status(result) shouldEqual OK
    }

    "go back to previous action" in {
      val action1 = Action { Ok }
      val action2 = Action { Ok }
      val navigation = Navigation() track action1 track action2

      navigation.current shouldEqual action2

      val action: Action[AnyContent] = navigation.previous
      action shouldEqual action1

      val result = action(FakeRequest())
      status(result) shouldEqual OK
    }

    "should give original back from only one existing action" in {
      val action = Action { Ok }
      val navigation = Navigation() track action

      val currentAction = navigation.previous
      currentAction shouldEqual action
    }
  }
}