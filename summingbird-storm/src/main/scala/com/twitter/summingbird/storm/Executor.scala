/*
 Copyright 2013 Twitter, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.twitter.summingbird.storm

import com.twitter.summingbird._
import com.twitter.summingbird.chill.ChillExecutionConfig
import com.twitter.scalding.Args


/**
 * @author Ian O Connell
 */

trait StormExecutionConfig extends ChillExecutionConfig[Storm] {
}

object Executor {
  def apply(inargs: Array[String], generator: Args => StormExecutionConfig) {
    val args = Args(inargs)
    val config = generator(args)

    val storm = if (args.boolean("local")) {
        Storm.local(config.getNamedOptions)
    } else {
        Storm.remote(config.getNamedOptions)
    }

    storm
        .withRegistrars(config.registrars)
        .withConfigUpdater { c => c.updated(config.transformConfig(c.toMap)) }
        .run(config.graph, config.name)
  }
}
