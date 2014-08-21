package com.derbysoft.redis.util

import java.util.concurrent._
import scala.collection.JavaConversions._
import com.derbysoft.redis.clients.common.config.Hosts
import java.util

object ExecutorUtils {

  var executor: Executor = new ThreadPoolExecutor(Hosts.redisHostsSize * 2, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue[Runnable]())

  def setExecutor(executor: Executor) {
    this.executor = executor
  }

  def batchExecute[T](tasks: util.Collection[Callable[T]]): util.List[T] = {
    batchExecute(executor, tasks)
  }

  def batchExecute[T](executor: Executor, tasks: util.Collection[Callable[T]]): util.List[T] = {
    val futures = submitTasks(executor, tasks)
    val results = new util.ArrayList[T](tasks.size())
    for (future <- futures) {
      try {
        results.add(future.get())
      } catch {
        case e: Exception =>
          println(e.getMessage, e)
          throw new RuntimeException(e)
      }
    }
    results
  }

  private def submitTasks[T](executor: Executor, tasks: util.Collection[Callable[T]]): util.List[Future[T]] = {
    val service = new ExecutorCompletionService[T](executor)
    val futures = new util.ArrayList[Future[T]](tasks.size())
    for (task <- tasks) {
      val future = service.submit(task)
      futures.add(future)
    }
    futures
  }

}
