package lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success}
import scala.concurrent.duration._

object FuturesPromises extends App{
  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }
  val aFuture = Future {
    calculateMeaningOfLife
  }

  println(aFuture.value)  // Option[Try[Int]]
  println("waiting the future")
  aFuture.onComplete {
    case Success(value) => println(s"the meaning of life is $value")
    case Failure(exception) => println(s"I have failed with $exception")
  }

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit =
      println(s"$name poking ${anotherProfile.name}")
  }

  object SocialNetwork {
    val names: Map[String, String] = Map("zuck" -> "Mark", "bill" -> "Bill", "dummy" -> "Dummy")
    val friends: Map[String, String] = Map("zuck" -> "bill")
    val random = new Random()

    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }
  val mark = SocialNetwork.fetchProfile("zuck")
  mark.onComplete{
    case Success(markProfile) =>
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete{
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(exception) => exception.printStackTrace()
      }
    case Failure(exception) => exception.printStackTrace()
  }

  val nameOnTheWall = mark.map(profile => profile.name)
  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  val zucksBestFriend = marksBestFriend.filter(profile => profile.name.startsWith("z"))

  for {
    mark <- SocialNetwork.fetchProfile("zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover{
    case e: Throwable => Profile("hahaha", "forever alone")
  }
  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith{
    case e: Throwable => SocialNetwork.fetchProfile("tentei de novo")
  }
  val fallBackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("tentei de novo"))

  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)
  object BankingApp {
    val name = "Lia banking"

    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(500)
      User(name)
    }
    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "Sucess")
    }
    def purchase(username: String, item: String, cost: Double, merchantName: String): String = {
      val transactionStatus = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status
      Await.result(transactionStatus, 2.seconds)
    }
  }

  val promise = Promise[Int]()
  val future = promise.future
  future.onComplete{
    case Success(value) => println(s"eu consumi o valor $value")
  }
  val producer = new Thread(() => {
    println("crunching numbers")
    Thread.sleep(1000)
    promise.success(42)
    println("producer done")
  })
}
