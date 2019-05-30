package io.textile.textile;

import io.textile.pb.Model.Contact;
import io.textile.pb.QueryOuterClass.QueryOptions;

import mobile.Mobile_;
import mobile.SearchHandle;

/**
 * Provides access to Textile account related APIs
 */
public class Account extends NodeDependent {

    Account(Mobile_ node) {
        super(node);
    }

    /**
     * @return The address of the Textile account
     */
    public String address() {
        return node.address();
    }

    /**
     * @return The seed of the Textile account
     */
    public String seed() {
        return node.seed();
    }

    /**
     * Encrypt raw data with the account private key
     * @param data The data to encrypt
     * @return The encryped data
     * @throws Exception The exception that occurred
     */
    public byte[] encrypt(byte[] bytes) throws Exception {
        return node.encrypt(bytes);
    }

    /**
     * Decrypt encryped data using the account private key
     * @param data The encrypted data
     * @return The decrypted data
     * @throws Exception The exception that occurred
     */
    public byte[] decrypt(byte[] bytes) throws Exception {
        return node.decrypt(bytes);
    }

    /**
     * Get the contact associated with the user account
     * @return The Contact object representing the Textile account
     * @throws Exception The exception that occurred
     */
    public Contact contact() throws Exception {
        byte[] bytes = node.accountContact();
        return Contact.parseFrom(bytes);
    }

    /**
     * Syncs the local node account with all thread snapshots found on the network
     * @param options The query options to configure the behavior of the account sync
     * @return A handle that can be used to cancel the account sync
     * @throws Exception The exception that occurred
     */
    public SearchHandle sync(QueryOptions options) throws Exception {
        return node.syncAccount(options.toByteArray());
    }
}
