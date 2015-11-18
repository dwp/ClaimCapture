package models.domain


import models.view.CachedClaim
import org.specs2.mock.Mockito
import play.api.cache.CacheApi
import play.api.test.Helpers._
import play.api.Play.current
import utils.ClaimEncryption

import scala.reflect.ClassTag

trait MockForm extends Mockito {
  def cache = current.injector.instanceOf[CacheApi]

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

  def extractCacheKey(result:scala.concurrent.Future[play.api.mvc.Result], sessionKey : String = CachedClaim.key) = {
    session(result).get(sessionKey).get
  }

  def getClaimFromCache(result:scala.concurrent.Future[play.api.mvc.Result], sessionKey : String = CachedClaim.key) = {
    ClaimEncryption.decrypt(cache.get[Claim](extractCacheKey(result, sessionKey)).get)
  }
}
