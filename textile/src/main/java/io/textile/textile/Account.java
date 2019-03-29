package io.textile.textile;

import io.textile.pb.Model.ContactList;
import io.textile.pb.QueryOuterClass.ThreadBackupQuery;
import io.textile.pb.QueryOuterClass.QueryOptions;

import mobile.Mobile_;
import mobile.SearchHandle;

public class Account extends NodeDependant {

    Account(Mobile_ node) {
        super(node);
    }

    public String address() {
        return this.node.address();
    }

    public String seed() {
        return this.node.seed();
    }

    public byte[] encrypt(byte[] bytes) throws Exception {
        return this.node.encrypt(bytes);
    }

    public byte[] decrypt(byte[] bytes) throws Exception {
        return this.node.decrypt(bytes);
    }

    ContactList peers() throws Exception {
        byte[] bytes = this.node.accountPeers();
        return ContactList.parseFrom(bytes);
    }

    SearchHandle findThreadBackups(ThreadBackupQuery query, QueryOptions options) throws Exception {
        return this.node.findThreadBackups(query.toByteArray(), options.toByteArray());
    }
}
