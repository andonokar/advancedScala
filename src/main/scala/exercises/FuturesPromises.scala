package exercises

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.Try

object FuturesPromises extends App{
  val ex1 = Future(42)
  def inSequence(f1: Future[Any], f2: Future[Any]): Future[Any] =
    f1.flatMap(_ => f2)
  def first(f1: Future[Any], f2: Future[Any]): Future[Any] = {
    val promise = Promise[Any]()
    f1.onComplete(promise.tryComplete)
    f2.onComplete(promise.tryComplete)
    promise.future
  }
  def last(f1: Future[Any], f2: Future[Any]): Future[Any] = {
    val promise = Promise[Any]()
    var counter = 0
    def checker(t: Try[Any]): Any = {
      counter += 1
      if (counter == 2)
        promise.tryComplete(t)
    }

    f1.onComplete(checker)
    f2.onComplete(checker)
    promise.future
  }

  def retryUntil[T](action: () =>  Future[T], condition: T => Boolean): Future[T] = {
    action().filter(condition).recoverWith{
      case _ => retryUntil(action, condition)
    }
  }

}
