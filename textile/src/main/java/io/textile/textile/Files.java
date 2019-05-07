package io.textile.textile;

import io.textile.pb.Mobile.MobilePreparedFiles;
import io.textile.pb.Model.Block;
import io.textile.pb.View.Directory;
import io.textile.pb.View.FilesList;
import mobile.Callback;
import mobile.Mobile_;

public class Files extends NodeDependent {

    public interface PreparedFilesHandler {
        void onFilesPrepared(MobilePreparedFiles preparedFiles);
        void onError(Exception e);
    }

    Files(Mobile_ node) {
        super(node);
    }

    public void prepare(String data, String threadId, final PreparedFilesHandler handler) {
        node.prepareFiles(data, threadId, new Callback() {
            @Override
            public void call(byte[] bytes, Exception e) {
                if (e != null) {
                    handler.onError(e);
                    return;
                }
                try {
                    handler.onFilesPrepared(MobilePreparedFiles.parseFrom(bytes));
                } catch (Exception exception) {
                    handler.onError(exception);
                }
            }
        });
    }

    public void prepareByPath(String path, String threadId, final PreparedFilesHandler handler) {
        node.prepareFilesByPath(path, threadId, new Callback() {
            @Override
            public void call(byte[] bytes, Exception e) {
                if (e != null) {
                    handler.onError(e);
                    return;
                }
                try {
                    handler.onFilesPrepared(MobilePreparedFiles.parseFrom(bytes));
                } catch (Exception exception) {
                    handler.onError(exception);
                }
            }
        });
    }

    public MobilePreparedFiles prepareSync(String data, String threadId) throws Exception {
        byte[] bytes = node.prepareFilesSync(data, threadId);
        return MobilePreparedFiles.parseFrom(bytes);
    }

    public MobilePreparedFiles prepareByPathSync(String path, String threadId) throws Exception {
        byte[] bytes = node.prepareFilesByPathSync(path, threadId);
        return MobilePreparedFiles.parseFrom(bytes);
    }

    public Block add(Directory directory, String threadId, String caption) throws Exception {
        byte[] bytes = node.addFiles(directory.toByteArray(), threadId, caption);
        return Block.parseFrom(bytes);
    }

    public Block addByTarget(String target, String threadId, String caption) throws Exception {
        byte[] bytes = node.addFilesByTarget(target, threadId, caption);
        return Block.parseFrom(bytes);
    }

    public FilesList list(String threadId, String offset, long limit) throws Exception {
        byte[] bytes = node.files(threadId, offset, limit);
        return FilesList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    public String data(String hash) throws Exception {
        return node.fileData(hash);
    }

    public String imageDataForMinWidth(String path, long minWidth) throws Exception {
        return node.imageFileDataForMinWidth(path, minWidth);
    }

}
