package com.wd.tech.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.wd.tech.bean.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property NickName = new Property(1, String.class, "nickName", false, "NICK_NAME");
        public final static Property Phone = new Property(2, String.class, "phone", false, "PHONE");
        public final static Property Pwd = new Property(3, String.class, "pwd", false, "PWD");
        public final static Property SessionId = new Property(4, String.class, "sessionId", false, "SESSION_ID");
        public final static Property UserId = new Property(5, int.class, "userId", false, "USER_ID");
        public final static Property UserName = new Property(6, String.class, "userName", false, "USER_NAME");
        public final static Property WhetherVip = new Property(7, int.class, "whetherVip", false, "WHETHER_VIP");
        public final static Property WhetherFaceId = new Property(8, int.class, "whetherFaceId", false, "WHETHER_FACE_ID");
        public final static Property Statu = new Property(9, String.class, "statu", false, "STATU");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"NICK_NAME\" TEXT," + // 1: nickName
                "\"PHONE\" TEXT," + // 2: phone
                "\"PWD\" TEXT," + // 3: pwd
                "\"SESSION_ID\" TEXT," + // 4: sessionId
                "\"USER_ID\" INTEGER NOT NULL ," + // 5: userId
                "\"USER_NAME\" TEXT," + // 6: userName
                "\"WHETHER_VIP\" INTEGER NOT NULL ," + // 7: whetherVip
                "\"WHETHER_FACE_ID\" INTEGER NOT NULL ," + // 8: whetherFaceId
                "\"STATU\" TEXT);"); // 9: statu
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(2, nickName);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(4, pwd);
        }
 
        String sessionId = entity.getSessionId();
        if (sessionId != null) {
            stmt.bindString(5, sessionId);
        }
        stmt.bindLong(6, entity.getUserId());
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(7, userName);
        }
        stmt.bindLong(8, entity.getWhetherVip());
        stmt.bindLong(9, entity.getWhetherFaceId());
 
        String statu = entity.getStatu();
        if (statu != null) {
            stmt.bindString(10, statu);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(2, nickName);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(4, pwd);
        }
 
        String sessionId = entity.getSessionId();
        if (sessionId != null) {
            stmt.bindString(5, sessionId);
        }
        stmt.bindLong(6, entity.getUserId());
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(7, userName);
        }
        stmt.bindLong(8, entity.getWhetherVip());
        stmt.bindLong(9, entity.getWhetherFaceId());
 
        String statu = entity.getStatu();
        if (statu != null) {
            stmt.bindString(10, statu);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nickName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // phone
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // pwd
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // sessionId
            cursor.getInt(offset + 5), // userId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // userName
            cursor.getInt(offset + 7), // whetherVip
            cursor.getInt(offset + 8), // whetherFaceId
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // statu
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setNickName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPhone(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPwd(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSessionId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUserId(cursor.getInt(offset + 5));
        entity.setUserName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setWhetherVip(cursor.getInt(offset + 7));
        entity.setWhetherFaceId(cursor.getInt(offset + 8));
        entity.setStatu(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
