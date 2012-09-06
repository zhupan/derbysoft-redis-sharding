package com.derbysoft.redis.util

import java.util.ArrayList
import java.util.Collection
import java.util.List
import java.util.concurrent._
import scala.collection.JavaConversions._

object ExecutorUtils {

  val executor = Executors.newCachedThreadPool()

  def batchExecute[T](tasks: Collection[Callable[T]]): List[T] = {
    batchExecute(executor, tasks)
  }

  def batchExecute[T](executor: Executor, tasks: Collection[Callable[T]]): List[T] = {
    val futures = submitTasks(executor, tasks)
    val results = new ArrayList[T](tasks.size())

    for (future <- futures) {
      try {
        results.add(future.get())
      } catch {
        case e: Exception => {
          println(e.getMessage)
        }
      }
    }
    results
  }


  private def submitTasks[T](executor: Executor, tasks: Collection[Callable[T]]): List[Future[T]] = {
    val service = new ExecutorCompletionService[T](executor)
    val futures = new ArrayList[Future[T]](tasks.size())
    for (task <- tasks) {
      val future = service.submit(task)
      futures.add(future)
    }
    futures
  }

}
