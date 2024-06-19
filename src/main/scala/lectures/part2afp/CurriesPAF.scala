package lectures.part2afp

object CurriesPAF extends App{
  val superAdder: Int => Int => Int = x => y => x + y
  def currierAdder(x: Int)(y: Int): Int = x + y

  val add4: Int => Int = currierAdder(4)
  val add5 = currierAdder(5) _
  // lifting = ETA-EXPANSION

  def inc(x: Int) = x + 1
  List(1,2,3).map(inc) // ETA-EXPANSION

  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddFunction(x: Int)(y: Int) = x + y

  val add7 = curriedAddFunction(7) _
  val add7_2: Int => Int = curriedAddFunction(7)
  val add7_3 = (x: Int) => simpleAddFunction(7, x)
  val add7_4 = (x: Int) => simpleAddMethod(7, x)
  val add7_5 = simpleAddFunction.curried(7)
  val add7_6 = curriedAddFunction(7)(_)
  val add7_7 = curriedAddFunction(7)(_)
  val add7_8 = simpleAddMethod(7, _: Int)
  val add7_9 = simpleAddFunction(7, _)

  def concatenator(a: String, b: String, c: String) = a+b+c
  val insertName = concatenator("Hello ", _: String, " how are you?")
  println(insertName("Lia"))
  val fill = concatenator("hello ", _, _)
}
