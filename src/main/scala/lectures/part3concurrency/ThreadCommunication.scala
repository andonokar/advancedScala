package lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {
  /*
  The producer-consumer problem
  producer -> [ ? ] -> consumer
   */

  class SimpleContainer {
    private var value = 0
    def isEmpty: Boolean = value == 0
    def set(valueNew: Int): Unit = value = valueNew
    def get: Int = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("waiting")
      while(container.isEmpty) {
        println("consumer waiting")
      }
      println(s"consumed ${container.get}")
    })
  val producer = new Thread(() => {
    println("producing")
    Thread.sleep(500)
    val value = 42
    println(s"produced $value")
    container.set(value)

  })

  consumer.start()
  producer.start()
  }
//  naiveProdCons()

  def smartProdCons(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("waiting")
      container.synchronized{
        container.wait()
      }
      println(s"consumed ${container.get}")
    })
  val producer = new Thread(() => {
    println("producing")
    Thread.sleep(2000)
    val value = 42
    container.synchronized{
      println(s"produced $value")
      container.set(value)
      container.notify()
    }
  })
  consumer.start()
    producer.start()
  }


  def prodConsLargeBuffer(): Unit = {
    val buffer = new mutable.Queue[Int]
    val capacity = 3
    val consumer = new Thread(() => {
      val random = new Random()
      while(true) {
        buffer.synchronized{
          if (buffer.isEmpty) {
            println("consumer empty")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"consumer $x")
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while(true) {
        buffer.synchronized{
          if(buffer.size == capacity) {
            println("buffer is full, waiting")
            buffer.wait()
          }
          println(s"producing $i")
          buffer.enqueue(i)
          buffer.notify()
          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    })
    consumer.start()
    producer.start()
  }
//  prodConsLargeBuffer()

}
