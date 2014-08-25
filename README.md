

# Cassandra Subsystem

## Prerequisites

### Wildfly 8.1.0

Get and install Wildfly 8.1.0: http://download.jboss.org/wildfly/8.1.0.Final/wildfly-8.1.0.Final.zip

It's currently been tested against WF 8.0.1 and the default server configuration (standalone-cassdandra.xml) is configured for WF 8.
But apart from that there should be no reason to not use it on WF 9.

### Patched Cassandra 2.0.6 version
Checkout the patched cassandra version from https://github.com/heiko-braun/cassandra and install it to your local maven repo:

 `ant  mvn-install`

Why not use a stock Cassandra version?

The reason is simple: Cassandra isn't designed to be used an embedded library.
In particular the CassandraDaemon and some of the core services make heavy use 'System.exit()' calls.
But in order to embed Cassandra as a managed service, we had to remove some of the code
 that would otherwise break the service contracts in Wildlfy.


## Build & Install

Build the top level project:

`mvn clean install`

This will also create a cassandra-module.zip, that can be installed on Wildfly:

`unzip target/cassandra-module.zip -d $WILDFLY_HOME`

This will add an additional module that contains the cassandra extension and subsystem:

`modules/system/layers/base/org/wildfly/cassandra/main/`

## Server Configuration Profiles

When installing the cassandra-module.zip it create a custom server profile for both standalone and domain mode
($WILDFLY_HOME/standalone/configuration/standalone-cassandra.xml & $WILDFLY_HOME/domain/configuration/cassandra-domain.xml)
that can be used to start a pre-configured Wildfly instance:

### Standalone Mode

`./bin/standalone.sh -c standalone-cassandra.xml -b 127.0.0.1`

### Domain Mode

`./bin/domain.sh --domain-config=cassandra-domain.xml --host-config=cassandra-host.xml -b 127.0.0.1`

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

## Open Issues

We track ideas and open issues in the Wiki. Contributions are welcome:

https://github.com/heiko-braun/wildfly-cassandra/wiki/Todo

## Get In touch

The best to reach out and discuss the cassandra subsystem is currently the Wildfly mailing list and/or the Chat Room:

- Mailing List: https://lists.jboss.org/mailman/listinfo/wildfly-dev
- IRC: irc://freenode.org/#wildfly

## License

- http://www.apache.org/licenses/LICENSE-2.0.html

## Resources
- https://docs.jboss.org/author/display/WFLY8/Documentation
- http://www.datastax.com/documentation/cassandra/2.0/cassandra/gettingStartedCassandraIntro.html

