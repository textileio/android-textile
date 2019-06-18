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
     * Get raw data stored at an IPFS path
     * @param path The IPFS path for the data you want to retrieve
     * @return The raw data
     * @throws Exception The exception that occurred
     */
    public byte[] dataAtPath(String path) throws Exception {
        return node.dataAtPath(path);
    }
}
