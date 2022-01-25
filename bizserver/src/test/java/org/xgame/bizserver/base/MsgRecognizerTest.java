package org.xgame.bizserver.base;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.xgame.bizserver.msg.CommProtocol;

public class MsgRecognizerTest {
    @Test
    public void getMsgCodeTest() {
        int msgCode = MsgRecognizer.getInstance().getMsgCode(CommProtocol.PingCmd.class);
        Assert.assertEquals(msgCode, CommProtocol.CommMsgCodeDef._PingCmd_VALUE);
    }

    @Test
    public void getMsgBodyTest() {
        CommProtocol.ErrorHintResult msg0 = CommProtocol.ErrorHintResult.newBuilder()
            .setErrorCode(1024)
            .setErrorMsg("这是一个测试错误")
            .build();

        int msgCode = MsgRecognizer.getInstance().getMsgCode(msg0);
        Parser<? extends GeneratedMessageV3> msgParser = MsgRecognizer.getInstance().getMsgParserByMsgCode(msgCode);
        assert null != msgParser;

        try {
            GeneratedMessageV3 msg1 = msgParser.parseFrom(msg0.toByteArray());

            Assert.assertTrue(msg1 instanceof CommProtocol.ErrorHintResult);
            CommProtocol.ErrorHintResult msg2 = (CommProtocol.ErrorHintResult) msg1;

            Assert.assertTrue(msg2.getErrorCode() == msg0.getErrorCode());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
