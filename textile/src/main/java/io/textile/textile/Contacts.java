package io.textile.textile;

import io.textile.pb.Model.ThreadList;
import io.textile.pb.Model.Contact;
import io.textile.pb.Model.ContactList;
import io.textile.pb.QueryOuterClass.ContactQuery;
import io.textile.pb.QueryOuterClass.QueryOptions;
import mobile.Mobile_;
import mobile.SearchHandle;

public class Contacts extends NodeDependent {

    Contacts(Mobile_ node) {
        super(node);
    }

    public void add(Contact contact) throws Exception {
        this.node.addContact(contact.toByteArray());
    }

    public Contact get(String contactId) throws Exception {
        byte[] bytes = this.node.contact(contactId);
        return Contact.parseFrom(bytes);
    }

    public ContactList list() throws Exception {
        byte[] bytes = this.node.contacts();
        return ContactList.parseFrom(bytes);
    }

    public void remove(String contactId) throws Exception {
        this.node.removeContact(contactId);
    }

    public ThreadList threads(String contactId) throws Exception {
        byte[] bytes = this.node.contactThreads(contactId);
        return ThreadList.parseFrom(bytes);
    }

    public SearchHandle search(ContactQuery query, QueryOptions options) throws Exception {
        return this.node.searchContacts(query.toByteArray(), options.toByteArray());
    }
}
