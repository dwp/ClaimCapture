import play.api.mvc.{AnyContent, Action}

case class Navigation(actions: List[Action[AnyContent]] = List()) {
  def track(action: => Action[AnyContent]): Navigation = Navigation(actions :+ action)

  def backup: Navigation = Navigation(if (actions.size > 1) actions.dropRight(1) else actions)

  def current: Action[AnyContent] = actions.last

  def previous: Action[AnyContent] = backup.current
}