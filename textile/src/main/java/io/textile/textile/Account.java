package io.textile.textile;

import io.textile.pb.Model;
import io.textile.pb.QueryOuterClass;
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

    Model.ContactList peers() throws Exception {
        byte[] bytes = this.node.accountPeers();
        return Model.ContactList.parseFrom(bytes);
    }

    SearchHandle findThreadBackups(QueryOuterClass.ThreadBackupQuery query, QueryOuterClass.QueryOptions options) throws Exception {
        return this.node.findThreadBackups(query.toByteArray(), options.toByteArray());
    }
}
