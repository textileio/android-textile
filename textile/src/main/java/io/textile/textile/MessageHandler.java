package io.textile.textile;

import java.util.Set;

import io.textile.pb.MessageOuterClass.Error;
import io.textile.pb.Mobile.MobileQueryEvent;
import io.textile.pb.Model.Notification;
import io.textile.pb.Model.Thread;
import io.textile.pb.Model.Contact;
import io.textile.pb.View.WalletUpdate;
import io.textile.pb.View.FeedItem;
import mobile.Event;
import mobile.Messenger;

class MessageHandler implements Messenger {

    Set<TextileEventListener> listeners;

    MessageHandler(Set<TextileEventListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void notify(Event event) {
        if (event.getName().equals("NODE_START")) {
            for (TextileEventListener listener : listeners) {
                listener.nodeStarted();
            }
        } else if (event.getName().equals("NODE_STOP")) {
            for (TextileEventListener listener : listeners) {
                listener.nodeStopped();
            }
        } else if (event.getName().equals("NODE_ONLINE")) {
            for (TextileEventListener listener : listeners) {
                listener.nodeOnline();
            }
        } else if (event.getName().equals("WALLET_UPDATE")) {
            try {
                WalletUpdate walletUpdate = WalletUpdate.parseFrom(event.getData());
                switch (walletUpdate.getType()) {
                    case THREAD_ADDED:
                        for (TextileEventListener listener : listeners) {
                            listener.threadAdded(walletUpdate.getId());
                        }
                        break;
                    case THREAD_REMOVED:
                        for (TextileEventListener listener : listeners) {
                            listener.threadRemoved(walletUpdate.getId());
                        }
                        break;
                    case ACCOUNT_PEER_ADDED:
                        for (TextileEventListener listener : listeners) {
                            listener.accountPeerAdded(walletUpdate.getId());
                        }
                        break;
                    case ACCOUNT_PEER_REMOVED:
                        for (TextileEventListener listener : listeners) {
                            listener.accountPeerRemoved(walletUpdate.getId());
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                return;
            }

        } else if (event.getName().equals("THREAD_UPDATE")) {
            try {
                FeedItem feedItem = FeedItem.parseFrom(event.getData());
                FeedItemData data = Util.feedItemData(feedItem);
                for (TextileEventListener listener : listeners) {
                    listener.threadUpdateReceived(feedItem.getThread(), data);
                }
            } catch (Exception e) {
                return;
            }
        } else if (event.getName().equals("NOTIFICATION")) {
            try {
                Notification notification = Notification.parseFrom(event.getData());
                for (TextileEventListener listener : listeners) {
                    listener.notificationReceived(notification);
                }
            } catch (Exception e) {
                return;
            }
        } else if (event.getName().equals("QUERY_RESPONSE")) {
            try {
                MobileQueryEvent queryEvent = MobileQueryEvent.parseFrom(event.getData());
                switch (queryEvent.getType()) {
                    case DATA:
                        String type = queryEvent.getData().getValue().getTypeUrl();
                        if (type.equals("/Thread")) {
                            Thread clientThread = Thread.parseFrom(queryEvent.getData().getValue().getValue());
                            for (TextileEventListener listener : listeners) {
                                listener.clientThreadQueryResult(queryEvent.getId(), clientThread);
                            }
                        } else if (type.equals("/Contact")) {
                            Contact contact = Contact.parseFrom(queryEvent.getData().getValue().getValue());
                            for (TextileEventListener listener : listeners) {
                                listener.contactQueryResult(queryEvent.getId(), contact);
                            }
                        }
                        break;
                    case DONE:
                        for (TextileEventListener listener : listeners) {
                            listener.queryDone(queryEvent.getId());
                        }
                        break;
                    case ERROR:
                        Error error = queryEvent.getError();
                        Exception e = new Exception(error.getMessage());
                        for (TextileEventListener listener : listeners) {
                            listener.queryError(queryEvent.getId(), e);
                        }
                        break;
                }
            } catch (Exception e) {
                return;
            }
        }
    }
}
