package io.textile.textile;

import io.textile.pb.Model.Thread;
import io.textile.pb.Model.ThreadList;
import io.textile.pb.Model.ContactList;
import io.textile.pb.QueryOuterClass.ThreadSnapshotQuery;
import io.textile.pb.QueryOuterClass.QueryOptions;
import io.textile.pb.View.AddThreadConfig;
import mobile.Mobile_;
import mobile.SearchHandle;

public class Threads extends NodeDependent {

    Threads(Mobile_ node) {
        super(node);
    }

    public Thread add(AddThreadConfig config) throws Exception {
        byte[] bytes = node.addThread(config.toByteArray());
        return Thread.parseFrom(bytes);
    }

    public void addOrUpdate(Thread thread) throws Exception {
        node.addOrUpdateThread(thread.toByteArray());
    }

    public void rename(String threadId, String name) throws Exception {
        node.renameThread(threadId, name);
    }

    public Thread get(String threadId) throws Exception {
        /*
         * thread throws an Exception if no thread is found.
         * We can assume that bytes is valid once we get to Thread.parseFrom(bytes)
         */
        byte[] bytes = node.thread(threadId);
        return Thread.parseFrom(bytes);
    }

    public ThreadList list() throws Exception {
        byte[] bytes = node.threads();
        return ThreadList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    public ContactList peers(String threadId) throws Exception {
        byte[] bytes = node.threadPeers(threadId);
        return ContactList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    public String remove(String threadId) throws Exception {
        return node.removeThread(threadId);
    }

    public void snapshot() throws Exception {
        node.snapshotThreads();
    }

    public SearchHandle searchSnapshots(ThreadSnapshotQuery query, QueryOptions options) throws Exception {
        return node.searchThreadSnapshots(query.toByteArray(), options.toByteArray());
    }
}
