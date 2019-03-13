package com.wd.tech.util;

import android.app.Application;


import com.wd.tech.core.WDApplication;
import com.wd.tech.dao.ConversationDao;
import com.wd.tech.dao.DaoMaster;
import com.wd.tech.dao.DaoSession;

public class DaoUtils {
    private ConversationDao conversationDao;

    private static DaoUtils instance;

    private DaoUtils(){
        DaoSession session = DaoMaster.newDevSession(WDApplication.getContext(), ConversationDao.TABLENAME);
        conversationDao = session.getConversationDao();
    }

    public static DaoUtils getInstance() {
        if (instance==null){
            return new DaoUtils();
        }
        return instance;
    }

    public ConversationDao getConversationDao() {
        return conversationDao;
    }
}
