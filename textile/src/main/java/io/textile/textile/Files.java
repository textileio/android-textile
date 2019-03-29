package io.textile.textile;

import io.textile.pb.Mobile.MobilePreparedFiles;
import mobile.Callback;
import mobile.Mobile_;

public class Files extends NodeDependent {

    interface PreparedFilesHandler {
        void onFilesPrepared(MobilePreparedFiles preparedFiles);
        void onError(Exception e);
    }

    Files(Mobile_ node) {
        super(node);
    }

    public void prepare(String data, String threadId, final PreparedFilesHandler handler) {
        this.node.prepareFiles(data, threadId, new Callback() {
            @Override
            public void call(byte[] bytes, Exception e) {
                try {
                    handler.onFilesPrepared(MobilePreparedFiles.parseFrom(bytes));
                } catch (Exception e) {
                    handler.onError(e);
                }
            }
        });
    }

}
