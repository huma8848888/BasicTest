package com.example.basictest.pen.pt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongqiangqiang on 2024/4/24
 */
public class OneNote {

    public long noteId;
    public String noteName;

    public long createDate;

    public long updateDate;

    public String notePath;

    public String noteThumbnail;
    public Integer noteType;

    public int pageNum;

    public List<WritePage> writePageList = new ArrayList<>();

    @Override
    public String toString() {
        return "OneNote{" +
                "noteId=" + noteId +
                ", noteName='" + noteName + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", notePath='" + notePath + '\'' +
                ", noteThumbnail='" + noteThumbnail + '\'' +
                ", noteType=" + noteType +
                ", pageNum=" + pageNum +
                '}';
    }
}
