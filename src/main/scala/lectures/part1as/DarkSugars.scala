package lectures.part1as

import scala.util.Try

object DarkSugars extends App {
  def singleArgMethod(arg: Int): String = s"$arg little ducks"
  val description = singleArgMethod{
    42
  }
  val aTryInstance = Try {
    throw new RuntimeException
  }
  List(1,2,3).map{x => x + 1}

  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x
  }
  val aFunkyInstance: Action = (x: Int) => x
  aFunkyInstance.act(2)

  val aThread = new Thread(() => println("hello scala"))

  abstract class type1 {
    def a: Int = 23
    def f(a: Int): Unit
  }
  val instance: type1 = (a: Int) => println(a)
  instance.f(instance.a)

  val prependedList = 2 :: List(3,4)
  List(3,4).::(2)
  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this
  }
  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  class TeenGirl(name: String){
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }
  val lia = new TeenGirl("Lia")
  lia `and then said` "Scala is so sweet"

  class Composite[A, B]
  val composite: Int Composite String = ???
  class -->[A, B]
  val towards: Int --> String = ???

  val anArray = Array(1,2,3)
  anArray(2) = 7 // anArray.update(2, 7)
  // remember apply and update

  class Mutable {
    private var internalMember: Int = 0
    def member: Int = internalMember
    def member_=(value: Int): Unit = internalMember = value
  }
  val container = new Mutable
  container.member = 42
  container.member
}
