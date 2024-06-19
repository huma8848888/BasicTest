package com.example.basictest.pen.listener;

import com.example.basictest.pen.obj.wobj.WriteObj;
import com.example.basictest.pen.pt.OneNote;
import com.example.basictest.pen.pt.WritePage;

/**
 * Created by dongqiangqiang on 2024/4/26
 */
public interface IDataOptSupportListener {

    void insert(OneNote oneNote, WritePage writePage, WriteObj<?> writeObj);

    void insert(WritePage writePage, WriteObj<?> writeObj);

//    void undo(OneNote oneNote, WritePage writePage, WriteObj<?> writeObj);
//
//    void redo(OneNote oneNote, WritePage writePage, WriteObj<?> writeObj);
//
//    void clear(OneNote oneNote, WritePage writePage);
//
//    void newPage(OneNote oneNote, WritePage writePage);
//
//    void deletePage(OneNote oneNote, WritePage writePage);
//
//    void copyPage(OneNote oneNote, WritePage writePage);

//    void savePage(OneNote oneNote, WritePage writePage);

}
