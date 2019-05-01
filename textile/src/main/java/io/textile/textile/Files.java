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
                    handler.onFilesPrepared(bytes != null ? MobilePreparedFiles.parseFrom(bytes) : null);
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
                    handler.onFilesPrepared(bytes != null ? MobilePreparedFiles.parseFrom(bytes) : null);
                } catch (Exception exception) {
                    handler.onError(exception);
                }
            }
        });
    }

    public MobilePreparedFiles prepareSync(String data, String threadId) throws Exception {
        byte[] bytes = node.prepareFilesSync(data, threadId);
        return bytes != null ? MobilePreparedFiles.parseFrom(bytes) : null;
    }

    public MobilePreparedFiles prepareByPathSync(String path, String threadId) throws Exception {
        byte[] bytes = node.prepareFilesByPathSync(path, threadId);
        return bytes != null ? MobilePreparedFiles.parseFrom(bytes) : null;
    }

    public Block add(Directory directory, String threadId, String caption) throws Exception {
        byte[] bytes = node.addFiles(directory.toByteArray(), threadId, caption);
        return bytes != null ? Block.parseFrom(bytes) : null;
    }

    public Block addByTarget(String target, String threadId, String caption) throws Exception {
        byte[] bytes = node.addFilesByTarget(target, threadId, caption);
        return bytes != null ? Block.parseFrom(bytes) : null;
    }

    public FilesList list(String offset, long limit, String threadId) throws Exception {
        byte[] bytes = node.files(offset, limit, threadId);
        return FilesList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    public String data(String hash) throws Exception {
        return node.fileData(hash);
    }

    public String imageDataForMinWidth(String path, long minWidth) throws Exception {
        return node.imageFileDataForMinWidth(path, minWidth);
    }

}
