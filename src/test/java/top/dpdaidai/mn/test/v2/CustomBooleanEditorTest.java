package top.dpdaidai.mn.test.v2;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.propertyeditors.CustomBooleanEditor;

/**
 * @Author chenpantao
 * @Date 9/13/21 4:57 PM
 * @Version 1.0
 */
public class CustomBooleanEditorTest {

    @Test
    public void testConvertStringToBoolean() {
        CustomBooleanEditor customBooleanEditor = new CustomBooleanEditor(true);

        customBooleanEditor.setAsText("true");
        Assert.assertEquals(true, ((Boolean) customBooleanEditor.getValue()).booleanValue());
        customBooleanEditor.setAsText("false");
        Assert.assertEquals(false, ((Boolean)customBooleanEditor.getValue()).booleanValue());

        customBooleanEditor.setAsText("on");
        Assert.assertEquals(true, ((Boolean)customBooleanEditor.getValue()).booleanValue());
        customBooleanEditor.setAsText("off");
        Assert.assertEquals(false, ((Boolean)customBooleanEditor.getValue()).booleanValue());


        customBooleanEditor.setAsText("yes");
        Assert.assertEquals(true, ((Boolean)customBooleanEditor.getValue()).booleanValue());
        customBooleanEditor.setAsText("no");
        Assert.assertEquals(false, ((Boolean)customBooleanEditor.getValue()).booleanValue());

        try{
            customBooleanEditor.setAsText("aabbcc");
        }catch(IllegalArgumentException e){
            return;
        }
        Assert.fail();

    }


}
