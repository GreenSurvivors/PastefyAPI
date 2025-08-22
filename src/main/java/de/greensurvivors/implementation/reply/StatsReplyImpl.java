package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.reply.StatsReply;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord") // don't want to expose constructor
public class StatsReplyImpl implements StatsReply {
    @SerializedName("created_pastes")
    private final int createdPatesCount;
    @SerializedName("logged_in_pastes")
    private final int pastesWithOwnersCount;
    @SerializedName("user_count")
    private final int userCount;
    @SerializedName("tag_count")
    private final int tagCount;
    @SerializedName("folder_count")
    private final int folderCount;
    @SerializedName("indexed_pastes")
    private final int indexedPastesCount;
    @SerializedName("s3pastes")
    private final int s3pasteCount;

    private StatsReplyImpl(int createdPatesCount,
                          int pastesWithOwnersCount,
                          int userCount, int tagCount,
                          int folderCount,
                          int indexedPastesCount,
                          int s3pasteCount) {
        this.createdPatesCount = createdPatesCount;
        this.pastesWithOwnersCount = pastesWithOwnersCount;
        this.userCount = userCount;
        this.tagCount = tagCount;
        this.folderCount = folderCount;
        this.indexedPastesCount = indexedPastesCount;
        this.s3pasteCount = s3pasteCount;
    }

    @Override
    public int getCreatedPatesCount() {
        return createdPatesCount;
    }

    @Override
    public int getPastesWithOwnersCount() {
        return pastesWithOwnersCount;
    }

    @Override
    public int getUserCount() {
        return userCount;
    }

    @Override
    public int getTagCount() {
        return tagCount;
    }

    @Override
    public int getFolderCount() {
        return folderCount;
    }

    @Override
    public int getIndexedPastesCount() {
        return indexedPastesCount;
    }

    @Override
    public int getS3pasteCount() {
        return s3pasteCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StatsReplyImpl) obj;
        return this.createdPatesCount == that.createdPatesCount &&
            this.pastesWithOwnersCount == that.pastesWithOwnersCount &&
            this.userCount == that.userCount &&
            this.tagCount == that.tagCount &&
            this.folderCount == that.folderCount &&
            this.indexedPastesCount == that.indexedPastesCount &&
            this.s3pasteCount == that.s3pasteCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdPatesCount, pastesWithOwnersCount, userCount, tagCount, folderCount, indexedPastesCount, s3pasteCount);
    }

    @Override
    public String toString() {
        return "StatsReplyImpl[" +
            "createdPatesCount=" + createdPatesCount + ", " +
            "pastesWithOwnersCount=" + pastesWithOwnersCount + ", " +
            "userCount=" + userCount + ", " +
            "tagCount=" + tagCount + ", " +
            "folderCount=" + folderCount + ", " +
            "indexedPastesCount=" + indexedPastesCount + ", " +
            "s3pasteCount=" + s3pasteCount + ']';
    }
}
