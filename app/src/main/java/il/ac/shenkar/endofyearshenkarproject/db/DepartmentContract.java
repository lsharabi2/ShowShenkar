package il.ac.shenkar.endofyearshenkarproject.db;

import android.provider.BaseColumns;

public final class DepartmentContract {
    private DepartmentContract() {
    }

    public static class DepartmentEntry implements BaseColumns {
        public static final String TABLE_NAME = "departments_table";
        public static final String COLUMN_NAME_SERVER_ID = "server_id";
        public static final String COLUMN_NAME_JSON_BLOB = "json_blob";
    }
}
