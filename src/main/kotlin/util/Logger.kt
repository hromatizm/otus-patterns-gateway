package util

import org.slf4j.LoggerFactory.getLogger

inline fun <reified T : Any> T.logger() = getLogger(T::class.java)!!

inline fun <reified T : Any> T.lazyLogger() = lazy { logger() }

