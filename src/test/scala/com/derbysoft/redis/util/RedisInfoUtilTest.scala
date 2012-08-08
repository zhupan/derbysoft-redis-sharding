package com.derbysoft.redis.util

import junit.framework.{Assert, TestCase, TestSuite, Test}

object RedisInfoUtilTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[RedisInfoUtilTest]);
    suite
  }

  def main(args: Array[String]) {
    junit.textui.TestRunner.run(suite);
  }
}

class RedisInfoUtilTest extends TestCase("app") {

  def testGetUsedMemory = {
    Assert.assertEquals(2642120616L, RedisInfoUtil.getUsedMemory("redis_version:2.4.4\nredis_git_sha1:00000000\nredis_git_dirty:0\narch_bits:64\nmultiplexing_api:epoll\nprocess_id:5867\nuptime_in_seconds:10443025\nuptime_in_days:120\nlru_clock:1351140\nused_cpu_sys:12837.35\nused_cpu_user:3578.75\nused_cpu_sys_children:79526.82\nused_cpu_user_children:557872.88\nconnected_clients:2\nconnected_slaves:0\nclient_longest_output_list:0\nclient_biggest_input_buf:0\nblocked_clients:0\nused_memory:2642120616\nused_memory_human:2.46G\nused_memory_rss:2710675456\nused_memory_peak:2686031592\nused_memory_peak_human:2.50G\nmem_fragmentation_ratio:1.03\nmem_allocator:jemalloc-2.2.5\nloading:0\naof_enabled:1\nchanges_since_last_save:2903\nbgsave_in_progress:0\nlast_save_time:1334716964\nbgrewriteaof_in_progress:0\ntotal_connections_received:911\ntotal_commands_processed:657104225\nexpired_keys:0\nevicted_keys:0\nkeyspace_hits:325845275\nkeyspace_misses:1680506\npubsub_channels:0\npubsub_patterns:0\nlatest_fork_usec:412722\nvm_enabled:0\nrole:slave\naof_current_size:4914987620\naof_base_size:4223502477\naof_pending_rewrite:0\naof_buffer_length:0\naof_pending_bio_fsync:0\nmaster_host:10.152.55.11\nmaster_port:6379\nmaster_link_status:up\nmaster_last_io_seconds_ago:7\nmaster_sync_in_progress:0\ndb0:keys=3979958,expires=3979958"))
  }

}
