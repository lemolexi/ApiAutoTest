package common;

import constants.Constants;
import db.DBConn;

public class DBUtil {
    private static final String PREFIX_DB_ENVI = Constants.ENVI; //测试环境test.   模拟 sml.  正式pro.
    private static final String ESIGNPRO_SERVICE = "esignpro_service";

    public static DBConn esignpro() {
        return new DBConn(PREFIX_DB_ENVI + ESIGNPRO_SERVICE);
    }
}
