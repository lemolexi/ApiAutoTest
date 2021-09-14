package db;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConn {
    private static final String DB_URL_KEY_SUFFIX = ".url";
    private static final String DB_USER_KEY_SUFFIX = ".user";
    private static final String DB_PASSWORD_KEY_SUFFIX = ".password";
    private static final String DB_DRIVER_KEY_SUFFIX = ".driverClass";

//    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private String DB_DRIVER;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String dbSSH;

    private String dbName;
    //声明Connection对象
    private Connection conn = null;
    private Session session = null;

    private void connSession(String dbName) {
        String dbHost = "192.168.255.91";
//        String dbHost =
        String sshUser = DBConfig.getValue(dbName + ".ssh.user");
        String sshPassword = DBConfig.getValue(dbName + ".ssh.password");
        String sshHost = DBConfig.getValue(dbName + ".ssh.host");
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sshUser, sshHost, 22);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(sshHost, 22, dbHost, 3306);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param dbName 数据名称
     */
    public DBConn(String dbName) {
        dbUrl = DBConfig.getValue(dbName + DB_URL_KEY_SUFFIX);
        dbUser = DBConfig.getValue(dbName + DB_USER_KEY_SUFFIX);
        dbPassword = DBConfig.getValue(dbName + DB_PASSWORD_KEY_SUFFIX);
        dbSSH = DBConfig.getValue(dbName + ".isNeedSSH");
        DB_DRIVER = DBConfig.getValue(dbName + DB_DRIVER_KEY_SUFFIX);
        System.out.println("dbUrl = "+dbUrl+",dbUser = "+dbUser+",dbPassword = "+ dbPassword);
        this.dbName = dbName;
    }

    /**
     * 将数据查询结果集转成list
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private static List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<String, Object>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                //rowData.put(md.getColumnName(i), rs.getObject(i));//map.put(key = 字段名，value = 值)
                String fieldName = md.getColumnLabel(i);
                rowData.put(fieldName, rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    /**
     * 查询符合条件的数据集
     *
     * @param sql
     * @return
     */
    public List<Map<String, Object>> fetchAll(String sql) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {
            /*if (!sql.toUpperCase().startsWith("SELECT")) {
				throw new SQLException("此方法只能执行查询操作");
			}

			if (!sql.toUpperCase().startsWith("SELECT")) {
				return null;
			}*/

            if (this.initConn() == false) {
                return null;
            }
            sql = checkOracle(sql);
            pst = conn.prepareStatement(sql);//准备执行语句 ;

            rs = pst.executeQuery();
            list = convertList(rs);
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            this.close();
        }
        return list;
    }


    /**
     * 数据库更新操作
     */
    public int updateSql(String sql) {
        int result = 0;
        PreparedStatement pst = null;
        try{
            this.initConn();
            sql = checkOracle(sql);
            pst = conn.prepareStatement(sql);//准备执行语句 ;
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            this.close();
        }
        return result;
    }

    /**
     * 返回符合条件的单行数据
     *
     * @param sql
     * @return
     */
    public Map<String, Object> fetchRow(String sql) {
        List<Map<String, Object>> list = this.fetchAll(sql);
        if (list == null) {
            return null;
        }
        return list.isEmpty() ? new HashMap<String, Object>() : list.get(0);
    }

    /**
     * 返回第一列第一行的值，一般用于count 或者 单个字段的查询
     *
     * @param sql
     * @return
     */
    public Object fetchOne(String sql) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Object value = null;
        try {
			/*if (!sql.toUpperCase().startsWith("SELECT")) {
				throw new SQLException("此方法只能执行查询操作");
			}*/

            if (this.initConn() == false) {
                return null;
            }
            sql = checkOracle(sql);
            pst = conn.prepareStatement(sql);//准备执行语句 ;
            rs = pst.executeQuery();
            while (rs.next()) {
                value = rs.getObject(1);
                break;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            this.close();
        }
        return value;
    }

    /**
     * 创建数据库连接
     *
     * @return
     */
    private synchronized boolean initConn() {
        if (conn != null) {
            return true;
        }
        if (dbSSH == "true") {
            connSession(dbName);
        }
        try {
            Class.forName(DB_DRIVER);//指定连接类型
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);//获取连接
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭数据库连接
     */
    private void close() {
        try {
            if (conn != null) {
                //System.out.println("close db connection " + dbName);
                conn.close();
                conn = null;
            }
            if (session != null) {
                //System.out.println("Closing SSH Connection");
                session.disconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String checkOracle(String sql){
        if (sql.contains("limit") && dbUrl.contains("oracle")){
            String[] sqls = sql.split("limit");
            if (sqls[1].contains(",")){
                String[] nums = sqls[1].split(",");
                sql = sqls[0] + " OFFSET " + nums[0] + " ROWS FETCH NEXT  " + nums[1] + "ROWS ONLY";
            }else{
                sql = sql.replace("limit", " FETCH NEXT ") + " ROWS ONLY";
            }
        }
        return sql;
    }
}