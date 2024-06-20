package com.huitongzhiyuan.testapplication.pen.obj

import com.example.basictest.pen.pt.OneNote
import com.example.basictest.pen.pt.WritePage

//import com.tal.pad.practice.model.UploadResultModel

/**
 * @author: ypp
 * @date: 2024/5/7 14:31
 * @description：笔迹数据管理
 */
object NoteDataManager {

    private var writePages = mutableMapOf<String, WritePage>()
//    private var pagesUploadMap = mutableMapOf<String, UploadResultModel>()

    fun insertAndGetPage(id: String): WritePage {
        if (writePages[id] == null) {
            val page = WritePage()
            page.pageId = id
            writePages[id] = page
            return page
        } else {
            return writePages[id]!!
        }
    }

    //===================================== 废弃 =================================//

    private var paperMap = mutableMapOf<String, OneNote>()

    fun insertAndGetNote(paperId: String): OneNote {
        if (paperMap[paperId] != null) {
            return paperMap[paperId]!!
        } else {
            val note = OneNote()
            note.createDate = System.currentTimeMillis()
            note.noteId = paperId.hashCode().toLong()
            if (paperMap[paperId] == null) {
                paperMap[paperId] = note
            }
            return note
        }
    }

    fun initAllPages(paperId: String, totalPage: Int) {
        if (paperMap[paperId] != null && paperMap[paperId]!!.writePageList.isEmpty()) {
            val note = paperMap[paperId]!!
            for (i in 1..totalPage) {
                val newPage = WritePage()
                newPage.pageNo = i
                note.writePageList.add(newPage)
            }
        }
    }

    fun getOnePage(paperId: String, pageNo: Int): WritePage? {
        if (paperMap[paperId] != null) {
            val note = paperMap[paperId]!!
            val hasThisPage = note.writePageList.any {
                it.pageNo == pageNo
            }
            if (hasThisPage) {
                return note.writePageList.find { it.pageNo == pageNo }!!
            }
        }
        return null
    }


}