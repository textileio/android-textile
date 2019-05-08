package io.textile.textile;

import io.textile.pb.Model.Contact;
import io.textile.pb.QueryOuterClass.QueryOptions;

import mobile.Mobile_;
import mobile.SearchHandle;

public class Account extends NodeDependent {

    Account(Mobile_ node) {
        super(node);
    }

    public String address() {
        return node.address();
    }

    public byte[] decrypt(byte[] bytes) throws Exception {
        return node.decrypt(bytes);
    }

    public void delete(String repoPath, String address) throws Exception {
        node.deleteAccount(repoPath, address);
    }

    public Contact contact() throws Exception {
        byte[] bytes = node.accountContact();
        return Contact.parseFrom(bytes);
    }

    public byte[] encrypt(byte[] bytes) throws Exception {
        return node.encrypt(bytes);
    }

    public String seed() {
        return node.seed();
    }

    public SearchHandle sync(QueryOptions options) throws Exception {
        return node.syncAccount(options.toByteArray());
    }
}
