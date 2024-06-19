package exercises

import scala.collection.mutable
import scala.util.Random

object Threads extends App {
  def myProducerConsumer(c: Int, p: Int, cap: Int): Unit = {
    val buffer = new mutable.Queue[Int]
    val capacity = cap
    val consumer = (1 to c).map(_ => new Thread(() => {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) { // while
            println("consumer empty")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"consumer $x")
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    }))

    val producer = (1 to p).map(_ => new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) { // while
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
    }))
    consumer.foreach(_.start())
    producer.foreach(_.start())
  }


  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          while (buffer.isEmpty) {
            println(s"consumer $id sees buffer empty")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"consumer $id consume $x")
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {4
          while (buffer.size == capacity) {
            println("buffer is full, waiting")
            buffer.wait()
          }
          println(s"producer $id producing $i")
          buffer.enqueue(i)
          buffer.notify()
          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }
  def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
    val buffer = new mutable.Queue[Int]
    val capacity = 3
    (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
  }
}


