package models.domain

import java.util.UUID._

import models.view.CachedClaim
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import play.api.cache.Cache
import play.api.test.Helpers._
import play.api.Play.current

import scala.reflect.ClassTag

trait MockForm extends Scope with Mockito {
  val claimKey = randomUUID.toString

  def mockQuestionGroup[Q <: QuestionGroup](qi: QuestionGroup.Identifier)(implicit classTag: ClassTag[Q]): Q = {
    val questionGroup = mock[Q]
    questionGroup.identifier returns qi
    questionGroup
  }

  def mockQuestionGroup(id: String): QuestionGroup = {
    val questionGroupIdentifier = mock[QuestionGroup.Identifier]
    questionGroupIdentifier.id returns id

    val questionGroup = mock[QuestionGroup]
    questionGroup.identifier returns questionGroupIdentifier

    questionGroup
  }

  def extractCacheKey(result:scala.concurrent.Future[play.api.mvc.Result], sessionKey : String = CachedClaim.key) = session(result).get(sessionKey).get

  def getClaimFromCache(result:scala.concurrent.Future[play.api.mvc.Result], sessionKey : String = CachedClaim.key) = Cache.getAs[Claim](extractCacheKey(result, sessionKey)).get
}