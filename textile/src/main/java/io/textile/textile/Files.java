package io.textile.textile;

import io.textile.pb.Mobile.MobilePreparedFiles;
import io.textile.pb.Model.Block;
import io.textile.pb.View.Directory;
import io.textile.pb.View.FilesList;
import mobile.Callback;
import mobile.Mobile_;

/**
 * Provides access to Textile files related APIs
 */
public class Files extends NodeDependent {

    /**
     * Interface representing an object that can be
     * called with the results of prepare
     */
    public interface PreparedFilesHandler {
        /**
         * Called with the results of prepare
         * @param preparedFiles The prepared file data
         */
        void onFilesPrepared(MobilePreparedFiles preparedFiles);

        /**
         * Called in the case of an error while preparing files
         * @param e The exception
         */
        void onError(Exception e);
    }

    Files(Mobile_ node) {
        super(node);
    }

    /**
     * Prepare raw data to later add to a Textile thread
     * @param data Raw base64 string data
     * @param threadId The thread id the data will be added to
     * @param handler An object that will get called with the results of the prepare operation
     */
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

    /**
     * Prepare file data to later add to a Textile thread
     * @param path The path to the file containing the data to prepare
     * @param threadId The thread id the data will be added to
     * @param handler An object that will get called with the results of the prepare operation
     */
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

    /**
     * Prepare raw data synchronously to later add to a Textile thread
     * @param data Raw base64 string data
     * @param threadId The thread id the data will be added to
     * @return An object containing data about the prepared files that will be used to add to the thread later
     * @throws Exception The exception that occurred
     */
    public MobilePreparedFiles prepareSync(String data, String threadId) throws Exception {
        byte[] bytes = node.prepareFilesSync(data, threadId);
        return MobilePreparedFiles.parseFrom(bytes);
    }

    /**
     * Prepare file data synchronously to later add to a Textile thread
     * @param path The path to the file containing the data to prepare
     * @param threadId The thread id the data will be added to
     * @return An object containing data about the prepared files that will be used to add to the thread later
     * @throws Exception The exception that occurred
     */
    public MobilePreparedFiles prepareByPathSync(String path, String threadId) throws Exception {
        byte[] bytes = node.prepareFilesByPathSync(path, threadId);
        return MobilePreparedFiles.parseFrom(bytes);
    }

    /**
     * Add data to a Textile thread
     * @param directory The Directory data that was previously prepared
     * @param threadId The thread to add the data to
     * @param caption A caption to associate with the data
     * @return The Block representing the added data
     * @throws Exception The exception that occurred
     */
    public Block add(Directory directory, String threadId, String caption) throws Exception {
        byte[] bytes = node.addFiles(directory.toByteArray(), threadId, caption);
        return Block.parseFrom(bytes);
    }

    /**
     * Add data from a Textile thread to another Textile thread
     * @param target The target from the source thread of the data to add
     * @param threadId The thread to add the data to
     * @param caption A caption to associate with the data
     * @return The Block representing the added data
     * @throws Exception The exception that occurred
     */
    public Block addByTarget(String target, String threadId, String caption) throws Exception {
        byte[] bytes = node.addFilesByTarget(target, threadId, caption);
        return Block.parseFrom(bytes);
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
