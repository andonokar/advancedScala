package exercises

object PartialFunctionExercise extends App {
  val aPartialFunction = new PartialFunction[Int, Int] {
    override def isDefinedAt(x: Int): Boolean = x==1 || x==2 || x==5
    override def apply(v1: Int): Int = if (v1==1) 42 else if (v1==2) 56 else 999
  }
scala.io.Source.stdin.getLines().foreach{
  case "hello" => println("Hello from DumbBot")
  case "goodbye" => println("goodbye from dumbbot")
  case "lia" => println("eu te amo muito meu amorzinho princesa da minha vida")
  case _ => println("nao entendi sou burro")
}
}
