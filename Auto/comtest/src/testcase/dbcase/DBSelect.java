package testcase.dbcase;

import common.DBUtil;
import db.DBConn;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Author: zml
 * @Date: Create in [2018-07-02 17:45]
 * @Description: 数据库查询
 * @Modified By:
 */
@Slf4j
public class DBSelect {
    public static List<Map<String, Object>> getAccountList(String query) {
        String sql = "select id,uniqueId,name from account";
        if (!query.isEmpty()) {
            sql = sql + " where " + query;
        }
        DBConn conn = DBUtil.esignpro();
        List<Map<String, Object>> data = conn.fetchAll(sql);

        dbLogger(sql, data.toString());
        return data;
    }

    public static String getAccountId(String query) {
        String sql = "select id from account";
        if (!query.isEmpty()) {
            sql = sql + " where " + query;
        }
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * 功能描述: 根据kaptchaId查询短信验证码
     *
     * @auther: zml
     * @date: 2018-07-17 14:15
     * @param: [kaptchaId]
     * @return: java.lang.String
     */
    public static String getKaptcha(String kaptchaId) {
        String sql = "SELECT kaptcha FROM auth_sms_kaptcha";
        if (!kaptchaId.isEmpty()) {
            sql = String.format(sql + " WHERE uuid = '%s'", kaptchaId);
        }
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * 功能描述: 根据不同状态查询流程Id
     *
     * @auther: zml
     * @date: 2018-07-17 14:14
     * @param: [status]：0-草稿，1-签署中，2-完成，3-撤销，4-终止，5-过期，6-删除，7-拒签，8作废，9已归档，10预盖章，11旧版本废弃
     * @return: java.lang.String
     */
    public static String getProccessInfoId(String status) {
        String sql = "SELECT id FROM sf_process_info";
        if (!status.isEmpty()) {
            sql = String.format(sql + " WHERE sign_status = '%s' AND flow_type='Common' ", status);
        }
        sql = sql + " ORDER BY gmt_create DESC";
        DBConn conn = DBUtil.esignpro();
        try {
            String data = conn.fetchOne(sql).toString();

            dbLogger(sql, data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 功能描述: 根据不同状态查询流程相关信息,返回map
     *
     * @auther: heyun
     * @date: 2021-03-05 14:14
     * @param: [status]：0-草稿，1-签署中，2-完成，3-撤销，4-终止，5-过期，6-删除，7-拒签，8作废，9已归档，10预盖章，11旧版本废弃
     */
    public static Map<String,Object> getProccessInfo(String status) {
        String sql = "SELECT id,initiator_account_gid FROM sf_process_info";
        if (!status.isEmpty()) {
            sql = String.format(sql + " WHERE sign_status = '%s' AND flow_type='Common' ", status);
        }
        sql = sql + " ORDER BY gmt_create DESC";
        //log.info("sql为："+sql);
        DBConn conn = DBUtil.esignpro();
        try {
            Map map = conn.fetchRow(sql);
            dbLogger(sql, map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 功能描述: 根据不同状态查询对应的表单id
     */
    public static Integer getTemplateFromId(String templateStatus,String formStatus) {
        String sql = "SELECT f.id FROM template t left join template_form f on t.templateId = f.templateId where t.status = "+templateStatus+" and f.`status` = "+ formStatus+" order by f.createTime desc";
        DBConn conn = DBUtil.esignpro();
        Integer data = conn.fetchOne(sql).hashCode();

        dbLogger(sql, String.valueOf(data));
        return data;
    }

    /**
     * 功能描述: 根据不同状态查询对应的模板id
     */
    public static String getTemplateId(int templateStatus) {
        String sql = "SELECT templateId FROM template where status = "+templateStatus + " order by createTime desc";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, String.valueOf(data));
        return data;
    }

    /**
     * @author: wenmin
     * @date: 2021/1/12 10:59
     * @description: 查询流程的签署人/抄送人/预盖章人的id
     **/
    public static String getProccessActorId(String flowId,Integer roldId,String accountId,String orgId) {
        String sql = String.format("SELECT id FROM sf_process_actor WHERE process_id = %s AND role_id = %s",flowId,roldId);
        if (accountId!=null) {
            sql = String.format(sql + " AND signer_account_gid='%s' ", accountId);
        }
        if (accountId!=null) {
            sql = String.format(sql + " AND authorization_organize_gid='%s' ", orgId);
        }
        sql = sql + " ORDER BY gmt_create DESC";
        DBConn conn = DBUtil.esignpro();
        try {
            String data = conn.fetchOne(sql).toString();

            dbLogger(sql, data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCancelProccessInfoId(String status) {
        String sql = "SELECT id FROM sf_process_info";
        if (!status.isEmpty()) {
            sql = String.format(sql + " WHERE sign_status = '%s' AND flow_type='Cancellation' AND is_archived = 1 ", status);
        }
        sql = sql + " ORDER BY gmt_create DESC";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static String getforzenstatus(String processId) {
        String sql = "SELECT forzen_status FROM sf_process_info";
        if (!processId.isEmpty()) {
            sql = String.format(sql + " WHERE id = '%s'", processId);
        }
        sql = sql + " ORDER BY gmt_create DESC";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }
    /**
     * 功能描述: 根据流程Id查询签署文档
     *
     * @auther: zml
     * @date: 2018-07-17 14:14
     * @param: [flowId]
     * @return: net.sf.json.JSONObject
     */
    public static JSONObject getProcessDoc(String flowId) {
        String sql = "SELECT a.id,a.doc_manager_id,a.fileKey,a.status,a.encryption FROM sf_sign_doc a " +
                "INNER JOIN sf_process_doc b ON b.sign_doc_id = a.id " +
                "INNER JOIN sf_process_info c ON c.id = b.process_id";
        if (!flowId.isEmpty()) {
            sql = String.format(sql + " WHERE c.id = '%s'", flowId);
        }
        sql = sql + " ORDER BY c.gmt_create DESC";
        DBConn conn = DBUtil.esignpro();
        List<Map<String, Object>> r = conn.fetchAll(sql);
        JSONObject data = JSONObject.fromObject(r.get(0));

        dbLogger(sql, data);
        return data;
    }


    /**
     * 功能描述: 根据流程Id查询签署文档
     *
     * @auther: zml
     * @date: 2018-07-17 14:14
     * @param: [flowId]
     * @return: net.sf.json.JSONObject
     */
    public static JSONObject getProcessTask(String flowId) {
        String sql = "SELECT a.id,a.process_id,a.process_actor_id,a.doc_id,a.status,c.signer_account_gid,d.sign_filekey FROM sf_sign_doc_task a \n" +
                "INNER JOIN sf_process_info b ON b.id=a.process_id \n" +
                "INNER JOIN sf_process_actor c ON c.id=a.process_actor_id \n" +
                "INNER JOIN sf_process_doc d ON d.process_id=b.id";
        if (!flowId.isEmpty()) {
            sql = String.format(sql + "  AND b.id='%s' AND a.status<>0", flowId);
        }
        sql = sql + " ORDER BY a.gmt_create DESC";
        DBConn conn = DBUtil.esignpro();
        List<Map<String, Object>> r = conn.fetchAll(sql);
        JSONObject data = JSONObject.fromObject(r.get(0));

        dbLogger(sql, data);
        return data;
    }

    public static JSONArray getProcessActor_id(String flowId) {
        String sql = "SELECT process_actor_id FROM sf_sign_doc_task";
        if (!flowId.isEmpty()) {
            sql = String.format(sql + " WHERE process_id ='%s'", flowId);
        }
        DBConn conn = DBUtil.esignpro();
        List<Map<String, Object>> r = conn.fetchAll(sql);
        int size = r.size();
        JSONArray data = new JSONArray();
        for(int i =0;i<size;i++){
            data.add(r.get(i));
        }

        dbLogger(sql, data);
        return data;
    }
    /**
     * 功能描述: 根据冻结状态查询流程Id
     *
     * @param
     * @return java.lang.String
     * @author jcy
     * @date 2018-8-9 11:20
     */
    public static String getProccessIdByFrozenstatus(String status, String frozenStatus) {
        String sql = "SELECT id FROM sf_process_info";
        if (!status.isEmpty()) {
            sql = String.format(sql + " WHERE sign_status = '%s' and forzen_status = '%s'", status, frozenStatus);
        }
        sql = sql + "and flow_type = 'common' and is_archived = 1 ORDER BY gmt_create DESC";
        DBConn conn = DBUtil.esignpro();
        try{
            String data = conn.fetchOne(sql).toString();

            dbLogger(sql, data);
            return data;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 功能描述: 根据签署状态，冻结状态，归档状态，混合云签署状态查询流程Id
     *
     * @param
     * @return java.lang.String
     * @author jcy
     * @date 2018-8-9 11:20
     */
    public static String getProccessIdByStatus(String signStatus, String frozenStatus, String esignFlag, String flowType) {
        String sql = "SELECT id FROM sf_process_info";
        if (!signStatus.isEmpty()) {
            sql = String.format(sql + " WHERE sign_status = '%s' and forzen_status = '%s' and esign_flag = '%s' and flow_type = '%s'",
                    signStatus, frozenStatus, esignFlag, flowType);
        }
        sql = sql + "and is_archived = 1 ORDER BY gmt_create DESC";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * 功能描述: 根据task签署状态查询流程Id
     *
     * @param
     * @return java.lang.String
     * @author jcy
     * @date 2018-8-9 11:20
     */
    public static String getProccessIdByTaskStatus(String signStatus, String frozenStatus, String esignFlag, String flowType, String taskStatus) {
        String sql = "select f.id from sf_process_info f";
        if (!signStatus.isEmpty()) {
            sql = String.format(sql + " INNER JOIN sf_sign_doc_task t on f.id = t.process_id WHERE f.sign_status = '%s' and f.forzen_status = '%s' and f.esign_flag = '%s' and f.flow_type = '%s' and t.status = '%s'",
                    signStatus, frozenStatus, esignFlag, flowType, taskStatus);
        }
        sql = sql + "ORDER BY f.gmt_create DESC";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static String getDepartmentName(String departmentId) {
        String sql = "SELECT name FROM department";
        if (!departmentId.isEmpty()) {
            sql = String.format(sql + " where id = '%s' ", departmentId);
        }
        System.out.println("sql = " + sql);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static String getDepartmentParentId(String departmentId) {
        String sql = "SELECT parentId FROM department";
        if (!departmentId.isEmpty()) {
            sql = String.format(sql + " where id = '%s'", departmentId);
        }
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static void main(String[] args) {
        getRoleId();
        getDepartmentId();
        getAuthInfoId();
        getArchiveDocId();
        getQuickProcessId();
    }

    public static int getRoleId() {
        String sql = "select roleId from role_account_ref where status = 1 ORDER BY createTime desc limit 1;";
        DBConn conn = DBUtil.esignpro();
        int data = conn.fetchOne(sql).hashCode();

        dbLogger(sql, String.valueOf(data));
        return data;
    }

    public static String getAccountIdByRoleId(int roleId) {
        String sql = "SELECT accountId FROM role_account_ref";
        sql = String.format(sql + " where roleId = '%s'", roleId) + " ORDER BY createTime desc limit 1;";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static List<Map<String, Object>> getAccountIds() {
        String sql = "SELECT id  FROM account a where a.status= 1 order by a.createTime desc limit 10";
        DBConn conn = DBUtil.esignpro();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = conn.fetchAll(sql);
        dbLogger(sql, list);
        return list;
    }

    public static String getDepartmentId() {
        String sql = "select id from department where status = 1 ORDER BY createTime desc limit 1;";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static String getAuthInfoId() {
        String sql = "select id from account_auth_info where status = '1'  limit 1;";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static String getFingerTemplate() {
        String sql = "select SUBSTRING_INDEX(SUBSTRING_INDEX(value,'\\\"',8),'\\\"',-1) AS fingerTemplate from auth_info_ex where status = 1 and value like '%fingerImage%' order by createTime desc limit 1;";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * 功能描述:查询证书Id
     *
     * @return data
     * @author jcy
     * @date: 2018-9-10 11:15
     * @param:[type]：类型，0-云证书单证书，1-ukey单证书，2-云证书双证书，3-ukey双证书 [flag]：1- 距离截止日期30天之内，2- 距离截止日期大于30天
     */

    public static String getCertId(Integer type, int flag) {
        String sql = "select id from cert_info_ref where type =" + type + "";

        if (flag == 1) {
            sql = String.format(sql + " and DATEDIFF(endDate,CURDATE())<=30");
        } else {
            sql = String.format(sql + " and DATEDIFF(endDate,CURDATE())>30");
        }
        sql = sql + " ORDER BY createTime desc";

        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();
        dbLogger(sql, data);
        return data;
    }

    /**
     * 功能描述:获取借阅中的文档
     *
     * @param
     * @return java.lang.String
     * @author jcy
     * @date 2018-9-13 20:16
     */
    public static JSONObject getArchiveDocId() {
        String sql = "select archiveId,accountId from borrow_approve where `status`=2 ORDER BY createTime desc";

        DBConn conn = DBUtil.esignpro();
        List<Map<String, Object>> r = conn.fetchAll(sql);
        JSONObject data = JSONObject.fromObject(r.get(0));

        dbLogger(sql, data);
        return data;
    }

    /**
     * 功能描述: 通过归档文档Id查找flowId
     *
     * @param
     * @return java.lang.String
     * @author jcy
     * @date 2018-9-20 14:46
     */

    public static String getFlowdIdByArchiveId(String archivedId) {
        String sql = "select envelopeId from archive_doc";

        if (!archivedId.isEmpty()) {
            sql = String.format(sql + " where id = '%s'", archivedId);
        }
        sql = sql + " ORDER BY createTime DESC";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }


    //根据用户查询印章ID
    public static String getRequestId(String accountId) {
//        String sql = "select seal_request_id from seal_manager  where user_id = 'd9910925-ab3f-4acc-ae27-ab552ff8d05b' and is_delete = 0 and `type` = 2 and `status` =5 order by create_time desc limit 1;";
        String sql = String.format(" select seal_request_id from seal_manager  where user_id = '%d' and is_delete = 0 and `type` = 2 and `status` =5 order by create_time desc limit 1;", accountId);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();
        dbLogger(sql, data);
        return data;
    }

    /**
     * 功能描述:获取打印文档的id
     *
     * @param
     * @return java.lang.String
     * @author jcy
     * @date 2018-9-27 17:43
     */
    public static String getPrintTaskId(int status) {
        String sql = "select id from print_task_log";
        sql = String.format(sql + " where state = '%s'", status) + " ORDER BY createTime desc;";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * @Description: 获取是快捷签的信封
     * @Author: wenmin
     * @Date: 2018/10/31 18:00
     * @Param: []
     * @Return: net.sf.json.JSONObject
     */
    public static String getQuickProcessId() {

        String sql = "SELECT a.id FROM sf_process_info a \n" +
                "WHERE a.sign_status=1 \n" +
                "AND a.esign_flag<>0 \n" +
                "ORDER BY a.id DESC LIMIT 1;";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * @Description: 查询外部打印Id
     * @Author: wl
     * @Date: 2020/01/05 10:10
     * @Param: []
     * @Return: java.lang.String
     */
    public static String getOutArchiveDocId(String accountId) {
        String sql =  String.format("SELECT archiveDocId FROM print_task_log WHERE accountId = '%s'", accountId);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * @Description: 查询归档id
     * @Author: wl
     * @Date: 2020/01/05 10:10
     * @Param: []
     * @Return: java.lang.String
     */
    public static String getarchivedocid(String accountId) {
        String sql =  String.format("SELECT id FROM archive_doc WHERE accountId = '%s'", accountId);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * @Description: 根据flowId查询签署文档
     * @Author: wl
     * @Date: 2020/01/05 10:10
     * @Param: []
     * @Return: java.lang.String
     */
    public static String getprocessIdDocId(String signFlowId) {
        String sql =  String.format("SELECT sign_doc_id FROM sf_process_doc WHERE process_id = '%s'", signFlowId);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * @Description: 查询外部下载Id
     * @Author: wl
     * @Date: 2020/01/05 10:10
     * @Param: []
     * @Return: java.lang.String
     */
    public static String getOutDownloadArchiveDocId(String accountId) {
        String sql =  String.format("SELECT archive_doc_id FROM doc_download_log WHERE account_id = '%s'", accountId);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * @Description: 查询外部下载signDocId
     * @Author: wl
     * @Date: 2020/01/05 10:10
     * @Param: []
     * @Return: java.lang.String
     */
    public static String getOutDownloadsignDocId(String accountId) {
        String sql =  String.format("SELECT process_id FROM doc_download_log WHERE account_id = '%s'", accountId);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * @Description: 查询外部下载DocId
     * @Author: wl
     * @Date: 2020/01/05 10:10
     * @Param: []
     * @Return: java.lang.String
     */
    public static String getOutDownloadDocId(String accountId) {
        String sql =  String.format("SELECT sign_doc_id FROM doc_download_log WHERE account_id = '%s'", accountId);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * @Description: 查询外部打印签署文档id
     * @Author: wl
     * @Date: 2020/01/05 10:10
     * @Param: []
     * @Return: java.lang.String
     */
    public static String getOutProcessId(String accountId) {
        String sql = String.format("SELECT processDocId FROM print_task_log WHERE accountId = '%s'", accountId);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * @Description: 查询打印文档
     * @Author: wenmin
     * @Date: 2018/11/23 15:27
     * @Param: []
     * @Return: java.lang.String
     */
    public static String getProccessIdByArchived() {
        String sql = "SELECT id FROM sf_process_info \n" +
                "WHERE flow_type = 'common' AND sign_status=2 AND is_archived = 2  ORDER BY gmt_create DESC";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * 功能描述:通过信封状态/开启打印设置/冻结状态查询信封Id
     *
     * @auther: hupo
     * @date: 2018/12/19 20:41
     * @param: [status]
     * @return: java.lang.String
     */
    public static String getProccessPrintInfoId(int status) {
        String sql = "SELECT a.id FROM sf_process_info a,archive_doc b";
        sql = String.format(sql + " WHERE a.id = b.envelopeId AND b.viewControl >= 3 AND a.is_archived = 1 AND a.is_print_control = 1 AND a.flow_type = 'common' AND a.forzen_status = 0 AND a.sign_status = %d ORDER BY gmt_create DESC limit 1", status);
        System.out.println(sql);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * 功能描述:获取签署流程processId
     *
     * @auther: jiuge
     * @date: 2021/03/11 20:41
     * @param: [status]
     * @return: java.lang.String
     */
    public static String getorganizeSignProcessId(String accountId,String organizeId,int signStatus) {
        String sql = "SELECT process_id FROM sf_process_actor a, sf_process_info b \n" +
                "WHERE a.process_id=b.id AND a.signer_account_gid = '"+accountId+"' AND b.sign_status='"+signStatus+"' AND a.authorization_organize_gid = '"+organizeId+"' AND a.role_id=1 limit 1";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static String getArchiveId(String processId) {
        String sql = "SELECT a.id FROM archive_doc a ";
        sql = String.format(sql + " WHERE a.envelopeId = %s AND a.viewControl >= 3 limit 1", processId);
        System.out.println(sql);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * 功能描述:通过文档Id获取文档filekey
     *
     * @auther: hupo
     * @date: 2018/12/18 19:49
     * @param: [docId]
     * @return: java.lang.String
     */
    public static String getSignFileKeyByDocId(int docId) {
        String sql = " select fileKey from sf_sign_doc a,sf_process_doc b, sf_process_info c WHERE b.sign_doc_id= a.id  and c.id=b.process_id ";
        sql = String.format(sql + "and b.sign_doc_id = '%s' ", docId);
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /*获取签署场景Id*/
    public static String getSignDefinitionId(String query) {
        String sql = " select id from sign_scene_definition ";
        if (!query.isEmpty()) {
            sql = sql + " where " + query + " and deleted =0 order by create_date desc";
        }
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /*获取签署场景Num*/
    public static String getSignDefinitionNum(String query) {
        String sql = " select serial_number from sign_scene_definition ";
        if (!query.isEmpty()) {
            sql = sql + " where " + query + " and deleted =0 order by create_date desc";
        }
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    /**
     * @author: huaying
     * @date: 2019-2-21 15:31
     * @description: 获取扩展字段Id
     */
    public static String getExtId() {
        String sql = " select id from ext_info_dictionary where status  = 1 order by createTime desc"; //order by createTime desc
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static String getExtPersonId(String query) {
        String sql = "select id from account_outer";
        if (!query.isEmpty()) {
            sql = sql + " where " + query;
        }
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static String getExtPersonUniqueId(String query) {
        String sql = "select id from account_outer";
        if (!query.isEmpty()) {
            sql = sql + " where " + query;
        }
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static String getOutAccountUniqueId(String accountId) {
        String sql = "select `uniqueId` from account_outer";
        if (!accountId.isEmpty()) {
            sql = sql + " where id = " + accountId + " limit 1";
        }
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, data);
        return data;
    }

    public static String getSealRequestId(String query) {
        String sql = "select id from seal_request"; //order by createTime desc
        if (!query.isEmpty()) {
            sql = sql + " where " + query + "order by createTime desc";
        }
        DBConn conn = DBUtil.esignpro();
        String data = "";
        if (null != conn.fetchOne(sql)) {
            data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
        }

        return data;
    }

    // 删除用户与角色绑定关系(逻辑删除)
    public static void updateAccountRoleRef(Object scopeId) {
        String sql = "UPDATE role_account_ref SET STATUS = 0 WHERE scopeId = " + scopeId;
        DBConn conn = DBUtil.esignpro();
        int result = conn.updateSql(sql);
        if (result == 1) {
            Reporter.log("[数据库更新成功]： " + sql);
            System.out.println("[数据库更新成功]： " + sql);
        } else {
            Reporter.log("[数据库更新成失败]： " + sql);
            System.out.println("[数据库更新失败]： " + sql);
        }
    }

    // 删除用户机构权限绑定关系(逻辑删除)
    public static void updateScopeDepsRef(Object scopeId) {
        String sql = "UPDATE scope_deps_ref SET STATUS = 0, permission = 0 WHERE scopeId = " + scopeId;
        DBConn conn = DBUtil.esignpro();
        int result = conn.updateSql(sql);
        if (result > 1) {
            Reporter.log("[数据库更新成功]： " + sql);
            System.out.println("[数据库更新成功]： " + sql);
        } else {
            Reporter.log("[数据库更新成失败]： " + sql);
            System.out.println("[数据库更新失败]： " + sql);
        }
    }

    // 修改数据库操作
    public static void updateDatabase(String sql) {
        DBConn conn = DBUtil.esignpro();
        int result = conn.updateSql(sql);
        if (result > 0) {
            Reporter.log("[数据库更新成功]： " + sql);
            System.out.println("[数据库更新成功]： " + sql);
        } else {
            Reporter.log("[数据库更新成失败]： " + sql);
            System.out.println("[数据库更新失败]： " + sql);
        }
    }

    // 查询数据库操作-返回一条
    public static Object selectFetchOne(String sql) {
        DBConn conn = DBUtil.esignpro();
        Object data = conn.fetchOne(sql);
        dbLogger(sql, data);
        return data;
    }

    // 查询数据库操作-返回多条
    public static List<Map<String, Object>> selectFetch(String sql) {
        DBConn conn = DBUtil.esignpro();
        List<Map<String, Object>> data = conn.fetchAll(sql);
        dbLogger(sql, data);
        return data;
    }

    // 获取scope_deps_ref表ID
    public static String getScopeRefId(Object scopeId, Object depId) {
        String sql = "SELECT * FROM scope_deps_ref WHERE scopeId = '" + scopeId + "' AND depId = '" + depId + "'";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();
        dbLogger(sql, data);
        return data;
    }

    /**
     * @author: huaying
     * @date: 2019-3-2715:41
     * @description: 获取模板的签署流Id
     */
    public static Integer getTemplateFlowId() {
        String sql = "SELECT id FROM template_flow where status = 1 order by createTime desc";
        DBConn conn = DBUtil.esignpro();
        Integer data = conn.fetchOne(sql).hashCode();

        dbLogger(sql, String.valueOf(data));
        return data;
    }

    /**
     * @author: huaying
     * @date: 2019-3-2716:48
     * @description: 获取模板的签署日期Id
     */
    public static Integer getTemplateSignDateId(Integer flowId) {
        String sql = "SELECT signDateId FROM template_flow";
        sql = String.format(sql + " where status=1 and id = '%s'", flowId) + " ORDER BY createTime desc;";

        DBConn conn = DBUtil.esignpro();
        Integer data = conn.fetchOne(sql).hashCode();

        dbLogger(sql, String.valueOf(data));
        return data;
    }

    /**
     * @author: huaying
     * @date: 2019-3-2716:48
     * @description: 获取模板的主体Id
     */
    public static Integer getTemplatePredefinedId(Integer flowId) {
        String sql = "SELECT predefineId FROM template_flow";
        sql = String.format(sql + " where status=1 and id = '%s'", flowId) + " ORDER BY createTime desc;";

        DBConn conn = DBUtil.esignpro();
        Integer data = conn.fetchOne(sql).hashCode();

        dbLogger(sql, String.valueOf(data));
        return data;
    }

    /**
     * @author: huaying
     * @date: 2019-3-2716:48
     * @description: 获取模板文档的模板Id
     */
    public static String getTemplateId() {
        String sql = "SELECT templateId FROM template where status = 1 order by createTime desc";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();
        dbLogger(sql, data);
        return data;
    }

    public static String getrocessActorId(String flowId) {
        String sql = "SELECT process_actor_id FROM sf_assign_cert_seal where process_id = " + flowId + " order by create_time desc";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();
        dbLogger(sql, data);
        return data;
    }

    //查询混合云开关的情况
    public static String getCloudSigningSwich() {
        String sql = "SELECT value FROM properties WHERE paramKey='publicCloudSign'";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();
        dbLogger(sql, data);
        return data;
    }

    //修改混合云开关参数
    public static void updateCloudSigning(Integer techSign, Integer signWay) {
//        String sql = String.format("UPDATE properties SET value='{\"signModel\":1,\"signWay\":%d,\"localSignEvidence\":1,\"techSign\":%d}' WHERE paramKey='publicCloudSign'",signWay,techSign);
        String sql = "select * from project_properties_relation limit 1;";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();
        dbLogger(sql, data);
    }

    public static void updateCloudSigning2(Integer techSign, Integer signWay, String project_key) {
//        String sql = String.format("UPDATE project_properties_relation SET value='{\"signModel\":1,\"signWay\":%d,\"localSignEvidence\":1,\"techSign\":%d}' WHERE param_key='publicCloudSign' and project_key='%s'",signWay,techSign,project_key);
        String sql = "select * from project_properties_relation limit 1;";
        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();
        dbLogger(sql, data);
    }

    public static String getApprovalInfoFlowId() {
        String sql = "SELECT process_id FROM sf_sign_doc_task WHERE id In (SELECT taskId FROM sf_sign_doc_task_detail WHERE `status`=3) order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getApprovalInfoFlowId1() {
        String sql = "SELECT process_id FROM sf_sign_doc_task WHERE id In (SELECT taskId FROM sf_sign_doc_task_detail WHERE `status`=1) order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getSignFlowProgressFlowId(String accountId) {
        String sql = "SELECT process_id FROM sf_sign_doc_task WHERE id In (SELECT taskId FROM sf_sign_doc_task_detail WHERE `signer_account_gid`= '" + accountId + "') order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getSignFlowTaskId() {
        String sql = "SELECT taskId FROM sf_sign_doc_task_detail WHERE sign_type = '4' order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getSignFlowTaskId(String accountId) {
        String sql = "SELECT taskId FROM sf_sign_doc_task_detail WHERE sign_type = '4' and signer_account_gid = '" + accountId + "' order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getApiStatusFlowId(String status, String accountId) {
        String sql = "SELECT process_id FROM sf_process_actor WHERE allow_api_sign = 1 and status = '" + status + "' and signer_account_gid = '" + accountId + "' order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getApicountFlowId(int count, String accountId) {
        String sql = "SELECT process_id FROM sf_process_actor WHERE allow_api_sign = 1 and status = '1' and signer_account_gid = '" + accountId + "' order by id desc limit " + count + ",1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getQrCodeDocId() {
        String sql = "SELECT id FROM sf_sign_doc order by id asc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getSignLogId(String accountId) {
        String sql = "SELECT id FROM sign_log where accountId = '" + accountId + "'order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getApplyPrintFlowId() {
        String sql = "SELECT process_doc_id FROM print_apply order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getPrintArchiveId(String accountId) {
        String sql = "select Id from archive_doc where accountId = '" + accountId + "' order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getSignDocId(String accountId) {
        String sql = "SELECT id from sf_process_doc where process_id in (select envelopeId from archive_doc where accountId = '" + accountId + "') limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getSealId() {
        String sql = "SELECT seal_id from seal where status = 1 order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    public static String getDocManagerId(String docId) {
        String sql = "SELECT doc_manager_id FROM sf_sign_doc WHERE id=" + docId;
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }

    /**
     * @Description: 根据流程模板状态查询获取流程模板id
     * @Param: [status]：0 初始化 10 启用 11 停用 20 已删除
     * @Return: java.lang.String
     * @Author: Liu Hong
     * @Create: 2020/11/10 15:35
     **/
    public static String getFlowTemplateId(Integer status) {
        String sql = "SELECT id FROM fl_sign_flow_template WHERE status=" + status + " order by id desc limit 1";
        DBConn conn = DBUtil.esignpro();
        if (conn.fetchOne(sql) != null) {
            String data = conn.fetchOne(sql).toString();
            dbLogger(sql, data);
            return data;
        }
        return null;
    }


    /**
     * @Description: 通过id值查询某张表的某个字段信息
     * @Author: wenmin
     * @Date: 2020/10/15 16:22
     * @Param:
     * @Return:
     */
    public static String getInfoById(String id, String tableName, String colName) {
        String sql = String.format("SELECT %s FROM %s where id = '%s'", colName, tableName, id);

        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, String.valueOf(data));
        return data;
    }

    /**
     * @Description: 根据流程状态获取flowId  0 待开启 10 进行中 30 发起失败 40 发起成功 50 已删除 60 发起中
     * @Param: [status]
     * @Return: java.lang.String
     * @Author: Liu Hong
     * @Create: 2020/11/23 17:04
     **/
    public static String getFlowIdByStatus(String status) {
        String sql = String.format("SELECT id from fl_running_flow where status = %s ORDER BY create_time desc  LIMIT 1", status);

        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, String.valueOf(data));
        return data;
    }

    /**
      *@Description: 根据templateId获取id
      *@Author: Liu Hong
      *@Create: 2021/1/11 14:13
    **/
    public static String getIdByTemplateId(String templateId){
        String sql = String.format("SELECT id from template where templateId = '%s' ORDER BY createTime desc  LIMIT 1", templateId);

        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, String.valueOf(data));
        return data;
    }

    /**
     * @Description: 获取fl_running_handle表数据，根据状态
     * @Param: [status]
     * @Return: java.lang.String
     * @Author: Liu Hong
     * @Create: 2020/11/23 17:10
     **/
    public static String getHandleIdByStatus(String status) {
        String sql = String.format("SELECT id from fl_running_handle where status = %s ORDER BY create_time desc  LIMIT 1", status);

        DBConn conn = DBUtil.esignpro();
        String data = conn.fetchOne(sql).toString();

        dbLogger(sql, String.valueOf(data));
        return data;
    }

    /**
     * 功能描述: 根据typeId查询对应的parentId
     */
    public static Integer getTemplateTypeParentId(int typeId) {
        String sql = "SELECT parentId from template_type where id ="+typeId;
        DBConn conn = DBUtil.esignpro();
        Integer data = conn.fetchOne(sql).hashCode();

        dbLogger(sql, String.valueOf(data));
        return data;
    }


    public static void dbLogger(String sql, Object data) {
        Reporter.log("[数据库查询语句]： " + sql);
        Reporter.log("[数据库查询结果]： " + data);
        System.out.println("[数据库查询语句]： " + sql);
        System.out.println("[数据库查询结果]： " + data + "\n");
    }
}
