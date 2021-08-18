package testNg;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * FileName: demo
 * Author:   wanglin
 * Date:     2021/8/16 10:32
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class demo {

    @DataProvider(name = "test001")
    public Object[][] testdata(){
        Object[][] obj = {
                {"王林","18"},
                {"玉清","20"}
        };
        return obj;
    }

    @Test(dataProvider = "test001")
    public void test(String name, String age){
        System.out.println("name = "+ name + ";" + "age = " + age);
    }
}
