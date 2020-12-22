package test.com.yuxz;

import com.yuxz.CommonUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * CommonUtil Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Dec 22, 2020</pre>
 */
public class CommonUtilTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: checkFileExist(String[] sqlFiles)
     */
    @Test
    public void testCheckFileExist() throws Exception {
        boolean flag = CommonUtil.checkFileExist(new String[]{"./tidb-datasource.properties"});
        Assert.assertTrue(flag);
    }

    /**
     * Method: generatePseudoSqls(int strLength, int sql)
     */
    @Test
    public void testGeneratePseudoSqls() throws Exception {
        List<Integer> sql1 = CommonUtil.generatePseudoSqls(2, 1);
        Assert.assertEquals(String.valueOf(sql1), "[1, 1]");

    }

    /**
     * Method: getFileLineNum(String filePath)
     */
    @Test
    public void testGetFileLineNum() throws Exception {
//TODO: Test goes here... 
    }


} 
