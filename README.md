

# Cassandra Subsystem for the Wildfly Application Server

## Prerequisites

- Wildfly 8.0.1: http://download.jboss.org/wildfly/8.1.0.Final/wildfly-8.1.0.Final.zip
- Patched cassandra 2.0.6 version: https://github.com/heiko-braun/cassandra

## Build & Install

Build the top level project:

`mvn clean install`

This will also create a cassandra-module.zip, that can be installed on Wildfly:

`unzip target/cassandra-module.zip -d $WILDFLY_HOME`

This will add an additional module that contains the cassandra extension and subsystem:

`modules/system/layers/base/org/wildfly/cassandra/main/`

## Wildfly Configuration Profile

When installing the cassandra-module.zip it create a custom server profile ($WILDFLY_HOME/standalone/configuration/cassandra.xml)
that can be used to start a stripped wildfly instance:

`./bin/standalone.sh -c cassandra.xml`

## Cassandra Configuration

The cassandra service can be configured like any other wildfly resource:

<pre>
`[standalone@localhost:9990 /] /subsystem=cassandra/cluster=WildflyCluster:read-resource
 {
     "outcome" => "success",
     "result" => {
         "authenticator" => "AllowAllAuthenticator",
         "authorizer" => "AllowAllAuthorizer",
         "broadcast-address" => expression "${jboss.default.multicast.address:230.0.0.4}",
         "client-encryption-enabled" => false,
         "commitlog-sync" => "periodic",
         "commitlog-sync-period-in-ms" => 10000,
         "debug" => false,
         "endpoint-snitch" => "SimpleSnitch",
         "hinted-handoff-enabled" => true,
         "internode-authenticator" => "org.apache.cassandra.auth.AllowAllInternodeAuthenticator",
         "listen-address" => expression "${jboss.bind.address:127.0.0.1}",
         "native-transport-port" => 9042,
         "num-tokens" => 256,
         "partitioner" => "org.apache.cassandra.dht.Murmur3Partitioner",
         "request-scheduler" => "org.apache.cassandra.scheduler.NoScheduler",
         "rpc-port" => 9160,
         "seed-provider-class-name" => "org.apache.cassandra.locator.SimpleSeedProvider",
         "seed-provider-seeds" => expression "${jboss.bind.address:127.0.0.1}",
         "server-encryption-enabled" => false,
         "start-native-transport" => true,
         "start-rpc" => true
     }
 }`
</pre>

## License

- http://www.apache.org/licenses/LICENSE-2.0.html

## Resources
- https://docs.jboss.org/author/display/WFLY8/Documentation
- http://www.datastax.com/documentation/cassandra/2.0/cassandra/gettingStartedCassandraIntro.html

