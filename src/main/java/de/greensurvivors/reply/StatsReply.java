package de.greensurvivors.reply;

public interface StatsReply {
    int getCreatedPatesCount();

    int getPastesWithOwnersCount();

    int getUserCount();

    int getTagCount();

    int getFolderCount();

    int getIndexedPastesCount();

    int getS3pasteCount();
}
