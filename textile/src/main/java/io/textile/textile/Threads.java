package io.textile.textile;

import io.textile.pb.Model.Thread;
import io.textile.pb.Model.ThreadList;
import io.textile.pb.Model.PeerList;
import io.textile.pb.QueryOuterClass.ThreadSnapshotQuery;
import io.textile.pb.QueryOuterClass.QueryOptions;
import io.textile.pb.View.AddThreadConfig;
import mobile.Mobile_;
import mobile.SearchHandle;

/**
 * Provides access to Textile threads related APIs
 */
public class Threads extends NodeDependent {

    Threads(final Mobile_ node) {
        super(node);
    }

    /**
     * Create a new thread
     * @param config The configuration object that describes the thread to be created
     * @return The newly created thread
     * @throws Exception The exception that occurred
     */
    public Thread add(final AddThreadConfig config) throws Exception {
        final byte[] bytes = node.addThread(config.toByteArray());
        return Thread.parseFrom(bytes);
    }

    /**
     * Update an existing thread or create it if it doesn't exist
     * @param thread The updated representation of the thread to update
     * @throws Exception The exception that occurred
     */
    public void addOrUpdate(final Thread thread) throws Exception {
        node.addOrUpdateThread(thread.toByteArray());
    }

    /**
     * Rename a thread
     * @param threadId The id of the thread to rename
     * @param name The new name for the thread
     * @throws Exception The exception that occurred
     */
    public void rename(final String threadId, final String name) throws Exception {
        node.renameThread(threadId, name);
    }

    /**
     * Get an existing thread by id
     * @param threadId The id of the thread to retrieve
     * @return The corresponding thread object
     * @throws Exception The exception that occurred
     */
    public Thread get(final String threadId) throws Exception {
        /*
         * thread throws an Exception if no thread is found.
         * We can assume that bytes is valid once we get to Thread.parseFrom(bytes)
         */
        final byte[] bytes = node.thread(threadId);
        return Thread.parseFrom(bytes);
    }

    /**
     * List all threads the local peer account participates in
     * @return An object containing a list of threads
     * @throws Exception The exception that occurred
     */
    public ThreadList list() throws Exception {
        final byte[] bytes = node.threads();
        return ThreadList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    /**
     * List all contacts that participate in a particular thread
     * @param threadId The id of the thread to query
     * @return An object containing a list of contacts
     * @throws Exception The exception that occurred
     */
    public PeerList peers(final String threadId) throws Exception {
        final byte[] bytes = node.threadPeers(threadId);
        return PeerList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    /**
     * Leave a thread
     * @param threadId The id of the thread to remove
     * @return The hash of the newly created thread leave block
     * @throws Exception The exception that occurred
     */
    public String remove(final String threadId) throws Exception {
        return node.removeThread(threadId);
    }

    /**
     * Snapshot all threads and sync them to registered cafes
     * @throws Exception The exception that occurred
     */
    public void snapshot() throws Exception {
        node.snapshotThreads();
    }

    /**
     * Searches the network for thread snapshots
     * @param query The object describing the query to execute
     * @param options Options controlling the behavior of the search
     * @return A handle that can be used to cancel the search
     * @throws Exception The exception that occurred
     */
    public SearchHandle searchSnapshots(final ThreadSnapshotQuery query, final QueryOptions options) throws Exception {
        return node.searchThreadSnapshots(query.toByteArray(), options.toByteArray());
    }
}
