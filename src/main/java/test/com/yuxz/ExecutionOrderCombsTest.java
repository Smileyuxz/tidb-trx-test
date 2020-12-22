package test.com.yuxz;

import com.yuxz.ExecutionOrderCombs;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExecutionOrderCombs Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Dec 22, 2020</pre>
 */
public class ExecutionOrderCombsTest {
    private List<List<Integer>> executionOrderCombs;

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: permutation(String[] sqlFiles, Map<Integer, String> pseudoSqlMap)
     */
    @Test
    public void testPermutation() throws Exception {
        String[] args = new String[]{"/Users/yuxiuzhen/IdeaProjects/ALL_IMS/tidb-trx-test2/sql/sql2", "/Users/yuxiuzhen/IdeaProjects/ALL_IMS/tidb-trx-test2/sql/sql1"};

        Map<Integer, String> pseudoSqlMap = new HashMap<Integer, String>();
        List<List<Integer>> sqlCombs = new ExecutionOrderCombs().permutation(args, pseudoSqlMap);
        Assert.assertEquals(sqlCombs.size(), 6);
    }

    /**
     * Method: backtrack(List<Integer> permutationSqls, List<List<Integer>> executionOrderCombs, int idx, List<Integer> permutationSql)
     */
    @Test
    public void testBacktrack() throws Exception {

    }


}
