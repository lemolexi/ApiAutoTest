package testNg;

import com.beust.jcommander.Parameter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * FileName: testparamer001
 * Author:   wanglin
 * Date:     2021/8/16 10:43
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class testparamer001 {

    @Test
    @Parameters({"name","age"})
    public void test(String name, String age){
        System.out.println("name = " + name + ";" + "age = " + age);
    }

}
