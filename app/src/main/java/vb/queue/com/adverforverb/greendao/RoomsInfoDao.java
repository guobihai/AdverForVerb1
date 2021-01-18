package vb.queue.com.adverforverb.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import vb.queue.com.adverforverb.entry.RoomsInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ROOMS_INFO".
*/
public class RoomsInfoDao extends AbstractDao<RoomsInfo, Long> {

    public static final String TABLENAME = "ROOMS_INFO";

    /**
     * Properties of entity RoomsInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "Id", true, "_id");
        public final static Property RoomName = new Property(1, String.class, "roomName", false, "ROOM_NAME");
        public final static Property RoomTitle = new Property(2, String.class, "roomTitle", false, "ROOM_TITLE");
        public final static Property RoomDesc = new Property(3, String.class, "roomDesc", false, "ROOM_DESC");
        public final static Property Type = new Property(4, int.class, "type", false, "TYPE");
        public final static Property Remark = new Property(5, String.class, "remark", false, "REMARK");
        public final static Property Time = new Property(6, String.class, "time", false, "TIME");
    }


    public RoomsInfoDao(DaoConfig config) {
        super(config);
    }
    
    public RoomsInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ROOMS_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: Id
                "\"ROOM_NAME\" TEXT," + // 1: roomName
                "\"ROOM_TITLE\" TEXT," + // 2: roomTitle
                "\"ROOM_DESC\" TEXT," + // 3: roomDesc
                "\"TYPE\" INTEGER NOT NULL ," + // 4: type
                "\"REMARK\" TEXT," + // 5: remark
                "\"TIME\" TEXT);"); // 6: time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ROOMS_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RoomsInfo entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String roomName = entity.getRoomName();
        if (roomName != null) {
            stmt.bindString(2, roomName);
        }
 
        String roomTitle = entity.getRoomTitle();
        if (roomTitle != null) {
            stmt.bindString(3, roomTitle);
        }
 
        String roomDesc = entity.getRoomDesc();
        if (roomDesc != null) {
            stmt.bindString(4, roomDesc);
        }
        stmt.bindLong(5, entity.getType());
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(6, remark);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(7, time);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RoomsInfo entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String roomName = entity.getRoomName();
        if (roomName != null) {
            stmt.bindString(2, roomName);
        }
 
        String roomTitle = entity.getRoomTitle();
        if (roomTitle != null) {
            stmt.bindString(3, roomTitle);
        }
 
        String roomDesc = entity.getRoomDesc();
        if (roomDesc != null) {
            stmt.bindString(4, roomDesc);
        }
        stmt.bindLong(5, entity.getType());
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(6, remark);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(7, time);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RoomsInfo readEntity(Cursor cursor, int offset) {
        RoomsInfo entity = new RoomsInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // roomName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // roomTitle
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // roomDesc
            cursor.getInt(offset + 4), // type
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // remark
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // time
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RoomsInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRoomName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setRoomTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRoomDesc(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.getInt(offset + 4));
        entity.setRemark(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RoomsInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RoomsInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RoomsInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}