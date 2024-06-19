package exercises

object Curry extends App {
  val processor  = (x: List[Double], y: String) => x.foreach(num => println(y.format(num)))
  val option1 = processor(_, "%4.2f")
  val option2 = processor(_, "%8.6f")
  val option3 = processor(_, "%14.12f")
  option3(List(1.0,2.0,3.0,4.0))

  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1
  def method = 42
  def parenMethod() = 42

  byName(23)
  byName(method)
  byName(parenMethod())
  byName(parenMethod) // beware
  // byName(() -> 42)
  byName((() => 42)())
  // byName(parenMethod _)
  // byFunction(45)
  // byFunction(method)
  byFunction(parenMethod)
  byFunction(() => 46)
  byFunction(parenMethod _)
}
