package lectures.part1as

import scala.annotation.tailrec

object Recap extends App {
  val aCondition = false
  val aConditionedVal = if (aCondition) 42 else 65
  val aCodeBlock = {
    if (aCondition) 54
    56
  }
  val theUnit = println("hello, Scala")
  def aFunction(x: Int): Int = x + 1
  @tailrec
  def factorial(n: Int, accumulator: Int = 1): Int =
    if (n <= 0) accumulator
    else factorial(n-1, n * accumulator)

  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog
  trait Carnivore{
    def eat(a: Animal): Unit
  }
  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch")
  }
  val aCroc = new Crocodile
  aCroc eat aDog
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar")
  }

  abstract class MyList[+A]
  object MyList
  case class Person(name: String, age: Int)
  val throwsException = throw new RuntimeException
  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "I caughht"
  } finally {
    println("logs")
  }

  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }
  incrementer(1)

  val otherIncrementer = (x: Int) => x + 1
  List(1,2,3).map(otherIncrementer)

  val pairs = for {
    num <- List(1,2,3)
    char <- List('a', 'b', 'c')
  } yield num + "-" + char
  val aMap = Map("Daniel" -> 5555, "Jess" -> 555)
  val anOption = Some(2)
  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case _ => x + "th"
  }
  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi my name is %n"
  }
}
