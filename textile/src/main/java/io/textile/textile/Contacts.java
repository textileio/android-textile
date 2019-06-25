package io.textile.textile;

import io.textile.pb.Model.ThreadList;
import io.textile.pb.Model.Contact;
import io.textile.pb.Model.ContactList;
import io.textile.pb.QueryOuterClass.ContactQuery;
import io.textile.pb.QueryOuterClass.QueryOptions;
import mobile.Mobile_;
import mobile.SearchHandle;

/**
 * Provides access to Textile contacts related APIs
 */
public class Contacts extends NodeDependent {

    Contacts(final Mobile_ node) {
        super(node);
    }

    /**
     * Add a new Contact to the account's list of Contacts
     * @param contact The new contact to add, usually returned from a Contact search
     * @throws Exception The exception that occurred
     */
    public void add(final Contact contact) throws Exception {
        node.addContact(contact.toByteArray());
    }

    /**
     * Get a Contact by address from list of existing Contacts
     * @param address The address of the Contact to retrieve
     * @return The Contact object corresponding to the address
     * @throws Exception The exception that occurred
     */
    public Contact get(final String address) throws Exception {
        /*
         * contact throws an Exception if no contact is found.
         * We can assume that bytes is valid once we get to Contact.parseFrom(bytes)
         */
        final byte[] bytes = node.contact(address);
        return Contact.parseFrom(bytes);
    }

    /**
     * List all existing account Contacts
     * @return An object containing a list of all account contacts
     * @throws Exception The exception that occurred
     */
    public ContactList list() throws Exception {
        final byte[] bytes = node.contacts();
        return ContactList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    /**
     * Remove a Contact from the account by address
     * @param address The address of the contact to remove
     * @throws Exception The exception that occurred
     */
    public void remove(final String address) throws Exception {
        node.removeContact(address);
    }

    /**
     * List all threads a particular contact and the local node account participate in
     * @param address The contact address to find threads for
     * @return An object containing a list of all threads the contact and the local node account participate in
     * @throws Exception The exception that occurred
     */
    public ThreadList threads(final String address) throws Exception {
        final byte[] bytes = node.contactThreads(address);
        return ThreadList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    /**
     * Search for Textile Contacts across the entire network
     * @param query A query object describing the search to execute
     * @param options A query options object to control the behavior of the search
     * @return A handle that can be used to cancel the search
     * @throws Exception The exception that occurred
     */
    public SearchHandle search(final ContactQuery query, final QueryOptions options) throws Exception {
        return node.searchContacts(query.toByteArray(), options.toByteArray());
    }
}
