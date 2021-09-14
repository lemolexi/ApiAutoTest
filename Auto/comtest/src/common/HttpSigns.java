/**
 * FileName: HttpSigns
 * Author:   hupo
 * Date:     2020/8/27 17:57
 * Description: 测试
 * version: IT20200827
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package common;


import org.apache.http.client.methods.HttpPost;
import tgtest.utils.http.HttpConfig;
import tgtest.utils.http.HttpSign;
import tgtest.utils.http.ProjectAccesser;

public class HttpSigns {
    public static void signature(HttpPost req, HttpConfig config) throws Exception {
        ProjectAccesser project = config.getProjectAccesser();
        req.addHeader("X-timevale-project-id", project.getProjectId());
        req.addHeader("X-timevale-mode", config.getParameterType().getType());
        req.addHeader("X-timevale-signature-algorithm", project.getSignAlg().getDisc());
        req.addHeader("X-timevale-signature", HttpSign.sign(new byte[]{}, project));
    }

}