package io.textile.textile;

import mobile.Mobile_;

/**
 * Provides access to Textile IPFS related APIs
 */
public class Ipfs extends NodeDependent {

    Ipfs(final Mobile_ node) {
        super(node);
    }

    /**
     * Fetch the IPFS peer id
     * @return The IPFS peer id of the local Textile node
     * @throws Exception The exception that occurred
     */
    public String peerId() throws Exception {
        return node.peerId();
    }

    /**
     * Open a new direct connection to a peer using an IPFS multiaddr
     * @param multiaddr Peer IPFS multiaddr
     * @return Whether the peer swarm connect was successfull
     * @throws Exception The exception that occurred
     */
    public Boolean swarmConnect(final String multiaddr) throws Exception {
        final String result = node.swarmConnect(multiaddr);
        return result.length() > 0;
    }

    /**
     * Get raw data stored at an IPFS path
     * @param path The IPFS path for the data you want to retrieve
     * @param handler An object that will get called with the resulting data and media type
     */
    public void dataAtPath(final String path, final Handlers.DataHandler handler) {
        node.dataAtPath(path, (data, media, e) -> {
            if (e != null) {
                handler.onError(e);
                return;
            }
            try {
                handler.onComplete(data, media);
            } catch (final Exception exception) {
                handler.onError(exception);
            }
        });
    }

    /**
     * Publishes a message to a given pubsub topic
     * @param topic The topic to publish to
     * @param data The payload of message to publish
     * @throws Exception The exception that occurred
     */
    public void pubsubPub(final String topic, final String data) throws Exception {
        node.ipfsPubsubPub(topic, data);
    }

    /**
     * Subscribes to messages on a given topic
     * @param topic The ipfs pubsub sub topic
     * @return A query ID that can be used to cancel the sub
     * @throws Exception The exception that occurred
     */
    public String pubsubSub(final String topic) throws Exception {
        return node.ipfsPubsubSub(topic);
    }

    /**
     * Cancel subscribe to messages on a given topic
     * @param queryId The query ID that can be used to cancel the sub
     */
    public void cancelPubsubSub(final String queryId) {
        node.cancelIpfsPubsubSub(queryId);
    }
}
