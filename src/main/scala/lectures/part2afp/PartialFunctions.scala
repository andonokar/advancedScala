package lectures.part2afp

object PartialFunctions extends App {
  val aFunction = (x: Int) => x + 1
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  println(aPartialFunction(2))
  println(aPartialFunction.isDefinedAt(67))

  val lifted = aPartialFunction.lift
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }
  println(pfChain(45))

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }
  val aMappedList = List(1,2,3).map{
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
}
