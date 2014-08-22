/*
 * JBoss, Home of Professional Open Source
 *  Copyright ${year}, Red Hat, Inc., and individual contributors
 *  by the @authors tag. See the copyright.txt in the distribution for a
 *  full listing of individual contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wildfly.cassandra.extension;

/**
 * @author Heiko Braun
 * @since 20/08/14
 */
public class CassandraModel {
    public static final String CLUSTER = "cluster";
    public static final String NAME = "name";
    public static final String NUM_TOKENS = "num-tokens";
    public static final String DEBUG = "debug";
    public static final String HINTED_HANDOFF_ENABLED = "hinted-handoff-enabled";
    public static final String AUTHENTICATOR = "authenticator";
    public static final String AUTHORIZER = "authorizer";
    public static final String PARTITIONER = "partitioner";
    public static final String SEED_PROVIDER = "seed-provider-class-name";
    public static final String SEEDS = "seed-provider-seeds";
    public static final String LISTEN_ADDRESS = "listen-address";
    public static final String BROADCAST_ADDRESS = "broadcast-address";
    public static final String START_NATIVE_TRANSPORT = "start-native-transport";
    public static final String START_RPC = "start-rpc";
    public static final String NATIVE_TRANSPORT_PORT = "native-transport-port";
    public static final String RPC_PORT = "rpc-port";
    public static final String INTERNODE_AUTHENTICATOR = "internode-authenticator";
    public static final String DATA_FILE_DIR = "data-file-directories";
    public static final String SAVED_CACHES_DIR = "saved-caches-directory";
    public static final String COMMIT_LOG_DIR = "commitlog-directory";
    public static final String COMMIT_LOG_SYNC = "commitlog-sync";
    public static final String COMMIT_LOG_SYNC_PERIOD = "commitlog-sync-period-in-ms";
    public static final String ENDPOINT_SNITCH = "endpoint-snitch";
    public static final String REQUEST_SCHEDULER = "request-scheduler";
    public static final String SERVER_ENCRYPTION = "server-encryption-enabled";
    public static final String CLIENT_ENCRYPTION = "client-encryption-enabled";
}
