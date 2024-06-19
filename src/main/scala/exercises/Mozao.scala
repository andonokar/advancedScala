package exercises

object Mozao extends App {
  case class Person(nome: String, idade: Int)
  println(Person("Lia", 28) == Person("Lia", 28))
}
