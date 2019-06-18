package io.textile.textile;

import io.textile.pb.Model.Block;
import io.textile.pb.View;
import io.textile.pb.View.FilesList;
import mobile.Callback;
import mobile.Mobile_;

/**
 * Provides access to Textile files related APIs
 */
public class Files extends NodeDependent {

    /**
     * Interface representing an object that can be
     * called with a Block result
     */
    public interface BlockHandler {
        /**
         * Called with a block result
         * @param block The block
         */
        void onComplete(Block block);

        /**
         * Called in the case of an error
         * @param e The exception
         */
        void onError(Exception e);
    }

    Files(final Mobile_ node) {
        super(node);
    }

    /**
     * Add raw data to to a Textile thread
     * @param data Raw bytes
     * @param threadId The thread id the data will be added to
     * @param handler An object that will get called with the resulting block
     */
    public void addData(byte[] data, String threadId, String caption, final BlockHandler handler) {
        node.addData(data, threadId, caption, new Callback() {
            @Override
            public void call(byte[] bytes, Exception e) {
                if (e != null) {
                    handler.onError(e);
                    return;
                }
                try {
                    handler.onComplete(Block.parseFrom(bytes));
                } catch (Exception exception) {
                    handler.onError(exception);
                }
            }
        });
    }

    /**
     * Add file(s) to to a Textile thread
     * @param files A list of file paths
     * @param threadId The thread id the data will be added to
     * @param handler An object that will get called with the resulting block
     */
    public void addFiles(View.Strings files, String threadId, String caption, final BlockHandler handler) {
        node.addFiles(files.toByteArray(), threadId, caption, new Callback() {
            @Override
            public void call(byte[] bytes, Exception e) {
                if (e != null) {
                    handler.onError(e);
                    return;
                }
                try {
                    handler.onComplete(Block.parseFrom(bytes));
                } catch (Exception exception) {
                    handler.onError(exception);
                }
            }
        });
    }

    /**
     * Share files to a Textile thread
     * @param hash The hash of the files graph to share
     * @param threadId The thread id the data will be added to
     * @param handler An object that will get called with the resulting block
     */
    public void shareFiles(String hash, String threadId, String caption, final BlockHandler handler) {
        node.shareFiles(hash, threadId, caption, new Callback() {
            @Override
            public void call(byte[] bytes, Exception e) {
                if (e != null) {
                    handler.onError(e);
                    return;
                }
                try {
                    handler.onComplete(Block.parseFrom(bytes));
                } catch (Exception exception) {
                    handler.onError(exception);
                }
            }
        });
    }

    /**
     * Get a list of files data from a thread
     * @param threadId The thread to query
     * @param offset The offset to beging querying from
     * @param limit The max number of results to return
     * @return An object containing a list of files data
     * @throws Exception The exception that occurred
     */
    public FilesList list(String threadId, String offset, long limit) throws Exception {
        byte[] bytes = node.files(threadId, offset, limit);
        return FilesList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    /**
     * Get raw data for a file hash
     * @param hash The hash to return data for
     * @return The base64 string of data
     * @throws Exception The exception that occurred
     */
    public String content(String hash) throws Exception {
        return node.fileContent(hash);
    }

    /**
     * Helper function to return the most appropriate image data for a minimun image width
     * @param path The IPFS path that includes image data for various image sizes
     * @param minWidth The width of the image the data will be used for
     * @return The base64 string of image data
     * @throws Exception The exception that occurred
     */
    public String imageContentForMinWidth(String path, long minWidth) throws Exception {
        return node.imageFileContentForMinWidth(path, minWidth);
    }

}
