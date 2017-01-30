sbt '; set javaOptions += "-Dakka.remote.netty.tcp.port=2551" ; runMain com.tsykul.search.server.SearchServer'&

sbt '; set javaOptions += "-Dakka.remote.netty.tcp.port=2552" ; runMain com.tsykul.search.server.SearchServer'&

sbt '; set javaOptions += "-Dakka.remote.netty.tcp.port=2553" ; runMain com.tsykul.search.server.SearchApi'&