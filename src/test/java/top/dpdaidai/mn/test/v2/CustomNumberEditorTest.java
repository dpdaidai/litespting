package top.dpdaidai.mn.test.v2;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.propertyeditors.CustomNumberEditor;

/**
 * @Author chenpantao
 * @Date 9/13/21 5:10 PM
 * @Version 1.0
 */
public class CustomNumberEditorTest {

    @Test
    public void testConvertString() {
        CustomNumberEditor editor = new CustomNumberEditor(Integer.class,true);

        editor.setAsText("3");
        Object value = editor.getValue();
        Assert.assertTrue(value instanceof Integer);
        Assert.assertEquals(3, ((Integer)editor.getValue()).intValue());


        editor.setAsText("");
        Assert.assertTrue(editor.getValue() == null);


        try{
            editor.setAsText("3.1");

        }catch(IllegalArgumentException e){
            return ;
        }
        Assert.fail();

    }



}
