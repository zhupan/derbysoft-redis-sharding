package com.derbysoft.redis.util

import java.util.{ArrayList, Collection, List}
import java.util.concurrent._
import scala.collection.JavaConversions._
import com.derbysoft.redis.clients.common.config.HostKey
import org.apache.commons.logging.LogFactory

object ExecutorUtils {

  var executor: Executor = new ThreadPoolExecutor(HostKey.redisHostsSize * 2, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue[Runnable]())

  private val logger = LogFactory.getLog(this.getClass)

  def setExecutor(executor: Executor) {
    this.executor = executor
  }

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
          logger.warn(e.getMessage, e)
          throw new RuntimeException(e)
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
