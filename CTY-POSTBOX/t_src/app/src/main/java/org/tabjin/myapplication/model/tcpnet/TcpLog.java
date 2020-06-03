package org.tabjin.myapplication.model.tcpnet;

import org.tabjin.myapplication.Tool;
import org.tabjin.myapplication.base.Constant;

/**
 * tcp日志工具
 */
public class TcpLog {
    public static void d(String msg) {
        StringBuilder logMsg = new StringBuilder(Tool.D.getNowYMD_HMS());
        logMsg.append(msg).append("\r\n");
        Tool.F.appendTextTofile(logMsg.toString(), Constant.TCPEXPATH,Constant.TCPEXFILENAME,20);
    }
}
