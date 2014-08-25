#!/bin/sh

set -o xtrace

rm -f bin/nodetool
rm -rf modules/system/layers/base/org/wildfly/extension/cassandra/
rm -f standalone/configuration/standalone-cassandra.xml
rm -f domain/configuration/cassandra-domain.xml
rm -f domain/configuration/cassandra-host.xml