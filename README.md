

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

`/subsystem=cassandra/cluster=<CLUSTER_NAME>`

##Cassandra Documentation

http://www.datastax.com/documentation/cassandra/2.0/cassandra/gettingStartedCassandraIntro.html