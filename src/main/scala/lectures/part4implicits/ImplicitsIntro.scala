package lectures.part4implicits

import scala.language.implicitConversions

object ImplicitsIntro extends App{
  val pair = "Daniel" -> "555"
  case class Person(name: String) {
    def greet = s"Hi, I'm $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)
  println("Peter".greet)

  def increment(x: Int)(implicit amount: Int) = x + amount
  implicit val defaultAmount: Int = 10
  increment(3)
}
