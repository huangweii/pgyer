package com.pgyer.simple.pinpoint.tree;

import com.pgyer.simple.pinpoint.trace.Span;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private List<Span> list = new ArrayList<Span>();

    public void addSpan(Span span) {
        list.add(span);
    }

    public void call() {
        int size = list.size();
        if (size == 0) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder("调用过程:");
        stringBuilder.append("\r\n");
        stringBuilder.append("\r\n");
        int num = 1;
        int depthMax = 0;
        for (int i = 0; i < size; i++) {
            if (list.get(i).getParentSpanId() == -1) {
                stringBuilder.append("方法" + num + ":");
                stringBuilder.append("\r\n");
                stringBuilder.append(getMethod(list.get(i), num));
                stringBuilder.append("\r\n");
                stringBuilder.append("\r\n");
                num++;
                long id = list.get(i).getSpanId();
                for (int j = 0; j < size; j++) {
                    if (list.get(j).getParentSpanId() == id) {
                        stringBuilder.append("方法" + num + ":");
                        stringBuilder.append(getMethod(list.get(j), num));
                        stringBuilder.append("\r\n");
                        stringBuilder.append("\r\n");
                        id = list.get(j).getSpanId();
                        num++;
                        j = -1;
                    }
                }
            }
            if (list.get(i).getDepthId() > depthMax) {
                depthMax = list.get(i).getDepthId();
            }
        }
        stringBuilder.append("最大调用深度:" + depthMax);
        System.out.println(stringBuilder.toString());
    }


    private String getMethod(Span span, int num) {
        return span.getDescriptor().getFullName()+"\r\n"
                + "开始时刻:" + span.getStartElapsed() + ",耗时:" + span.getEndElapsed() + "\r\n"
                + "mid:" + num + "\r\n"
                + "memory:" + span.getStartMemoryAndCpu().getGc().toString() + " -- " + span.getEndMemoryAndCpu().getGc().toString() + "\r\n"
                + "cpu:" + span.getStartMemoryAndCpu().getCpuLoad().toString() + " -- " + span.getEndMemoryAndCpu().getCpuLoad().toString()+"\r\n"
                + "当前方法cpuTime:" + span.getUsedMemoryAndCpu().getCpuLoad().getJvmCpuTime()+"\r\n"
                + "当前方法userTime:"+span.getUsedMemoryAndCpu().getCpuLoad().getJvmUserCpuTime();
    }
}
